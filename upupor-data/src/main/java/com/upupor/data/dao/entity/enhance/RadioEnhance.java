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

package com.upupor.data.dao.entity.enhance;

import com.upupor.data.dao.entity.Radio;
import com.upupor.data.dto.page.common.ListCommentDto;
import com.upupor.data.types.UploadStatus;
import com.upupor.framework.utils.CcDateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * @author Yang Runkang (cruise)
 * @createTime 2022-11-27 03:54
 * @email: yangrunkang53@gmail.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RadioEnhance {
    private Radio radio;


    /**
     * 电台创作者
     */
    private MemberEnhance memberEnhance;

    /**
     * 文章数据
     */
    private ContentDataEnhance contentDataEnhance;


    private List<ViewHistoryEnhance> viewHistoryEnhanceList;


    /**
     * 当前电台上传状态
     */
    private UploadStatus uploadStatus;

    /**
     * 评论数
     */
    private Integer commentTotal;


    /**
     * 评论内容
     *
     * @return
     */
    private ListCommentDto listCommentDto;


    private String createDate;

    private String latestCommentDate;

    private String latestCommentUserName;


    public String getLatestCommentDate() {
        Long latestCommentTime = radio.getLatestCommentTime();
        if (Objects.isNull(latestCommentTime)) {
            return latestCommentDate;
        }
        return CcDateUtil.timeStamp2Date(latestCommentTime);
    }


    public String getCreateDate() {
        return CcDateUtil.timeStamp2Date(radio.getCreateTime());
    }
}
