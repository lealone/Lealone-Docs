/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.examples.petstore;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lealone.http.tomcat.TomcatRouter;
import com.lealone.service.template.TemplateEngine;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class PetStore {

    public static void main(String[] args) {
        args = new String[] {
                "-database",
                "petstore",
                "-config",
                "sql/lealone.sql",
                "sql/llm.sql",
                "sql/tables.sql",
                "sql/init-data.sql",
                "sql/services.sql" };
        com.lealone.main.Lealone.main(args);
    }

    public static class JwtUtils {

        private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        private static final long EXPIRATION_TIME = 86400000; // 24 hours

        public static String generateToken(String userId) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", userId);
            Date now = new Date();
            Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

            return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiration)
                    .signWith(key).compact();
        }

        public static Claims validateToken(String token) {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }

        public static String getUserIdFromToken(String token) {
            Claims claims = validateToken(token);
            return claims.get("userId", String.class);
        }

        public static boolean isTokenExpired(String token) {
            Claims claims = validateToken(token);
            return claims.getExpiration().before(new Date());
        }
    }

    public static class Router extends TomcatRouter {

        private String webRoot;
        private String uploadDirectory;

        @Override
        public void init(Map<String, String> config) {
            webRoot = config.get("web_root");
            String uploadDirectory = config.get("upload_directory");
            if (uploadDirectory == null)
                uploadDirectory = webRoot + "/store/img/file_uploads";
            this.uploadDirectory = uploadDirectory;

            addFilter(new RedirectFilter(), "/*");
            if (isDevelopmentEnvironment(config)) {
                addFilter(new TemplateFilter(), "*.html");
            }
            addFilter(new FileUploadFilter(), "/service/store_service/add_product");
            addFilter(new JwtFilter(), "/service/user_service/update");
            addFilter(new JwtFilter(), "/service/user_service/get_user");
            addFilter(new JwtFilter(), "/service/view_cart_service/*");
            tomcatServer.getContext().setAllowCasualMultipartParsing(true);
            super.init(config);
        }

        private boolean isDevelopmentEnvironment(Map<String, String> config) {
            String environment = config.get("environment");
            if (environment != null) {
                environment = environment.trim().toLowerCase();
                if (environment.equals("development") || environment.equals("dev")) {
                    return true;
                }
            }
            return false;
        }

        private class TemplateFilter extends HttpFilter {
            @Override
            protected void doFilter(HttpServletRequest request, HttpServletResponse response,
                    FilterChain chain) throws IOException, ServletException {
                TemplateEngine te = new TemplateEngine(webRoot, "utf-8");
                String file = request.getRequestURI();
                try {
                    String str = te.process(file);
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/html; charset=utf-8");
                    response.getWriter().write(str);
                } catch (Exception e) {
                }
            }
        }

        private class RedirectFilter extends HttpFilter {
            @Override
            protected void doFilter(HttpServletRequest request, HttpServletResponse response,
                    FilterChain chain) throws IOException, ServletException {
                String url = request.getRequestURI();
                if (url.equals("/")) {
                    response.sendRedirect("/home/index.html");
                } else if (url.equals("/user")) {
                    response.sendRedirect("/user/index.html");
                } else if (url.equals("/store")) {
                    response.sendRedirect("/store/index.html");
                } else if (url.equals("/redirect.do")) {
                    response.sendRedirect(request.getParameter("location"));
                } else {
                    chain.doFilter(request, response);
                }
            }
        }

        private class FileUploadFilter extends HttpFilter {
            @Override
            protected void doFilter(HttpServletRequest request, HttpServletResponse response,
                    FilterChain chain) throws IOException, ServletException {
                for (Part p : request.getParts()) {
                    if ("logo_file".equalsIgnoreCase(p.getName())) {
                        String fileName = p.getSubmittedFileName();
                        File logoFile = new File(uploadDirectory, fileName);
                        p.write(logoFile.getCanonicalPath());
                    }
                }
                chain.doFilter(request, response);
            }
        }

        private class JwtFilter extends HttpFilter {
            @Override
            protected void doFilter(HttpServletRequest request, HttpServletResponse response,
                    FilterChain chain) throws IOException, ServletException {
                String token = request.getHeader("Authorization");
                if (token == null || token.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Authorization token required\"}");
                    return;
                }

                try {
                    // 移除Bearer前缀
                    if (token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }
                    JwtUtils.validateToken(token);
                    chain.doFilter(request, response);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
                }
            }
        }
    }
}
