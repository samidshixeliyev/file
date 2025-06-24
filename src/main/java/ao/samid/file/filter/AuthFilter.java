package ao.samid.file.filter;


import ao.samid.file.client.AuthClient;
import ao.samid.file.handler.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final AuthClient authClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String path = request.getRequestURI();
        if(path.contains("swagger")  || path.contains("api-docs")){
            filterChain.doFilter(request, response);
            return;
        }
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String bearer = authorization.substring(7);
            try {
                authClient.checkAccess(bearer);
            } catch (CustomException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json"); // Set the Content-Type header
                response.setCharacterEncoding("UTF-8"); // Optional but recommended for proper character handling
                response.getWriter().write("{\n" +
                        "  \"message\": \"" + e.getMessage() + "\",\n" +
                        "  \"code\": " + e.getCode() + ",\n" +
                        "  \"url\":\"" + request.getRequestURI() + "\"\n" +
                        "}");
                return;
            }
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);}
    }
}