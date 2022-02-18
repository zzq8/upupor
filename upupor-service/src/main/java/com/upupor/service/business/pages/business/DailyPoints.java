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

package com.upupor.service.business.pages.business;

import com.upupor.framework.CcConstant;
import com.upupor.service.business.aggregation.MemberAggregateService;
import com.upupor.service.business.aggregation.service.MemberService;
import com.upupor.service.business.pages.AbstractView;
import com.upupor.service.common.IntegralEnum;
import com.upupor.service.dto.page.common.ListIntegralDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.upupor.framework.CcConstant.DAILY_POINTS;
import static com.upupor.framework.CcConstant.EVERY_DAILY_POINTS;

/**
 * @author Yang Runkang (cruise)
 * @date 2022年02月09日 12:53
 * @email: yangrunkang53@gmail.com
 */
@Component
@RequiredArgsConstructor
public class DailyPoints extends AbstractView {

    private final MemberAggregateService memberAggregateService;
    private final MemberService memberService;


    public static final String URL = "/daily-points";

    @Override
    public String viewName() {
        return EVERY_DAILY_POINTS;
    }

    @Override
    public String adapterUrlToViewName(String pageUrl) {
        if (pageUrl.equals(URL)) {
            return viewName();
        }
        return pageUrl;
    }

    @Override
    protected void seoInfo() {
        modelAndView.addObject(CcConstant.SeoKey.TITLE, "每日签到");
        modelAndView.addObject(CcConstant.SeoKey.DESCRIPTION, "每日签到");
    }

    @Override
    protected void fetchData() {
        modelAndView.addObject(memberService.dailyPointsMemberList());
        modelAndView.addObject(DAILY_POINTS, Boolean.FALSE);
        try {
            modelAndView.addObject(memberAggregateService.integralRecord(IntegralEnum.DAILY_POINTS.getRuleId(), query.getPageNum(), query.getPageSize()));
        } catch (Exception ignored) {
            modelAndView.addObject(new ListIntegralDto());
        }
    }
}