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

package com.upupor.service.business.profile.service;

import com.upupor.service.business.profile.AbstractProfile;
import com.upupor.service.dto.page.MemberIndexDto;
import com.upupor.service.types.ViewTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 个人主页聚合服务
 *
 * @author YangRunkang(cruise)
 * @date 2020/01/26 19:41
 */
@Service
@RequiredArgsConstructor
public class ProfileAggregateService {

    private final List<AbstractProfile> abstractProfileList;

    public MemberIndexDto index(String userId, Integer pageNum, Integer pageSize, ViewTargetType source) {
        for (AbstractProfile abstractProfile : abstractProfileList) {
            if (abstractProfile.viewTargetType().equals(source)) {
                return abstractProfile.getBusinessData(userId, pageNum, pageSize);
            }
        }
        return new MemberIndexDto();
    }


}