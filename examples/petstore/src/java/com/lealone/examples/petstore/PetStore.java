/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.examples.petstore;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.lealone.http.tomcat.TomcatRouter;

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

    public static class Router extends TomcatRouter {

        private String uploadDirectory;

        @Override
        public void init(Map<String, String> config) {
            String uploadDirectory = config.get("upload_directory");
            if (uploadDirectory == null)
                uploadDirectory = webRoot + "/store/img/file_uploads";
            this.uploadDirectory = uploadDirectory;

            addFilter(new RedirectFilter(), "/*");
            addFilter(new FileUploadFilter(), "/service/store_service/add_product");
            // addFilter(new JwtFilter(), "/service/user_service/update");
            // addFilter(new JwtFilter(), "/service/user_service/get_user");
            // addFilter(new JwtFilter(), "/service/view_cart_service/*");
            tomcatServer.getContext().setAllowCasualMultipartParsing(true);
            super.init(config);
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
    }
}
