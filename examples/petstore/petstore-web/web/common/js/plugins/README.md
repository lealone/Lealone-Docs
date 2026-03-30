# Lealone-JS

Lealone 前端动态化与智能化 RPC 客户端

不必学习 node.js、npm、webpack 等前端构建工具也能进行前端工程化组件化开发。


### lealone-rpc-5.0.0.js

RPC 客户端，动态智能化绑定后端服务，可以使用 axios 或 sockjs 与后端通信。


### lealone-vue-5.0.0.js

vue 框架扩展库，支持跨应用跨组件路由，把组件绑定到后端服务能极大降低应用的 js 代码量。


### vue-2.6.12.js

vue 框架的定制版本，只修改了少量代码，增加了两个功能:

1. 允许在不挂载组件的情况下获取组件实例

2. 允许组件依赖的数据就绪后再执行组件渲染操作


### 用法

完整的例子可以从 [PetStore](https://github.com/lealone/Lealone-Examples/tree/main/petstore) 这个应用开始

PetStore 是一个多页应用，分成三个子应用: 

[home](https://github.com/lealone/Lealone-Examples/blob/main/petstore/petstore-web/web/home/index.html) 主页与购物车

[user](https://github.com/lealone/Lealone-Examples/blob/main/petstore/petstore-web/web/user/index.html) 用户管理

[store](https://github.com/lealone/Lealone-Examples/blob/main/petstore/petstore-web/web/store/index.html) 库存管理


