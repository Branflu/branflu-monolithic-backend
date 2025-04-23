//package com.example.branflu.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JWTAuthenticationFilter extends OncePerRequestFilter {
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Autowired
//    private JWTTokenHelper jwtTokenHelper;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String requestToken = request.getHeader("Authorization");
//        System.out.println(requestToken);
//        String username=null;
//        String token=null;
//
//        if(requestToken!=null && requestToken.startsWith("Bearer")){
//            token = requestToken.substring(7);
//            username=this.jwtTokenHelper.getUsernameFromToken(token);
//        }else {
//            System.out.println("JWT token does not start with bearer");
//        }
//        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
//            UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
//            if(this.jwtTokenHelper.validateToken(token,userDetails)){
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//            else {
//                System.out.println("Inavlid JWT token");
//            }
//        }
//        else{
//            System.out.println("Username is null or context is not null");
//        }
//        filterChain.doFilter(request,response);
//    }
//}
