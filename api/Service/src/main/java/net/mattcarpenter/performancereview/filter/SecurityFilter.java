package net.mattcarpenter.performancereview.filter;

import net.mattcarpenter.performancereview.constants.Constants;
import net.mattcarpenter.performancereview.model.Token;
import net.mattcarpenter.performancereview.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private AuthService authService;

    public SecurityFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String jwt = null;

        // accept access token from either a header or a cookie
        String authorizationHeaderValue = request.getHeader(Constants.AUTHORIZATION_HEADER_NAME);
        if (!StringUtils.isEmpty(authorizationHeaderValue)) {
            if (authorizationHeaderValue.toUpperCase().indexOf("BEARER") == 0) {
                jwt = authorizationHeaderValue.substring(7);
            } else {
                jwt = authorizationHeaderValue;
            }
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Cookie tokenCookie = Arrays.stream(cookies)
                        .filter(c -> Constants.TOKEN_COOKIE_NAME.equals(c.getName()))
                        .findFirst()
                        .orElse(null);
                if (tokenCookie != null) {
                    jwt = tokenCookie.getValue();
                }
            }
        }

        if (!StringUtils.isEmpty(jwt)) {
            // validate token and load into the security context
            Token token = authService.validateJwt(jwt.trim());
            UsernamePasswordAuthenticationToken springSeurityToken =
                    new UsernamePasswordAuthenticationToken(token, null);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(springSeurityToken);
            SecurityContextHolder.setContext(context);
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(request, response);
    }
}
