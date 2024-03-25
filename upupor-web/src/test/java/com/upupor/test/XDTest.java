package com.upupor.test;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.upupor.web.UpuporWebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(classes = UpuporWebApplication.class)
public class XDTest {
//    @Autowired
//    TrueSend send;
    @Test
    void testQQMail() {
//        send.qqMail();
//        TrueSend bean = SpringContextUtils.getBean(TrueSend.class);
//        bean.qqMail();
    }
    
    @Test
    public void testAvatar() throws IOException {
//        AvatarHelper.create(123123123);
        char chr = 127;

    }

    /**
     * 最简单的上传凭证只需要AccessKey，SecretKey和Bucket就可以。
     */
    @Test
    public void testQiniuUpload(){
            //构造一个带指定 Region 对象的配置类
            Configuration cfg = new Configuration(Region.region2());
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            //...生成上传凭证，然后准备上传
            String accessKey = "_gHWJBizTe98WqI9wxMXMHjNQmzcVOZYssOLwq1I";
            String secretKey = "f2iesdbbxk_ul6sEtOQr6aGnkPg_rF66kQn1-6R2";
            String bucket = "1024zzq";
            //如果是Windows情况下，格式是 D:\\qiniu\\test.png
            String localFilePath = "C:\\Users\\Fighting\\Desktop\\1.png";
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = "blog/blog_"+ DateUtil.today() +".png";
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(localFilePath, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
    }
}
