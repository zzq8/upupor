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

package com.upupor.limiter;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.upupor.limiter.LimiterConstant.INTERVAL_30S;

/**
 * 默认限制器
 *
 * @author Yang Runkang (cruise)
 * @createTime 2022-04-22 00:46
 * @email: yangrunkang53@gmail.com
 */
@Builder
@Getter
public class DefaultLimiterConfig {

    /**
     * 限制类型
     */
    private LimitType limitType;

    /**
     * 请求频率
     */
    private Integer frequency;

    /**
     * 在xx秒内
     *
     * @note: 在 withinSeconds 秒内只允许请求 frequency 次
     */
    private Integer withinSeconds;

    /**
     * 默认的系统资源配置
     *
     * @return
     */
    public static List<DefaultLimiterConfig> defaultLimiterConfigList() {
        List<DefaultLimiterConfig> defaultLimiterConfigList = new ArrayList<>();

        defaultLimiterConfigList.add(
                DefaultLimiterConfig.builder()
                        .limitType(LimitType.CREATE_CONTENT)
                        .frequency(2)
                        .withinSeconds(INTERVAL_30S)
                        .build()
        );

        defaultLimiterConfigList.add(
                DefaultLimiterConfig.builder()
                        .limitType(LimitType.CREATE_RADIO)
                        .frequency(2)
                        .withinSeconds(INTERVAL_30S)
                        .build()
        );

        defaultLimiterConfigList.add(
                DefaultLimiterConfig.builder()
                        .limitType(LimitType.CREATE_MESSAGE_ON_BOARD)
                        .frequency(10)
                        .withinSeconds(INTERVAL_30S)
                        .build()
        );

        defaultLimiterConfigList.add(
                DefaultLimiterConfig.builder()
                        .limitType(LimitType.CREATE_COMMENT)
                        .frequency(2)
                        .withinSeconds(INTERVAL_30S)
                        .build()
        );

        return defaultLimiterConfigList;
    }

}
