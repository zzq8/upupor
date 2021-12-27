package com.upupor.service.utils;

import com.upupor.service.dao.entity.Member;
import org.springframework.util.DigestUtils;

/**
 * 密码工具类
 *
 * @author: YangRunkang(cruise)
 * @created: 2019/12/29 02:35
 */
public class PasswordUtils {


    /**
     * 加密用户输入的密码
     *
     * @param inputPassword
     * @param member
     * @return
     */
    public static String encryptMemberPassword(String inputPassword, Member member) {
        return getMd5(inputPassword, member.getUserId(), member.getCreateTime());
    }

    public static String encryptMemberEmergencyCode(String emergencyCode) {
        return getMd5(emergencyCode);
    }


    /**
     * 以用户Id 和 用户创建时间 维度 创建 MD5
     *
     * @param inputPassword
     * @param userId
     * @param userCreateTime
     * @return
     */
    private static String getMd5(String inputPassword, String userId, Long userCreateTime) {
        String slat = "upupor-slat" + userId + userCreateTime;
        String base = inputPassword + slat;
        // 11+32位长度
        return "#UPUPOR#" + DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 以 EmergencyCode 创建 MD5
     *
     * @param emergencyCode
     * @return
     */
    private static String getMd5(String emergencyCode) {
        String slat = "EmergencyCode-slat";
        String base = emergencyCode + slat;
        // 11+32位长度
        return "#UPUPOR#" + DigestUtils.md5DigestAsHex(base.getBytes());
    }


}
