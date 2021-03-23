# 服务计算 juddi实验
## 环境
- __jdk=1.8(1.6+), maven=3.0.4, juddi=3.3.4__
## 部署
1.  进入<http://archive.apache.org/dist/juddi/juddi/3.3.4/>下载 ___juddi-distro-3.3.4.zip（or tar.gz）___，解压到本地 
   ![juddi解压内容](/res/img/juddi解压内容.png)
2.  使用内置tomcat
   - ___cd___ __your/path/juddi-distro-3.3.4/juddi-tomcat-3.3.4/bin__
   - __./catalina.bat run__
   ![tomcat路径](/res/img/tomcat_path.png)
   - 访问 __localhost:8080__,访问到juddi启动页面
   ![juddi启动页面](/res/img/juddi启动页面.png)

## 使用：服务的注册和查询
### 1. 通过juddi用户界面

### 2. 通过接口






   ~~拷贝 your/path/juddi-distro-3.3.4/juddi-tomcat-3.3.4~~
   ~~打开环境变量页面，添加系统变量名 ___CATALINA_HOME___，变量值为拷贝的路径~~
  ~~path选项中添加 ___CATALIAN_HOME\bin___~~
  ~~通过命令 ___startup___ 启动tomcat~~
  ~~浏览器地址栏访问 __localhost:8080__~~