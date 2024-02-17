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

package com.upupor.service.business.message;

import com.upupor.framework.utils.SpringContextUtils;
import com.upupor.service.business.message.model.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 消息发送
 *
 * @author Yang Runkang (cruise)
 * @date 2022年11月10日
 * @email: yangrunkang53@gmail.com
 */
@Slf4j
public class MessageSend {
    public static void send(MessageModel messageModel) {
        if (StringUtils.isEmpty(messageModel.getMessageId())) {
            log.warn("msgId为空,无法发送消息");
            return;
        }

        List<AbstractMessage> abstractMessageList = SpringContextUtils.getBeans(AbstractMessage.class);
        if (CollectionUtils.isEmpty(abstractMessageList)) {
            log.warn("无具体实现类,无法发送消息");
            return;
        }

        for (AbstractMessage abstractMessage : abstractMessageList) {
            if (!abstractMessage.isSend(messageModel)) {
                continue;
            }
            //UNKNOWN 这里有两个实现，具体调用的是哪个????     实测先进了Email.class
            abstractMessage.send(messageModel);
        }
    }
}
