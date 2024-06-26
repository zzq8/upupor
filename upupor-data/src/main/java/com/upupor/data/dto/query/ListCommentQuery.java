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

package com.upupor.data.dto.query;

import com.upupor.data.types.CommentStatus;
import com.upupor.data.types.ContentType;
import lombok.Data;

/**
 * @author Yang Runkang (cruise)
 * @date 2022年11月25日
 * @email: yangrunkang53@gmail.com
 */
@Data
public class ListCommentQuery {
    private String targetId;
    private String userId;
    private String commentId;
    private CommentStatus status;
    /**
     * 评论来源和文章类型是一致的
     */
    private ContentType commentSource;
    private Integer pageNum;
    private Integer pageSize;

    /**
     * 查询所有
     */
    private Boolean queryAll = false;

    private Boolean orderByCreateTimeBool = false;
}
