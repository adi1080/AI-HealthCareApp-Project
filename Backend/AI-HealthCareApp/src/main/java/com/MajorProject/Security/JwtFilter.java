package com.MajorProject.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.MajorProject.model.User;
import com.MajorProject.Repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    String token = null;
	    String username = null;

	    // 1. Try header
	    String authHeader = request.getHeader("Authorization");
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        token = authHeader.substring(7);
	    }

	    // 2. If not in header, check query param (for SSE)
	    if (token == null) {
	        token = request.getParameter("token");
	    }

	    // 🔽 LOGGING STARTS HERE
	    if (token != null) {
	        try {
	            username = jwtUtil.extractUsername(token);
	            System.out.println("✅ Token is valid. Extracted username: " + username);
	        } catch (Exception e) {
	            System.err.println("❌ Failed to parse JWT token: " + e.getMessage());
	        }
	    }

	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        User user = userRepository.findByUsername(username).orElse(null);
	        if (user != null && jwtUtil.validateToken(token, username)) {
	            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, null);
	            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	        }
	    }

	    filterChain.doFilter(request, response);
	}

}
