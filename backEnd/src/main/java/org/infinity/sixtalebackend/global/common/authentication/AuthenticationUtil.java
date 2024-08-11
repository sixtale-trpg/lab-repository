package org.infinity.sixtalebackend.global.common.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Slf4j
public class AuthenticationUtil {

    private AuthenticationUtil() {}

    public static Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        log.info("class", principal.getClass().getName());
        log.info(principal.toString());

        if (principal instanceof User) {
            String userId = ((User) principal).getUsername();
            return Long.valueOf(userId);
        } else if (principal instanceof String) {
            String principalStr = (String) principal;
            if ("anonymousUser".equals(principalStr)) {
                log.info("Anonymous user detected, no valid user ID.");
                return Long.valueOf(-1);
            }
            return Long.valueOf(principalStr);
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }
    }
}
