package com.thucjava.shopapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.service.JwtService;
import com.thucjava.shopapp.service.MyUserDetailsService;
import com.thucjava.shopapp.utils.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    ApplicationContext context;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper ;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String access_token=null;
        String email=null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            access_token = authHeader.substring(7);
            // kiem tra access_token co trong blacklist hay khong
            if(redisTemplate.hasKey("BLACKLIST_TOKEN:"+access_token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String jsonError = "{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Access token expired\", \"code\": \"TOKEN_EXPIRED\"}";
                response.getWriter().write(jsonError);
                return;
            }

            // kiem tra xem access_token het han chua, neu chua thì lấy ra email
            String emailCached =(String) redisTemplate.opsForValue().get("TOKEN_CACHE:"+access_token);
            try{

                if(emailCached!=null){
                    email = emailCached;
                }
                else{
                    email=jwtService.extractUserName(access_token,TokenType.ACCESS_TOKEN);
                }
            }catch (Exception ex){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String jsonError = "{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Access token expired\", \"code\": \"TOKEN_EXPIRED\"}";
                response.getWriter().write(jsonError);
                // nếu hết hạn gữi lại trong blacklist 10 phút
                redisTemplate.opsForValue().set("BLACKLIST_TOKEN:"+access_token,"expired",10, TimeUnit.MINUTES);
                return;
            }
        }

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(email);
                if(jwtService.validateToken(access_token,userDetails, TokenType.ACCESS_TOKEN)){
                    UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println("Authorities: " + userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // Lưu access_token vào redis neu redis chua co access_token
                    if(!redisTemplate.hasKey("TOKEN_CACHE:"+access_token)) {
                        redisTemplate.opsForValue().set("TOKEN_CACHE:"+access_token,email,
                                jwtService.extractExpiration(access_token,TokenType.ACCESS_TOKEN).getTime(),
                                TimeUnit.MILLISECONDS);

                    }

                }

            }

        filterChain.doFilter(request, response);
    }
}
