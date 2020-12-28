# juddiv3.3.4-conf

##
- jdk=1.8
- maven=3.0.4(juddi pom文件里指定的是3.0.3，.3和.4的用法还是有点差距的，无所谓了~但是，如果要打成jar包，用3.1+吧)
- juddi=3.3.4
## Usage
- jdk、maven、juddi绑定的tomcat，该配置的环境变量都配置好吧 JAVA_HOME/MAVEN_HOME/CATALINA_HOME/CATALINE_BASE
- 注意tomcat用的是juddi中绑定的，具体路径：juddi-distro-3.3.4/juddi-tomcat-3.3.4
- 配置没问题的话,启动tomcat __startup.bat run__
- 打开localhost:8080地址，不出意外的话出现下面画面
![]()