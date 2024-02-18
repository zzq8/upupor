package com.upupor.test;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.upupor.service.business.email.TrueSend;
import com.upupor.web.UpuporWebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UpuporWebApplication.class)
public class XDTest {
    @Autowired
    TrueSend send;
    @Test
    void testQQMail() throws MessagingException, javax.mail.MessagingException {
//        send.qqMail();
//        TrueSend bean = SpringContextUtils.getBean(TrueSend.class);
//        bean.qqMail();
    }
}
