package net.mattcarpenter.performancereview.utils;

import net.mattcarpenter.performancereview.constants.Constants;

import javax.servlet.http.Cookie;

public class CookieUtils {

    private static int TOKEN_COOKIE_MAX_AGE_SECONDS = 24 * 60 * 60;
    private static final String TOKEN_COOKIE_PATH = "/";

    public static Cookie createTokenCookie(String jwt) {
        Cookie cookie = new Cookie(Constants.TOKEN_COOKIE_NAME, jwt);
        cookie.setMaxAge(TOKEN_COOKIE_MAX_AGE_SECONDS); // todo - calculate based on jwt expiration
        //cookie.setSecure(true); // todo - enable once tls is implemented
        cookie.setHttpOnly(true);
        cookie.setPath(TOKEN_COOKIE_PATH);
        return cookie;
    }
}
