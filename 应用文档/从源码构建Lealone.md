## 编译需要

* Git 2.32+
* JDK 17+ (运行只需要 JDK 1.8+)
* Maven 3.8+


## 下载项目源代码

`git clone https://github.com/lealone/Lealone.git lealone`

假设源代码放在 `E:\lealone`


## 从源代码构建

进入 E:\lealone 目录，运行: `mvn clean package assembly:assembly -Dmaven.test.skip=true`

生成的文件放在 E:\lealone\target 目录中，
默认生成 lealone-x.y.z.tar.gz 和 lealone-x.y.z.zip 两个压缩文件，
其中 x.y.z 代表实际的版本号

如果运行: `mvn package -Dmaven.test.skip=true -P database`

只生成 lealone 数据库和 lealone-client 不包含依赖的最小 jar 包
