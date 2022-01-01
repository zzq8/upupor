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

package com.upupor.service.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.upupor.service.common.CcConstant;
import com.upupor.service.types.ViewTargetType;
import com.upupor.service.types.ViewerDeleteStatus;
import lombok.Data;

import java.util.Date;

/**
 * 访问者
 */
@Data
public class Viewer extends BaseEntity {

    private String targetId;

    private String viewerUserId;

    private ViewTargetType targetType;

    private ViewerDeleteStatus deleteStatus;

    private Date sysUpdateTime;

    private Long createTime;

    /****************************/
    /**
     * 访问者用户头像
     */
    @TableField(exist = false)
    private String viewerUserVia = CcConstant.DEFAULT_PROFILE_PHOTO;

    /**
     * 渲染url
     */
    @TableField(exist = false)
    private String cardHtml;

    /**
     * 渲染url
     */
    @TableField(exist = false)
    private String viewerUserName;


}
