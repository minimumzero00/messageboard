//파일이 업로드되면, MD5 체크섬의 값으로 서버에 저장되게 구현
//문자열을 MD5 체크섬으로 변환하는 기능을 구현
//MD5: 암호화 또는 파일 무결성 검사 용도로 쓰임

package com.project3.messageboard.news.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NewsMD5Generator {
    private String result;

    public NewsMD5Generator(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest mdMD5 = MessageDigest.getInstance("MD5");
        mdMD5.update(input.getBytes("UTF-8"));
        byte[] md5Hash = mdMD5.digest();
        StringBuilder hexMD5hash = new StringBuilder();
        for(byte b : md5Hash) {
            String hexString = String.format("%02x", b);
            hexMD5hash.append(hexString);
        }
        result = hexMD5hash.toString();
    }

    public String toString() {
        return result;
    }
}
