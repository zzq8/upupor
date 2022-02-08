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

package com.upupor.web.page.member;

import com.upupor.framework.CcConstant;
import com.upupor.service.business.aggregation.MemberAggregateService;
import com.upupor.web.page.abstracts.AbstractView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.upupor.framework.CcConstant.UserView.USER_LIST;

/**
 * 用户列表
 * @author Yang Runkang (cruise)
 * @date 2022年02月06日 10:32
 * @email: yangrunkang53@gmail.com
 */
@RequiredArgsConstructor
@Component
public class UserListView extends AbstractView {
    private final MemberAggregateService memberAggregateService;
    public final static String URL = "/list-user";

    @Override
    public String viewName() {
        return USER_LIST;
    }

    @Override
    protected void seoInfo() {
        modelAndView.addObject(CcConstant.SeoKey.TITLE, "所有用户");
        modelAndView.addObject(CcConstant.SeoKey.DESCRIPTION, "所有用户");
    }

    @Override
    protected void fetchData() {
        modelAndView.addObject(memberAggregateService.userList(pageNum, pageSize));
    }

    @Override
    public String adapterUrlToViewName(String pageUrl) {
        if(pageUrl.equals(URL)){
            return viewName();
        }
        return pageUrl;
    }

    @Override
    public String prefix() {
        return CcConstant.UserView.BASE_PATH;
    }
}
