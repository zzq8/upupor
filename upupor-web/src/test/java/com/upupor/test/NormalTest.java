package com.upupor.test;

import com.upupor.framework.utils.DeflaterUtils;
import org.junit.jupiter.api.Test;

import java.util.Base64;

public class NormalTest {
    @Test
    public void test01() {
        String str = "中文";
        String s = Base64.getEncoder().encodeToString(str.getBytes());
        System.out.println("s = " + s);
    }
    
    
    @Test
    public void test02(){
        String s = DeflaterUtils.unzipString("UPUPOR_LSDH&%^UOIHJKNHBGFR%E^&T*YUIHJBKVH..akti2GC^%E&*Y21687615_ZIP(eNp7smPXk11tT9cue7JzwdM9DU/7Jz5bu+Rp76yn6xY937z7+e75CiDwZG/b8ykbn+zqezan92nXwqczVzzdv/ppx4Znc+a/39MDUf5sVtOz6due7Oh9vm7h8wltAMBePR4=)");
        System.out.println("s = " + s);
    }
}

