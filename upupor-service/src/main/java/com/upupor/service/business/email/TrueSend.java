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

package com.upupor.service.business.email;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.upupor.framework.BusinessException;
import com.upupor.framework.ErrorCode;
import com.upupor.framework.config.Email;
import com.upupor.framework.config.UpuporConfig;
import com.upupor.framework.utils.SpringContextUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yang Runkang (cruise)
 * @date 2022年11月24日
 * @email: yangrunkang53@gmail.com
 */
@Component
public class TrueSend {

    private static Map<String, String> errMsgMap = new HashMap<>();

    static {
        errMsgMap.put("InvalidReceiverName.Malformed", "收件人格式不正确，必须有@符号，域名组成为数字，字母，下划线，减号和点，账号组成为数字，字母，下划线，减号和点");
        errMsgMap.put("InvalidMailAddress.NotFound", "不存在，请检查批定的发信地址");
        errMsgMap.put("InvalidTemplate.NotFound", "指定的模板不存在");
        errMsgMap.put("InvalidReceiver.NotFound", "The specified receiver does not exist.");
        errMsgMap.put("InvalidToAddress", "收件人格式不正确，必须有@符号，域名组成为数字，字母，下划线，减号和点，账号组成为数字，字母，下划线，减号和点");
        errMsgMap.put("InvalidToAddress.Spam", "无效地址，请检查地址有效性");
        errMsgMap.put("InvalidBody", "textBody或textBody格式错误,请重新填写内容");
        errMsgMap.put("InvalidSendMail.Spam", "发信被拒绝，请检查用户状态，是否是频率超限，额度等问题");
        errMsgMap.put("InvalidMailAddressSendType.Malformed", "发送类型不正确，请去控制台检查类型，设置相应的值");
        errMsgMap.put("InvalidMailAddressStatus.Malformed", "发信地址状态不对，请检查是否可用，是否是被冻结状态");
        errMsgMap.put("InvalidMailAddressDomain.Malformed", "域名格式不正确，请使用数字，字母，下划线，减号和点");
        errMsgMap.put("InvalidFromAlias.Malformed", "主题错误，主题不能超过100个字符");
        errMsgMap.put("InvalidReplyAddressAlias.Malformed", "回信地址别名格式不正确，长度不超过15个符");
        errMsgMap.put("InvalidReplyAddress.Malformed", "回信地址格式不正确，必须有@符号，域名组成为数字，字母，下划线，减号和点，账号组成为数字，字母，下划线，减号和点");
    }


    /**
     * XD: IAcsClient是阿里云的SDK中的类，用于调用阿里云的各项服务，包括邮件服务。但是IAcsClient并不适用于直接连接到QQ邮箱发送邮件。
     *
     * @param toAddress 目标地址 可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
     * @param subject   邮件主题
     * @param tagName   控制台创建的标签
     * @param htmlBody  邮件正文(文本邮件的大小限制为3M)
     */
    public Boolean trueSend(String toAddress, String subject, String tagName, String htmlBody) {
        Email email = SpringContextUtils.getBean(UpuporConfig.class).getEmail();
        String accessKeyId = email.getAccessKeyId();
        String accessKeySecret = email.getAccessKeySecret();
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        //try {
        //DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com", "ap-southeast-1", "Dm",  "dm.ap-southeast-1.aliyuncs.com");
        //} catch (ClientException e) {
        //e.printStackTrace();
        //}
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            //request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
//            request.setAccountName("控制台创建的发信地址");
            com.upupor.framework.config.Email emailConfig = SpringContextUtils.getBean(UpuporConfig.class).getEmail();
            request.setAccountName(emailConfig.getSenderAccountName());
            // 发信人昵称
            request.setFromAlias(emailConfig.getSenderNickName());
            request.setAddressType(1);
            request.setTagName(tagName);
            request.setReplyToAddress(true);
            request.setToAddress(toAddress);
            //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            //request.setToAddress("邮箱1,邮箱2");
            request.setSubject(subject);
            //如果采用byte[].toString的方式的话请确保最终转换成utf-8的格式再放入htmlbody和textbody，若编码不一致则会被当成垃圾邮件。
            //注意：文本邮件的大小限制为3M，过大的文本会导致连接超时或413错误
            request.setHtmlBody(htmlBody);

            //SDK 采用的是http协议的发信方式, 默认是GET方法，有一定的长度限制。
            //若textBody、htmlBody或content的大小不确定，建议采用POST方式提交，避免出现uri is not valid异常
            request.setMethod(MethodType.GET);

            //开启需要备案，0关闭，1开启
            //request.setClickTrace("0");

            //如果调用成功，正常返回httpResponse；如果调用失败则抛出异常，需要在异常中捕获错误异常码；错误异常码请参考对应的API文档;
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
            return Objects.nonNull(httpResponse);
        } catch (ServerException e) {
            throw new BusinessException(ErrorCode.SEND_EMAIL_ERROR, handSendEmailErrCode(e.getErrCode()));
        } catch (ClientException e) {
            throw new BusinessException(ErrorCode.SEND_EMAIL_ERROR, handSendEmailErrCode(e.getErrCode()));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SEND_EMAIL_ERROR, e.getMessage());
        }
    }




    // 注入 mail 实体，所有邮件相关的操作，都在这个类上
    @Resource
    private JavaMailSenderImpl mailSender;

    public void qqMail() throws MessagingException, javax.mail.MessagingException {
        // 负责邮件消息类
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 参数1：消息类
        // 参数2：是否支持发送附件
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);
        // 邮件主题
        mimeMessageHelper.setSubject("SpringBoot 实现超文本邮件发送~");
        // 参数1：邮件内容
        // 参数2：是否支持html
        mimeMessageHelper.setText("<h1 style='color:blue>'一个文本文件已发送到你的QQ邮箱上~</h1>",true);
        // 附件
        // 参数1：发送到邮件的文件名
        // 参数2：本地文件的绝对路径,不知道在哪的，右键文件点属性有个位置，复制下来，加上文件名
//        mimeMessageHelper.addAttachment("SpringBoot01.jpg", new File("C:\\Users\\xzh\\Desktop\\SpringBoot01.jpg"));
        // 发送人
        mimeMessageHelper.setTo("547061946@qq.com");
        // 接收人
        mimeMessageHelper.setFrom("zzq2333@qq.com");
        // 开始发送
        mailSender.send(mimeMessage);
    }


    private static String handSendEmailErrCode(String errorCode) {
        for (String code : errMsgMap.keySet()) {
            if (code.equals(errorCode)) {
                throw new BusinessException(ErrorCode.SEND_EMAIL_ERROR, errMsgMap.get(code));
            }
        }
        throw new BusinessException(ErrorCode.SEND_EMAIL_ERROR, "未知错误");
    }

}
