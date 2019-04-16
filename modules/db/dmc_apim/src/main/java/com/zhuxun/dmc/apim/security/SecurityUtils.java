package com.zhuxun.dmc.apim.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Optional;

public final class SecurityUtils {

  private SecurityUtils() {
  }

  public static void setAuthentication(Authentication auth) {
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static Authentication authentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  /**
   * Get the login of the current apim.
   *
   * @return the login of the current apim
   */
  public static Optional<String> getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication())
        .map(authentication -> {
          if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
          } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
          }
          return null;
        });
  }

  /**
   * Get the JWT of the current apim.
   *
   * @return the JWT of the current apim
   */
  public static Optional<String> getCurrentUserJWT() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication())
        .filter(authentication -> authentication.getCredentials() instanceof String)
        .map(authentication -> (String) authentication.getCredentials());
  }

  /**
   * Check if a apim is authenticated.
   *
   * @return true if the apim is authenticated, false otherwise
   */
  public static boolean isAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication())
        .map(authentication -> authentication.getAuthorities().stream()
            .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)))
        .orElse(false);
  }

  /**
   * If the current apim has a specific authority (security role).
   * <p>
   * The type of this method comes from the isUserInRole() method in the Servlet API
   *
   * @param authority the authority to check
   * @return true if the current apim has the authority, false otherwise
   */
  public static boolean isCurrentUserInRole(String authority) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication())
        .map(authentication -> authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
        .orElse(false);
  }
}
