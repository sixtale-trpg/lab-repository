package org.infinity.sixtalebackend.global.common.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class AuthenticationUtil {

    private AuthenticationUtil() {}

    public static Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            String userId = ((User) principal).getUsername();
            return Long.valueOf(userId);
        } else if (principal instanceof String) {
            return Long.valueOf((String) principal);
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }
    }
}
