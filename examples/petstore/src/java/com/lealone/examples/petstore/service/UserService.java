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
package com.lealone.examples.petstore.service;

import com.lealone.examples.petstore.JwtUtils;
import com.lealone.examples.petstore.model.Account;
import com.lealone.examples.petstore.model.User;
import com.lealone.orm.json.JsonObject;

public class UserService {

    public String login(String userId, String password) {
        User user = User.dao.where().userId.eq(userId).and().password.eq(password).findOne();
        if (user == null)
            throw new RuntimeException("用户名不存在或密码错误");
        String token = JwtUtils.generateToken(userId);
        JsonObject json = new JsonObject();
        json.put("user", user);
        json.put("token", token);
        return json.encode();
    }

    public String logout(String userId) {
        return "ok";
    }

    public void register(String userId, String password, String password2) {
        new User().userId.set(userId).password.set(password).insert();
    }

    public void update(Account account) {
        Account old = Account.dao.where().userId.eq(account.userId.get()).findOne();
        if (old == null)
            account.insert();
        else
            account.update();
    }

    public String getUser(String userId) {
        User user = User.dao.where().userId.eq(userId).findOne();
        Account account = Account.dao.where().userId.eq(userId).findOne();
        if (account == null)
            account = new Account();
        JsonObject json = new JsonObject();
        json.put("user", user);
        json.put("account", account);
        return json.encode();
    }
}
