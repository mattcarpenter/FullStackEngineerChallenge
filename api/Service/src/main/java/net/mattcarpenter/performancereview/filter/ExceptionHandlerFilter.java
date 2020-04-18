package net.mattcarpenter.performancereview.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mattcarpenter.performancereview.error.Error;
import net.mattcarpenter.performancereview.error.ErrorCode;
import net.mattcarpenter.performancereview.exception.BadRequestException;
import net.mattcarpenter.performancereview.exception.NotAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (NotAuthorizedException nae) {
            Error error = new Error(nae.getErrorCode());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE_APPLICATION_JSON);
            response.getWriter().write(toJson(error));
        } catch (BadRequestException bre) {
            Error error = new Error(bre.getErrorCode());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(CONTENT_TYPE_APPLICATION_JSON);
            response.getWriter().write(toJson(error));
        } catch (Exception ex) {
            Error error = new Error(ErrorCode.INTERNAL_SERVICE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(CONTENT_TYPE_APPLICATION_JSON);
            response.getWriter().write(toJson(error));
        }
    }

    private String toJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
