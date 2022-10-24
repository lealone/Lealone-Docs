## 编译运行需要

* JDK 1.8+
* Maven 3.3+


## 下载脚手架

[create-lealone-app-5.0.0.jar](https://github.com/lealone/Lealone-Plugins/releases/download/lealone-plugins-5.0.0/create-lealone-app-5.0.0.jar)


## 创建一个 hello 应用

`java -jar create-lealone-app-5.0.0.jar -groupId org.lealone.examples.hello -artifactId hello`

应用脚手架的参数用法可以阅读 [README](https://github.com/lealone/Lealone-Plugins/blob/master/service/create-app/README.md) 文件


## 构建 hello 应用

cd hello

build -p


## 运行 hello 应用

cd target\hello-1.0.0\bin

lealone


## 打开页面

http://localhost:9000/

