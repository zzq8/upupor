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

CREATE TABLE  IF NOT EXISTS `radio` (
    `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `radio_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '音频id',
    `user_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户Id',
    `content_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '关联文章Id',
    `radio_intro` text COLLATE utf8mb4_bin COMMENT '音频简介',
    `radio_url` text COLLATE utf8mb4_bin COMMENT '音频链接',
    `status` int(2) DEFAULT NULL COMMENT '状态 0-公开 1-隐藏 2-删除',
    `create_time` bigint(11) DEFAULT NULL COMMENT '创建时间',
    `sys_update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '系统更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='电台音频表';