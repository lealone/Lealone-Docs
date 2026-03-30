# Lealone-PetStore

基于 Vue + Lealone 开发的 PetStore 应用，改编自 [Webx 的 PetStore](https://github.com/webx/citrus-sample)



### 构建

build -p



### 运行

cd target\petstore-1.0.0\bin

petstore



### 打开页面

http://localhost:8080/



### 在 IDE 中开发调试


把代码导入到 IDE 后，

先运行 PetStoreTest 启动应用，

最后就能打开页面查看了。



### 可能出现的问题

如果执行 build -p 找不到 Lealone 的依赖包，
需要下载以下项目的代码: 

[lealone-database](https://github.com/lealone/Lealone)


执行 build -i 把它们安装到本地的 maven 仓库即可。
