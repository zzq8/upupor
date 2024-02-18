/*
 * <!--
 *   ~ MIT License
 *   ~
 *   ~ Copyright (c) 2021-2022 yangrunkang
 *   ~
 *   ~ Author: yangrunkang
 *   ~ Email: yangrunkang53@gmail.com
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   -->
 */

package com.upupor.service.business.member.business;

import com.upupor.framework.*;
import com.upupor.framework.common.UserCheckFieldType;
import com.upupor.framework.config.UpuporConfig;
import com.upupor.framework.utils.CcUtils;
import com.upupor.framework.utils.RedisUtil;
import com.upupor.service.business.member.abstracts.AbstractMember;
import com.upupor.service.business.member.common.MemberBusiness;
import com.upupor.service.business.message.MessageSend;
import com.upupor.service.business.message.model.MessageModel;
import com.upupor.service.outer.req.member.SendVerifyCodeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author Yang Runkang (cruise)
 * @createTime 2022-09-11 13:15
 * @email: yangrunkang53@gmail.com
 */
@Component
@Slf4j
public class SendVerifyCode extends AbstractMember<SendVerifyCodeReq> {
    @Resource
    private UpuporConfig upuporConfig;

    @Override
    public MemberBusiness memberBusiness() {
        return MemberBusiness.SEND_VERIFY_CODE;
    }

    @Override
    public CcResponse handle() {
        CcResponse ccResponse = new CcResponse();

        SendVerifyCodeReq addVerifyCodeReq = transferReq();

        String verifyCode = CcUtils.getVerifyCode();

        String email = addVerifyCodeReq.getEmail().trim();
        if (!email.contains(CcConstant.AT)) {
            throw new BusinessException(ErrorCode.WRONG_EMAIL);
        }

        String emailTitle;
        String emailContent = "验证码: " + verifyCode;
        if (addVerifyCodeReq.getSource().equals(REGISTER)) {
            emailTitle = "Gnmd 新用户注册";
            // 新用户注册,检查邮箱是否已经存在
            if (memberService.checkUserExists(addVerifyCodeReq.getEmail(), UserCheckFieldType.EMAIL)) {
                throw new BusinessException(ErrorCode.EMAIL_ALREADY_REGISTER);
            }
            // 新用户注册,检查用户名是否已经被其他人使用
            if (memberService.checkUserExists(addVerifyCodeReq.getUserName(), UserCheckFieldType.USER_NAME)) {
                throw new BusinessException(ErrorCode.USER_NAME_ALREADY_USED_BY_OTHERS);
            }
        } else if (addVerifyCodeReq.getSource().equals(FORGET_PASSWORD)) {
            // 忘记密码,需要检验邮箱是否已经注册
            if (!memberService.checkUserExists(addVerifyCodeReq.getEmail(), UserCheckFieldType.EMAIL)) {
                throw new BusinessException(ErrorCode.YOU_EMAIL_HAS_NOT_REGISTERED);
            }
            emailTitle = "Upupor重置密码";
        } else {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "发送验证码来源信息错误");
        }

        if ("dev".equals(upuporConfig.getEnv())) {
            log.info("开发环境验证码:{}", verifyCode);
        }

        // 邮件发送
        MessageSend.send(MessageModel.builder()
                .directEmailModel(MessageModel.DirectEmailModel.builder()
                        .email(email)
                        .build())
                .emailModel(MessageModel.EmailModel.builder()
                        .title(emailTitle)
                        .content(emailContent)
                        .build())
                .messageId(CcUtils.getUuId())
                .build());


        // Redis缓存90s 用户注册 RedisKey组成: source + email + 验证码
        String key = CcRedis.Key.memberVerifyCodeKey(addVerifyCodeReq.getSource(), addVerifyCodeReq.getEmail(), verifyCode);
        RedisUtil.set(key, verifyCode, 90L);
        ccResponse.setData(true);
        return ccResponse;
    }
}
