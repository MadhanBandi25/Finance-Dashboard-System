package com.finance.dashboard.security;

 import com.finance.dashboard.exception.ErrorResponse;
import com.finance.dashboard.enums.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 import tools.jackson.databind.ObjectMapper;

 import java.io.IOException;
import java.util.List;

@Component
public class RoleAuthFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String roleHeader = request.getHeader("X-Role");

        if (roleHeader == null || roleHeader.isBlank()) {
            sendError(response, "X-Role header is required. Values: VIEWER, ANALYST, ADMIN");
            return;
        }

        Role role;
        try {
            role = Role.valueOf(roleHeader.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            sendError(response, "Invalid role: '" + roleHeader + "'. Must be VIEWER, ANALYST, or ADMIN");
            return;
        }

        var authority = new SimpleGrantedAuthority("ROLE_" + role.name());
        var authentication = new UsernamePasswordAuthenticationToken(
                role.name(), null, List.of(authority));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        new ErrorResponse(403, "Forbidden", message)));
    }
}