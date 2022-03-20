package com.seezoon.security;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {


    public static final Integer SUPER_ADMIN_USER_ID = 1;
    public static final Integer ANONYMOUS_USER_ID = -1;

    public static User getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 匿名也是Authenticated = true
        if (null != authentication && authentication.isAuthenticated()) {
            // 如果是匿名，返回的是个字符串
            Object principal = authentication.getPrincipal();
            if (principal instanceof SeezoonUserDetails) {
                SeezoonUserDetails<User> seezoonUserDetails = (SeezoonUserDetails) principal;
                return seezoonUserDetails.getUserInfo();
            }
        }
        return null;
    }


    public static boolean isSuperAdmin(Serializable userId) {
        if (Objects.equals(userId, SUPER_ADMIN_USER_ID)) {
            return true;
        }
        return false;
    }

    public static boolean isSuperAdmin() {
        final User userInfo = getUserInfo();
        if (null != userInfo && Objects.equals(userInfo.getUserId(), SUPER_ADMIN_USER_ID)) {
            return true;
        }
        return false;
    }
}