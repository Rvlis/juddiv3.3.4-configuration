# 服务计算 juddi实验
## 一、环境
- __jdk=1.8(1.6+), maven=3.0.4, juddi=3.3.4__
## 二、部署
1.  进入<http://archive.apache.org/dist/juddi/juddi/3.3.4/>下载 ___juddi-distro-3.3.4.zip（or tar.gz）___，解压到本地 
   <!-- ![juddi解压内容](/res/img/juddi解压内容.png) -->
   <div align="center">
      <img src="./res/img/juddi解压内容.png" width = "80%" alt="juddi解压" align=center />
   </div>
   
   
2.  使用内置tomcat  __juddi-tomcat-3.3.4__
   - ___cd___ __your/path/juddi-distro-3.3.4/juddi-tomcat-3.3.4/bin__
   - __./catalina.bat run__
   
   <!-- ![tomcat路径](/res/img/tomcat_path.png) -->
   <div align="center">
      <img src="./res/img/tomcat_path.png" width = "80%" alt="tomcat路径" align=center />
   </div>
   
   - 访问 __localhost:8080__，打开juddi启动页面
   
   <!-- ![juddi启动页面](/res/img/juddi启动页面.png) -->
   <div align="center">
      <img src="./res/img/juddi启动页面.png" width = "80%" alt="juddi启动页面" align=center />
   </div>
   

## 三、实验：服务的注册和查询
### UDDI 数据模型
   <div align="center">
      <img src="./res/img/UDDI-data-model.png" width = "80%" alt="uddi-data-model" align=center />
   </div>

### 1. 通过juddi用户界面
- __点击jUDDI User Interface，进入用户界面__

  <!-- ![welcome-page](./res/img/welcome-to-juddi.jpg)  -->
   <div align="center">
      <img src="./res/img/welcome-to-juddi.jpg" width = "80%" alt="welcome-page" align=center />
   </div>
  <!-- ![home-page](./res/img/juddi-home-page.png) -->
  <div align="center">
      <img src="./res/img/juddi-home-page.png" width = "80%" alt="home-page" align=center />
   </div>
  
- __点击右上角登录__ ___默认用户名、密码:（uddi,uddi）___
- __create tModel Partition(Key Generator)__
  
  <!-- ![tModel-partition-location](./res/img/key-generator-location.png) -->
  <div align="center">
      <img src="./res/img/key-generator-location.png" width = "80%" alt="tModel-partition-location" align=center />
   </div>
  TModel密钥生成器是tModel的一种特殊类型，借助它可以使用所需的任意tModel前缀定义新的tModel。例如，如果您希望将tModel定义为 `uddi:www.mycompany.com:ServiceAuthenticationMethod`, 则首先必须创建一个值为 `uddi:www.mycompany.com:keyGenerator` 的tModel密钥生成器。这是UDDI规范的一部分，同时充当管理机制。
- __创建一个bytedance的key generator__

  <!-- ![bytedance](res/img/bytedance-key-generator.png) -->
  <div align="center">
      <img src="res/img/bytedance-key-generator.png" width = "80%" alt="bytedance" align=center />
   </div>
  
- __create business， 使用创建的bytedance的key generator作为前缀__

  <!-- ![business-editor](./res/img/business-editor.png) -->
  <div align="center">
      <img src="./res/img/business-editor.png" width = "80%" alt="business-editor" align=center />
   </div>
  
- __create service，创建服务__
  每个business entity下包含多个服务service

   首先生成以 ___business entity为前缀的key generator___，例如`uddi:www.bytedance.com:management-unit:keygenerator`
   
  <!-- ![management-unit-key-generator](./res/img/management-unit-key-generator.png) -->
  <div align="center">
      <img src="./res/img/management-unit-key-generator.png" width = "80%" alt="management-unit-key-generator" align=center />
   </div>
  
  然后创建服务
  <!-- ![create-service-location](res/img/create-service-location.png) -->
  <div align="center">
      <img src="./res/img/create-service-location.png" width = "80%" alt="create-service-location" align=center />
   </div>
  <!-- ![add-service](./res/img/add-service.png) -->
  <div align="center">
      <img src="./res/img/add-service.png" width = "80%" alt="add-service" align=center />
   </div>
  <!-- ![service-editor](./res/img/service-editor.png) -->
  <div align="center">
      <img src="./res/img/service-editor.png" width = "80%" alt="service-editor" align=center />
   </div>
  
- __查询__

  <!-- ![browse-location](./res/img/browse-location.png) -->
  <div align="center">
      <img src="./res/img/browse-location.png" width = "80%" alt="browse-location" align=center />
   </div>
  <!-- ![browse-result](./res/img/browse-result.png) -->
  <div align="center">
      <img src="./res/img/browse-result.png" width = "80%" alt="browse-result" align=center />
   </div>

### 2. 通过接口
[Examples](examples/)
