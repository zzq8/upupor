package com.upupor.web.controller;

import com.upupor.service.common.*;
import com.upupor.service.dao.entity.File;
import com.upupor.service.dao.entity.Member;
import com.upupor.service.listener.event.MemberRegisterEvent;
import com.upupor.service.service.aggregation.service.FileService;
import com.upupor.service.service.aggregation.service.MemberIntegralService;
import com.upupor.service.service.aggregation.service.MemberService;
import com.upupor.service.service.aggregation.service.MessageService;
import com.upupor.service.utils.CcUtils;
import com.upupor.service.utils.RedisUtil;
import com.upupor.service.utils.ServletUtils;
import com.upupor.spi.req.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

import static com.upupor.service.common.CcConstant.SKIP_SUBSCRIBE_EMAIL_CHECK;


/**
 * 用户
 *
 * @author: YangRunkang(cruise)
 * @created: 2019/12/23 01:51
 */
@Slf4j
@Api(tags = "用户服务")
@RestController
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

    private final MemberService memberService;
    private final MessageService messageService;
    private final MemberIntegralService memberIntegralService;
    private final FileService fileService;
    private final String register = "register";
    private final String forgetPassword = "forgetPassword";
    private final ApplicationEventPublisher eventPublisher;
    /**
     * 当前激活的环境
     */
    @Value("${upupor.env}")
    private String activeEnv;

    @ApiOperation("用户登录")
    @PostMapping("get")
    @ResponseBody
    public CcResponse get(GetMemberReq getMemberReq) {
        CcResponse cc = new CcResponse();
        Boolean login = memberService.login(getMemberReq);
        if (!login) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        cc.setData(true);
        return cc;
    }


    @ApiOperation("用户注册")
    @PostMapping("add")
    @ResponseBody
    public CcResponse add(AddMemberReq addMemberReq) {

        CcResponse cc = new CcResponse();

        if (StringUtils.isEmpty(addMemberReq.getUserName())) {
            throw new BusinessException(ErrorCode.USER_NAME_CAN_NOT_EMPTY);
        } else {
            checkUserName(addMemberReq.getUserName());
        }

        // 检测用户是否存在
        memberService.checkUserExists(addMemberReq);

        // 先校验验证码是否存在 用户注册 RedisKey组成: source + email + 验证码
        String key = register + addMemberReq.getEmail().trim() + addMemberReq.getVerifyCode();
        String value = RedisUtil.get(key);
        if (StringUtils.isEmpty(value)) {
            throw new BusinessException(ErrorCode.VERIFY_CODE_ERROR);
        }

        // 检查验证码是否正确
        if (!addMemberReq.getVerifyCode().trim().equals(value)) {
            throw new BusinessException(ErrorCode.VERIFY_CODE_ERROR);
        }

        Member member = memberService.register(addMemberReq);
        if (Objects.isNull(member)) {
            throw new BusinessException(ErrorCode.REGISTER_FAILED);
        }

        // 注册成功后自动登录
        // 设置登录成功Session
        ServletUtils.getSession().setAttribute(CcConstant.Session.USER_ID, member.getUserId());
        ServletUtils.getSession().setAttribute(CcConstant.Session.USER_VIA, member.getVia());
        ServletUtils.getSession().setAttribute(CcConstant.Session.USER_NAME, member.getUserName());

        // 注册消息
        MemberRegisterEvent memberRegisterEvent = new MemberRegisterEvent();
        memberRegisterEvent.setUserId(member.getUserId());
        eventPublisher.publishEvent(memberRegisterEvent);

        cc.setData(true);
        return cc;
    }


    @ApiOperation("登出")
    @GetMapping("/logout")
    @ResponseBody
    public CcResponse logoutConfirm() {
        // 清除session
        ServletUtils.getSession().invalidate();

        return new CcResponse();
    }

    @ApiOperation("编辑用户")
    @PostMapping("edit")
    @ResponseBody
    public CcResponse edit(UpdateMemberReq updateMemberReq) throws Exception {
        CcResponse cc = new CcResponse();

        if (StringUtils.isEmpty(updateMemberReq.getUserName())) {
            throw new BusinessException(ErrorCode.USER_NAME_CAN_NOT_EMPTY);
        } else {
            checkUserName(updateMemberReq.getUserName());
        }

        String userId = ServletUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR_USER_ID);
        }

        updateMemberReq.setUserId(userId);
        Boolean editSuccess = memberService.editMember(updateMemberReq);
        if (!editSuccess) {
            throw new BusinessException(ErrorCode.EDIT_MEMBER_FAILED);
        }
        // 变更用户名
        ServletUtils.getSession().setAttribute(CcConstant.Session.USER_NAME, updateMemberReq.getUserName());
        cc.setData(true);
        return cc;
    }

    private void checkUserName(String userName) {
        if (userName.toLowerCase().contains("test") || userName.contains("测试")) {
            throw new BusinessException(ErrorCode.USER_NAME_ERROR);
        }
    }

    @ApiOperation("设置背景样式")
    @PostMapping("edit/bg-style-settings")
    @ResponseBody
    public CcResponse bgStyleSettings(UpdateCssReq updateCssReq) throws Exception {
        CcResponse cc = new CcResponse();
        String userId = ServletUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR_USER_ID);
        }

        updateCssReq.setUserId(userId);
        Boolean editSuccess = memberService.editMemberBgStyle(updateCssReq);
        if (!editSuccess) {
            throw new BusinessException(ErrorCode.REGISTER_FAILED);
        }
        cc.setData(true);
        return cc;
    }

    @ApiOperation("重置密码")
    @PostMapping("resetPassword")
    @ResponseBody
    public CcResponse resetPassword(UpdatePasswordReq updatePasswordReq) {
        CcResponse cc = new CcResponse();
        String email = updatePasswordReq.getEmail().trim();
        if (StringUtils.isEmpty(email)) {
            throw new BusinessException(ErrorCode.EMAIL_EMPTY);
        }

        // 校验邮箱唯一性
        memberService.checkUserExists(email);

        // 先校验验证码是否存在 用户注册 RedisKey组成: source + email + 验证码
        String key = forgetPassword + updatePasswordReq.getEmail().trim() + updatePasswordReq.getVerifyCode();
        String value = RedisUtil.get(key);
        if (StringUtils.isEmpty(value)) {
            throw new BusinessException(ErrorCode.VERIFY_CODE_ERROR);
        }

        // 检查验证码是否正确
        if (!updatePasswordReq.getVerifyCode().trim().equals(value)) {
            throw new BusinessException(ErrorCode.VERIFY_CODE_ERROR);
        }

        Boolean reset = memberService.resetPassword(updatePasswordReq);
        if (!reset) {
            throw new BusinessException(ErrorCode.RESET_PASSWORD_FAILURE);
        }
        cc.setData(true);
        return cc;
    }

    @ApiOperation("发送验证码")
    @PostMapping("/sendVerifyCode")
    @ResponseBody
    public CcResponse sendVerifyCode(AddVerifyCodeReq addVerifyCodeReq) {
        CcResponse ccResponse = new CcResponse();

        String verifyCode = CcUtils.getVerifyCode();

        String email = addVerifyCodeReq.getEmail().trim();
        if (!email.contains(CcConstant.AT)) {
            throw new BusinessException(ErrorCode.WRONG_EMAIL);
        }

        String emailTitle;
        String emailContent = "验证码: " + verifyCode;
        if (addVerifyCodeReq.getSource().equals(register)) {
            emailTitle = "Upupor新用户注册";

        } else if (addVerifyCodeReq.getSource().equals(forgetPassword)) {
            // 检测是否是本站用户
            memberService.checkUserExists(addVerifyCodeReq.getEmail());
            emailTitle = "Upupor重置密码";
        } else {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "发送验证码来源信息错误");
        }

        if ("dev".equals(activeEnv)) {
            log.info("开发环境验证码:{}", verifyCode);
        }

        // 验证码的短信要跳过是否订阅邮件配置
        messageService.sendEmail(email, emailTitle, emailContent, SKIP_SUBSCRIBE_EMAIL_CHECK);

        // Redis缓存90s 用户注册 RedisKey组成: source + email + 验证码
        String key = addVerifyCodeReq.getSource() + addVerifyCodeReq.getEmail().trim() + verifyCode;
        RedisUtil.set(key, verifyCode, 90L);
        ccResponse.setData(true);
        return ccResponse;
    }

    @ApiOperation("领取今日积分")
    @PostMapping("dailyPoints")
    @ResponseBody
    public CcResponse dailyPoints() {

        CcResponse ccResponse = new CcResponse();

        Boolean exists = memberService.checkIsGetDailyPoints();
        if (exists) {
            throw new BusinessException(ErrorCode.ALREADY_GET_DAILY_POINTS);
        }

        String userId = ServletUtils.getUserId();

        memberIntegralService.addIntegral(IntegralEnum.DAILY_POINTS, userId, userId);
        return ccResponse;
    }

    @ApiOperation("变更头像")
    @PostMapping("updateVia")
    @ResponseBody
    public CcResponse updateVia(UpdateViaReq updateViaReq) {

        CcResponse ccResponse = new CcResponse();

        String via = updateViaReq.getVia();
        File fileByFileUrl = fileService.selectByFileUrl(via);
        if (Objects.isNull(fileByFileUrl)) {
            throw new BusinessException(ErrorCode.SELECTED_VIA_NOT_EXISTS);
        }
        String userId = ServletUtils.getUserId();
        Member member = memberService.memberInfo(userId);
        member.setVia(via);
        member.setSysUpdateTime(new Date());
        Boolean result = memberService.update(member);
        if (result) {
            ServletUtils.getSession().setAttribute(CcConstant.Session.USER_VIA, via);
        }

        ccResponse.setData(result);
        return ccResponse;
    }


}
