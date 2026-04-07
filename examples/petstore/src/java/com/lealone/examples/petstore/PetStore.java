/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.examples.petstore;

import java.util.Map;

import com.lealone.http.tomcat.TomcatRouter;

public class PetStore {

    public static void main(String[] args) {
        args = new String[] {
                "-database",
                "petstore",
                "-config",
                "sql/lealone.sql",
                // "sql/llm.sql",
                "sql/tables.sql",
                "sql/init-data.sql",
                "sql/services.sql" };
        com.lealone.main.Lealone.main(args);
    }

    public static class Router extends TomcatRouter {
        @Override
        public void init(Map<String, String> config) {
            addRedirectFilter("/", "/home/index.html");
            addRedirectFilter("/user", "/user/index.html");
            addRedirectFilter("/store", "/store/index.html");
            addRedirectFilter("/redirect.do", "@location");
            addFileUploadFilter("/service/store_service/add_product");
            // addFilter(new JwtFilter(), "/service/user_service/update");
            // addFilter(new JwtFilter(), "/service/user_service/get_user");
            // addFilter(new JwtFilter(), "/service/view_cart_service/*");
            super.init(config);
        }
    }
}
