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

package com.upupor.web.page.view_data.content;

import com.upupor.data.dto.page.ad.AbstractAd;
import com.upupor.data.dto.page.common.ListContentDto;
import com.upupor.data.types.SearchContentType;
import com.upupor.framework.CcConstant;
import com.upupor.service.base.ContentService;
import com.upupor.web.page.view_data.AbstractView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.upupor.data.types.SearchContentType.SEARCH_CONTENT_URL.REPRINT_CONTENT_VIEW_URL;
import static com.upupor.framework.CcConstant.ContentView.CONTENT_TYPE;

/**
 * 转载
 *
 * @author Yang Runkang (cruise)
 * @date 2022年04月07日 01:23
 * @email: yangrunkang53@gmail.com
 */
@Component
@RequiredArgsConstructor
public class ReprintContentView extends AbstractView {
    public static final String URL = REPRINT_CONTENT_VIEW_URL;
    private final ContentService contentService;

    @Override
    public String viewName() {
        return CONTENT_TYPE;
    }

    @Override
    protected String pageUrl() {
        return URL;
    }

    @Override
    protected void seoInfo() {
        modelAndView.addObject(CcConstant.SeoKey.TITLE, "转载内容");
        modelAndView.addObject(CcConstant.SeoKey.DESCRIPTION, "转载内容");
        modelAndView.addObject(CcConstant.SeoKey.BUSINESS_TITLE, "转载内容");
    }

    @Override
    protected void fetchData() {
        Integer pageNum = query.getPageNum();
        Integer pageSize = query.getPageSize();
        ListContentDto listContentDto = contentService.typeContentList(SearchContentType.getByUrl(URL), pageNum, pageSize);
        AbstractAd.ad(listContentDto.getContentEnhanceList());
        modelAndView.addObject(listContentDto);
    }

}