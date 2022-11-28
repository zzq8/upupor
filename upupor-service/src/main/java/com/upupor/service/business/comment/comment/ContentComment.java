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

package com.upupor.service.business.comment.comment;

import com.upupor.data.dao.entity.Content;
import com.upupor.data.dao.entity.Member;
import com.upupor.data.dao.entity.enhance.ContentEnhance;
import com.upupor.data.dao.entity.enhance.Converter;
import com.upupor.data.types.ContentType;
import com.upupor.data.types.MessageType;
import com.upupor.framework.common.IntegralEnum;
import com.upupor.framework.utils.CcDateUtil;
import com.upupor.service.base.ContentService;
import com.upupor.service.base.MemberIntegralService;
import com.upupor.service.base.MemberService;
import com.upupor.service.base.MessageService;
import com.upupor.service.business.comment.AbstractComment;
import com.upupor.service.business.message.MessageSend;
import com.upupor.service.business.message.model.MessageModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

import static com.upupor.framework.CcConstant.MsgTemplate.*;

/**
 * @author Yang Runkang (cruise)
 * @date 2021年12月16日 21:19
 * @email: yangrunkang53@gmail.com
 */
@Component
public class ContentComment extends AbstractComment<ContentEnhance> {

    @Resource
    private ContentService contentService;

    @Resource
    private MessageService messageService;

    @Resource
    private MemberIntegralService memberIntegralService;

    public ContentComment(MemberService memberService) {
        super(memberService);
    }

    @Override
    public void comment(String targetId, String commenterUserId, String commentId) {
        String msgId = getMsgId();
        // 文章信息
        ContentEnhance contentEnhance = getTarget(targetId);
        Content target = contentEnhance.getContent();
        String contentId = target.getContentId();
        String contentTitle = target.getTitle();
        Member contentAuthor = getMemberInfo(target.getUserId()).getMember();

        // 获取评论者信息
        Member commenter = getMemberInfo(commenterUserId).getMember();
        String commenterUserName = commenter.getUserName();

        // 如果文章作者自己评论自己就不用发邮件了
        if (commenter.getUserId().equals(target.getUserId())) {
            return;
        }

        // 站内信通知作者
        String innerMsgText = "您的文章《" + String.format(CONTENT_INNER_MSG, contentId, msgId, contentTitle) + "》有新的评论,来自"
                + String.format(PROFILE_INNER_MSG, commenterUserId, msgId, commenterUserName)
                + ",快去" + String.format(CONTENT_INNER_MSG, contentId, msgId, "查看") + "吧";
        // 邮件内容
        String emailTitle = "您的文章有了新评论,快去看看吧";
        String emailContent = "点击《" + String.format(CONTENT_EMAIL, contentId, msgId, contentTitle) + "》查看评论,评论来自 "
                + String.format(PROFILE_EMAIL, commenterUserId, msgId, commenterUserName);

        // 赠送积分描述
        String integralText = "您评论了 《" + String.format(CONTENT_INTEGRAL, contentId, msgId, contentTitle) + "》文章,赠送 " +
                IntegralEnum.CREATE_COMMENT.getIntegral() + " 积分;快去写文章吧,您收到的评论越多,就会获得更多的积分~";


        MessageSend.send(MessageModel.builder()
                .toUserId(contentAuthor.getUserId())
                .emailModel(MessageModel.EmailModel.builder()
                        .title(emailTitle)
                        .content(emailContent)
                        .build())
                .innerModel(MessageModel.InnerModel.builder()
                        .message(innerMsgText).build())
                .messageType(MessageType.SYSTEM)
                .messageId(msgId)
                .build());

        // 送积分
        memberIntegralService.addIntegral(IntegralEnum.CREATE_COMMENT, integralText, commenterUserId, commentId);
    }

    @Override
    protected ContentEnhance getTarget(String targetId) {
        return contentService.getNormalContent(targetId);
    }

    @Override
    public Boolean confirmSource(ContentType contentType, String targetId) {
        return Objects.nonNull(getTarget(targetId)) && ContentType.contentSource().contains(contentType);
    }

    @Override
    public void updateTargetCommentCreatorInfo(String targetId, String commenterUserId) {
        ContentEnhance contentEnhance = getTarget(targetId);
        Content content = contentEnhance.getContent();
        content.setLatestCommentTime(CcDateUtil.getCurrentTime());
        content.setLatestCommentUserId(commenterUserId);
        contentService.updateContent(Converter.contentEnhance(content));
    }
}
