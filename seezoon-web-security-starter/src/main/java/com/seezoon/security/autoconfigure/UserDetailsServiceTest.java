package com.seezoon.security.autoconfigure;

import com.seezoon.security.SeezoonUserDetails;
import com.seezoon.security.UserDetailsServiceImpl;
import javax.servlet.http.HttpServletRequest;

public class UserDetailsServiceTest extends UserDetailsServiceImpl {

    @Override
    public SeezoonUserDetails<String> getSeezoonUserDetails(HttpServletRequest request, String loginType,
            String userName, String password) {
        return new SeezoonUserDetails<String>("String", userName, password);
    }
}
