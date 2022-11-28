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

package com.upupor.service.business.lucene.flush;

import com.upupor.data.dao.entity.Member;
import com.upupor.lucene.AbstractFlush;
import com.upupor.lucene.enums.LuceneDataType;
import com.upupor.service.base.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Yang Runkang (cruise)
 * @date 2022年04月04日 14:15
 * @email: yangrunkang53@gmail.com
 */
@Component
@RequiredArgsConstructor
public class FlushMember extends AbstractFlush<Member> {

    private final MemberService memberService;
    private Member member;

    @Override
    protected void add() {
        getUpuporLuceneService().addDocument(member.getUserName(), member.getUserId(), runDataType());
    }

    @Override
    protected void delete() {
        getUpuporLuceneService().deleteDocumentByTargetId(member.getUserId());
    }

    @Override
    protected void initTargetObject() {
        this.member = memberService.memberInfo(event.getTargetId()).getMember();
    }


    @Override
    public LuceneDataType runDataType() {
        return LuceneDataType.MEMBER;
    }
}