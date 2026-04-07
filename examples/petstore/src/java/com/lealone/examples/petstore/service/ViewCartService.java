/// *
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements. See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership. The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License. You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
// package com.lealone.examples.petstore.service;
//
// import java.util.ArrayList;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.concurrent.CopyOnWriteArrayList;
//
// import com.lealone.examples.petstore.model.Item;
// import com.lealone.orm.json.JsonArray;
// import com.lealone.orm.json.JsonObject;
//
// public class ViewCartService {
//
// public static class ViewCart {
// CopyOnWriteArrayList<String> items = new CopyOnWriteArrayList<>();
// }
//
// private final ConcurrentHashMap<String, ViewCart> viewCarts = new ConcurrentHashMap<>();
//
// public void addItem(String cartId, String itemId) {
// ViewCart viewCart = new ViewCart();
// ViewCart old = viewCarts.putIfAbsent(cartId, viewCart);
// if (old != null)
// viewCart = old;
// viewCart.items.add(itemId);
// }
//
// public void removeItem(String cartId, String itemId) {
// ViewCart viewCart = viewCarts.get(cartId);
// if (viewCart != null)
// viewCart.items.remove(itemId);
// }
//
// public void update(String cartId, String itemId, Integer quantity) {
// // TODO
// }
//
// public String getItems(String cartId) {
// JsonObject json = new JsonObject();
// ViewCart viewCart = viewCarts.get(cartId);
// if (viewCart == null) {
// json.put("items", new JsonArray());
// } else {
// int size = viewCart.items.size();
// ArrayList<Item> items = new ArrayList<>(size);
// for (int i = 0; i < size; i++) {
// Item item = Item.dao.where().itemid.eq(viewCart.items.get(i)).findOne();
// items.add(item);
// }
// json.put("items", new JsonArray(items));
// }
// return json.encode();
// }
// }
