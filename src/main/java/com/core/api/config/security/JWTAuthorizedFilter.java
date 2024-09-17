//package com.core.api.config.security;
//
//import  com.core.api.domain.models.Response;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class JWTAuthorizedFilter extends OncePerRequestFilter {
//
//    private final String HEADER = "Authorization";
//    private final String PREFIX = "Bearer ";
//    private final String SECRET = "mySecretKey";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        try {
//            if (existsJWTToken(request, response)) {
//                Claims claims = validateToken(request);
//                if (claims.get("authorities") != null) {
//                    setUpSpringAuthentication(claims);
//                } else {
//                    SecurityContextHolder.clearContext();
//                }
//            } else {
//                SecurityContextHolder.clearContext();
//            }
//            chain.doFilter(request, response);
//        } catch (RuntimeException e) {
//            response.setContentType("application/json");
//            response.setHeader("statusCode", "401");
//            response.getWriter().write(convertObjectToJson(
//                    new Response<>(
//                            HttpStatus.UNAUTHORIZED,
//                            "Invalid token " + e.getMessage(),
//                            null
//                    )
//            ));
//        }
//    }
//
//    public String convertObjectToJson(Object object) throws JsonProcessingException {
//        if (object == null) {
//            return null;
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.writeValueAsString(object);
//    }
//
//    private Claims validateToken(HttpServletRequest request) {
//        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
//        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
//    }
//
//    private void setUpSpringAuthentication(Claims claims) {
//        @SuppressWarnings("unchecked")
//        List<String> authorities = (List) claims.get("authorities");
//
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
//                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//    }
//
//    private boolean existsJWTToken(HttpServletRequest request, HttpServletResponse res) {
//        String authenticationHeader = request.getHeader(HEADER);
//        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
//            return false;
//        return true;
//    }
//
//}