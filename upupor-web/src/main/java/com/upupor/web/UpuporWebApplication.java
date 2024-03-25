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

package com.upupor.web;

import com.upupor.service.business.email.TrueSend;
import com.upupor.service.business.member.MemberOperateService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Upupor启动类
 *
 * @author runkangyang
 */
@Slf4j
@MapperScan("com.upupor.data.dao.mapper")
@ComponentScan(basePackages = {
        "com.upupor.web",
        "com.upupor.service",
        "com.upupor.framework",
        "com.upupor.task",
        "com.upupor.lucene",
        "com.upupor.data",
})
@SpringBootApplication
//XD 注解将会话的最大非活动间隔时间设置为 86400 秒（即 24 小时）。这意味着如果会话在 24 小时内没有任何活动，它将被认为是非活动的，并可能被清除。
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)  //XD 会话过期时间重置：每当用户进行会话操作时（例如访问页面或发送请求），Spring Session 会自动更新 Redis 中存储的会话的过期时间。这样，只要用户保持活动状态，会话就会自动续期，不会过期。
@EnableAsync
public class UpuporWebApplication implements CommandLineRunner {
    public static final String STATIC_SOURCE_VERSION;

    static {
        System.setProperty("druid.mysql.usePingMethod", "false");
        STATIC_SOURCE_VERSION = LocalDateTime.now(ZoneId.of("Asia/Shanghai")).toString();
    }

    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(UpuporWebApplication.class, args);
        MemberOperateService bean = run.getBean(MemberOperateService.class);
    }


    @Autowired
    TrueSend send;
    @Override
    public void run(String... args) throws Exception {
//        send.qqMail();       //到 junit 测试
    }
}
