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
// import java.util.List;
//
// import com.lealone.examples.petstore.model.Category;
// import com.lealone.examples.petstore.model.Item;
// import com.lealone.examples.petstore.model.Product;
// import com.lealone.orm.json.JsonArray;
// import com.lealone.orm.json.JsonObject;
//
// public class StoreService {
//
// public String addProduct(Product product) {
// product.insert();
// return null;
// }
//
// public String getAllCategories() {
// Product p = Product.dao;
// List<Category> list = Category.dao.join(p).on().catid.eq(p.categoryid) //
// .orderBy().catid.asc().findList();
// return new JsonObject().put("categories", new JsonArray(list)).encode();
// }
//
// // TODO 取inventory表的存货数
// public String getAllProductItems(String productId) {
//
// JsonObject json = new JsonObject();
//
// Category c = Category.dao;
// Product p = Product.dao;
// Item i = Item.dao;
//
// // 这是最简洁最高效的用法，只查一次数据库
// Product product = p.leftJoin(c).on().categoryid.eq(c.catid).leftJoin(i).on().productid
// .eq(i.productid) //
// .where().productid.eq(productId).findOne();
//
// json.put("category", product.getCategory());
// json.put("product", product);
// json.put("items", product.getItemList());
//
// /*
// // 以下三种用法都可行，但是不够简洁
// Category category = p.join(c).on().categoryid.eq(c.catid).join(i).on().productid.eq(i.productid) //
// .where().productid.eq(productId).m(c).findOne(); // m(c) 转到 Category
//
// json.put("category", category);
// json.put("product", category.getProductList().get(0));
// json.put("items", category.getProductList().get(0).getItemList());
//
// List<Item> items = p.join(c).on().categoryid.eq(c.catid).join(i).on().productid.eq(i.productid) //
// .where().productid.eq(productId).m(i).findList(); // m(i) 转到 Item
//
// json.put("category", items.get(0).getProduct().getCategory());
// json.put("product", items.get(0).getProduct());
// json.put("items", items);
//
// // 这种最慢了，要分三次查数据库
// // Product product = Product.dao.where().productid.eq(productId).findOne();
// // Category category = Category.dao.where().catid.eq(product.categoryid.get()).findOne();
// // List<Item> items = Item.dao.where().productid.eq(productId).findList();
// //
// // json.put("category", category);
// // json.put("product", product);
// // json.put("items", items);
// */
// return json.encode();
// }
// }
