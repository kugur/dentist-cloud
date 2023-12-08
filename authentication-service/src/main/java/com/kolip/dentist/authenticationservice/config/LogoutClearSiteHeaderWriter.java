package com.kolip.dentist.authenticationservice.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.header.HeaderWriter;

/**
 * Created by ugur.kolip on 30/11/2023.
 *
 * To use clear cookies from browser. Clear-site-data can not be used on not  secure connection
 * because of this added header to clear manually.
 */
public class LogoutClearSiteHeaderWriter implements HeaderWriter {
    private static final String CLEAR_SITE_DATA_HEADER = "Clear-Site-Data";
    private String[] clearingDirectives = new String[]{"cache", "cookies", "storage", "*"};

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < clearingDirectives.length - 1; i++) {
            sb.append(clearingDirectives[i]).append(", ");
        }
        sb.append(clearingDirectives[clearingDirectives.length - 1]);

        response.setHeader(CLEAR_SITE_DATA_HEADER, sb.toString());

        Cookie tokenCookie = new Cookie("Authorization", "");
        tokenCookie.setMaxAge(0);
        response.addCookie(tokenCookie);
    }
}
