1. Start the jUDDI-server (juddi-tomcat or juddi-bundle)

2. Check the settings of the META-INF/uddi.xml, to make sure the serverName and serverPort are set correctly.

3. `mvn -q -Pdemo test`

4. output:
   
   <!-- ![browse-output](../../res/img/browse-output.png) -->
   <div align="center">
      <img src="../../res/img/browse-output.png" width = "80%" alt="browse-output" align=center />
   </div>