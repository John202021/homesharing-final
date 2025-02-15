package gr.hua.dit.HomeSharing.util;

import gr.hua.dit.HomeSharing.entities.CustomUserDetails;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    public static CustomUserDetails getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        throw new IllegalStateException("No logged-in user found or incorrect principal type.");
    }

    public static int getLoggedInUserId() {
        return getLoggedInUser().getId();
    }


    public static String getLoggedInUserEmail() {
        return getLoggedInUser().getUsername();
    }

    public static List<String> getLoggedInUserRoles() {
        return getLoggedInUser().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
