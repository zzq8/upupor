/*
 * MIT License
 *
 * Copyright (c) 2021-2022 yangrunkang
 *
 * Author: yangrunkang
 * Email: yangrunkang53@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.upupor.web.page;

import com.upupor.framework.CcConstant;
import com.upupor.service.business.aggregation.MemberAggregateService;
import com.upupor.service.business.aggregation.service.MemberService;
import com.upupor.service.common.BusinessException;
import com.upupor.service.common.ErrorCode;
import com.upupor.service.utils.ServletUtils;
import com.upupor.web.page.abstracts.AbstractView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import joptsimple.internal.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static com.upupor.framework.CcConstant.*;


/**
 * 用户页面跳转控制器
 *
 * @author YangRunkang(cruise)
 * @date 2020/01/19 12:16
 */
@Slf4j
@Api(tags = "用户页面跳转控制器")
@RestController
@RequiredArgsConstructor
public class MemberPageJumpController {

    private final MemberAggregateService memberAggregateService;
    private final MemberService memberService;
    private final List<AbstractView> abstractViewList;

    @GetMapping({
            "/logout", // 登出
            "/register", // 注册
            "/forget-password", // 忘记密码
    })
    public ModelAndView all(HttpServletRequest request){
        String servletPath = request.getServletPath();
        for (AbstractView abstractView : abstractViewList) {
            if(abstractView.viewName().replace(UserView.BASE_PATH, Strings.EMPTY).equals(servletPath)){
                return abstractView.doBusiness();
            }
        }
        throw new BusinessException(ErrorCode.NONE_PAGE);
    }

    @ApiOperation("列出所有用户")
    @GetMapping("/list-user")
    public ModelAndView userList(Integer pageNum, Integer pageSize) {
        if (Objects.isNull(pageNum)) {
            pageNum = CcConstant.Page.NUM;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = CcConstant.Page.SIZE;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(USER_LIST);
        modelAndView.addObject(memberAggregateService.userList(pageNum, pageSize));
        modelAndView.addObject(SeoKey.TITLE, "所有用户");
        modelAndView.addObject(SeoKey.DESCRIPTION, "所有用户");
        return modelAndView;
    }


    @ApiOperation("登录")
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(USER_LOGIN);

        modelAndView.addObject(SeoKey.TITLE, "登录");
        modelAndView.addObject(SeoKey.DESCRIPTION, "登录");
        return modelAndView;
    }





    @ApiOperation("退订邮件")
    @GetMapping("unsubscribe-mail")
    @ResponseBody
    public ModelAndView unSubscribeMail() {
        ModelAndView modelAndView = new ModelAndView();

        String userId = ServletUtils.getUserId();
        String result = "result";
        Boolean success = memberService.unSubscribeMail(userId);
        modelAndView.addObject(result, success);

        modelAndView.setViewName(UNSUBSCRIBE_MAIL);
        modelAndView.addObject(SeoKey.TITLE, "退订邮件通知");
        modelAndView.addObject(SeoKey.DESCRIPTION, "退订,邮件");
        return modelAndView;
    }


}
