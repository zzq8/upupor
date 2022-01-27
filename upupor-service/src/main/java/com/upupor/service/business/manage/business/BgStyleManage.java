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

package com.upupor.service.business.manage.business;

import com.upupor.framework.CcConstant;
import com.upupor.service.business.aggregation.service.BusinessConfigService;
import com.upupor.service.business.manage.AbstractManageInfoGet;
import com.upupor.service.business.manage.ManageDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Yang Runkang (cruise)
 * @date 2022年01月27日 01:11
 * @email: yangrunkang53@gmail.com
 */
@Component
public class BgStyleManage  extends AbstractManageInfoGet {

    @Resource
    private BusinessConfigService businessConfigService;

    @Override
    protected void specifyDtoHandle(ManageDto manageDto) {
        getMemberIndexDto().setListCssPatternDto(businessConfigService.getAll(manageDto.getUserId()));
    }

    @Override
    public String viewName() {
        return CcConstant.UserManageView.USER_MANAGE_BG_STYLE_SETTINGS;
    }

    @Override
    public String viewDesc() {
        return "自定义网站背景样式";
    }

}