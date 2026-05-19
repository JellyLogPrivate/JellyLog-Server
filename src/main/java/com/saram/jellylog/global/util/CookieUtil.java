package com.saram.jellylog.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Base64;

public class CookieUtil {

    // 쿠키 추가
    public static void addCookie(HttpServletResponse response,
                                 String name,
                                 String value,
                                 int maxAge) {

        Cookie cookie = new Cookie(name, value);

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    // 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

    }

    // 정보 직렬화 -> 저장
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializeUtill.serialize(object));
    }

    // 정보 역직렬화 -> 읽기
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializeUtill.deserialize(
                        Base64.getUrlDecoder()
                                .decode(cookie.getValue())));
    }

}
