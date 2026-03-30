/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lealone.examples.petstore.test;

import java.io.File;
import java.net.URL;

import com.lealone.examples.petstore.main.PetStore;
import com.lealone.examples.petstore.web.PetStoreRouter;
import com.lealone.main.LealoneApplication;

public class PetStoreTest {

    public static void main(String[] args) {
        String[] sqlScripts = {
                // 执行建表脚本，同时自动生成对应的模型类的代码
                getAbsolutePath("petstore-model/sql/tables.sql"),
                // 初始化数据
                getAbsolutePath("petstore-model/sql/init-data.sql"),
                // 执行服务创建脚本，同时自动生成对应的服务接口代码
                getAbsolutePath("petstore-service/sql/services.sql") };

        LealoneApplication app = new LealoneApplication();
        app.setBaseDir("./target/test-data");
        // 动态生成绝对路径的webRoot，使用相对路径在eclipse和idea下面总有一个不正确
        app.setWebRoot(getAbsolutePath("petstore-web/web"));
        app.setDatabase("petstore");
        app.setInitSql("set @srcPath '" + getAbsolutePath("petstore-model/src/main/java") + "'");
        app.setSqlScripts(sqlScripts);
        app.setEnvironment("dev");
        app.setRouter(PetStoreRouter.class);
        app.start();
    }

    public static String getAbsolutePath(String name) {
        String appBaseDir;
        try {
            String classFile = PetStore.class.getName().replace('.', '/') + ".class";
            URL url = PetStore.class.getClassLoader().getResource(classFile);
            String file = new File(url.toURI()).getAbsolutePath();
            int pos = file.indexOf("petstore-main");
            appBaseDir = file.substring(0, pos - 1);
        } catch (Exception e) {
            appBaseDir = ".";
        }
        return new File(appBaseDir, name).getAbsolutePath().replace('/', File.separatorChar);
    }
}
