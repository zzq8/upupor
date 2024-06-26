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

package com.upupor.service.base;

import com.upupor.data.dao.entity.Comment;
import com.upupor.data.dao.entity.enhance.CommentEnhance;
import com.upupor.data.dao.entity.enhance.ContentEnhance;
import com.upupor.data.dao.entity.enhance.RadioEnhance;
import com.upupor.data.dto.page.common.ListCommentDto;
import com.upupor.data.dto.query.ListCommentQuery;
import com.upupor.service.base.impl.CommentServiceImpl;
import com.upupor.service.outer.req.AddCommentReq;

import java.util.List;

/**
 * 评论服务
 *
 * @author: YangRunkang(cruise)
 * @created: 2019/12/22 07:57
 */
public interface CommentService {
    /**
     * 添加评论
     *
     * @param addCommentReq
     * @return
     */
    Comment create(AddCommentReq addCommentReq);

    /**
     * 更新评论
     *
     * @param comment
     * @return
     */
    Boolean update(Comment comment);


    /**
     * 获取评论列表
     *
     * @return
     */
    ListCommentDto listComment(ListCommentQuery listCommentQuery);

    /**
     * 根据评论id获取评论
     *
     * @param commentId
     * @return
     */
    Comment getCommentByCommentId(String commentId);

    /**
     * 绑定评论的用户名
     *
     * @param commentList
     */
    void bindCommentUser(List<CommentEnhance> commentList, CommentServiceImpl.CommentOrReply commentOrReply);

    void bindContentComment(ContentEnhance content, Integer pageNum, Integer pageSize);

    void bindRadioComment(RadioEnhance radio, Integer pageNum, Integer pageSize);

    /**
     * 根据目标id统计评论数
     *
     * @param targetId
     */
    Integer countByTargetId(String targetId);

}