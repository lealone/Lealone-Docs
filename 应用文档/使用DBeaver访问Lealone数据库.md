
## 下载 DBeaver 和 Lealone

* [DBeaver](https://dbeaver.io/download/)

* [lealone-6.0.0.jar](https://github.com/lealone/Lealone/releases/download/lealone-6.0.0/lealone-6.0.0.jar)

## 在 DBeaver 中配置 Lealone 的驱动

点击菜单: 数据库 -> 驱动管理器
然后在"驱动管理器"窗口中点"新建"按钮

在"设置"界面中配置以下参数:
驱动名称: Lealone
驱动类型: Generic
类名: com.lealone.client.jdbc.JdbcDriver
URL 模板: jdbc:lealone:tcp://{host}[:{port}]/[{database}]
默认端口: 9210
默认数据库: lealone
默认用户: root
勾选”允许空密码"

![dbeaver01](https://github.com/lealone/Lealone-Docs/assets/872655/5b97e98e-d0d6-4487-a18f-130a78476a77)

在"库"界面点"添加文件"按钮，把 lealone-6.0.0.jar 加进去
最后点"确定"按钮完成 Lealone 的驱动的配置

![dbeaver02](https://github.com/lealone/Lealone-Docs/assets/872655/095201e5-1e56-46ad-8f3d-497c1f12dfe6)


## 在 DBeaver 中连接 Lealone 数据库

点击菜单: 数据库 -> 新建数据库连接
在"连接到数据库"窗口的搜索栏中输入 Lealone
双击打 Lealone 的图标

![dbeaver03](https://github.com/lealone/Lealone-Docs/assets/872655/70a2e7af-e946-439d-98fc-5c53e66639e1)

在"数据库/模式"那一栏输入想要连接的数据库名称(默认是 lealone)

![dbeaver04](https://github.com/lealone/Lealone-Docs/assets/872655/383454dc-fa84-43f8-9c1f-14080dadb99a)

点"完成"按钮，连接成功后就会看到以下界面:

![dbeaver05](https://github.com/lealone/Lealone-Docs/assets/872655/41c40a4a-9335-422d-aa6e-f43c66ad9821)

