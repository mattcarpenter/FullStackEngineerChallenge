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
import org.springframework.web.util.NestedServletException;

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
        } catch (NestedServletException nse) {
            // exceptions thrown from within the application will be wrapped up in a NestedServletException. Drill into
            // the exception to determine which type of error to return to the caller.
            if (nse.getCause() instanceof NotAuthorizedException) {
                respondWithNotAuthorized((NotAuthorizedException)(nse.getCause()), response);
            } else if (nse.getCause() instanceof BadRequestException) {
                respondWithBadRequest((BadRequestException)(nse.getCause()), response);
            } else {
                respondWithServiceError(response);
            }
        } catch (NotAuthorizedException nae) {
            respondWithNotAuthorized(nae, response);
        } catch (BadRequestException bre) {
            respondWithBadRequest(bre, response);
        } catch (Exception ex) {
            respondWithServiceError(response);
        }
    }

    private void respondWithNotAuthorized(NotAuthorizedException nae, HttpServletResponse response) throws IOException {
        Error error = new Error(nae.getErrorCode());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(CONTENT_TYPE_APPLICATION_JSON);
        response.getWriter().write(toJson(error));
    }

    private void respondWithBadRequest(BadRequestException nae, HttpServletResponse response) throws IOException {
        Error error = new Error(nae.getErrorCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(CONTENT_TYPE_APPLICATION_JSON);
        response.getWriter().write(toJson(error));
    }

    private void respondWithServiceError(HttpServletResponse response) throws IOException {
        Error error = new Error(ErrorCode.INTERNAL_SERVICE_ERROR);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(CONTENT_TYPE_APPLICATION_JSON);
        response.getWriter().write(toJson(error));
    }

    private String toJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
