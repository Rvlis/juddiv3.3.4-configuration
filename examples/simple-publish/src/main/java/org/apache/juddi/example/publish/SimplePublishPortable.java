/*
 * Copyright 2001-2010 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.juddi.example.publish;

import org.uddi.api_v3.*;
import org.apache.juddi.api_v3.*;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.v3_service.UDDISecurityPortType;
import org.uddi.v3_service.UDDIPublicationPortType;
import java.util.*;

/**
 * This shows you to interact with a UDDI server by publishing a Business,
 * Service and Binding Template. It uses some fairly generic code that should be
 * mostly portable to any other UDDI client library and is therefore consider
 * "portable". URLs are set in uddi.xml
 *
 */
public class SimplePublishPortable {

        private static UDDISecurityPortType security = null;
        private static UDDIPublicationPortType publish = null;

        public SimplePublishPortable() {
                try {
                        // create a client and read the config in the archive; 
                        // you can use your config file name
                        UDDIClient uddiClient = new UDDIClient("META-INF/uddi.xml");
                        // a UddiClient can be a client to multiple UDDI nodes, so 
                        // supply the nodeName (defined in your uddi.xml.
                        // The transport can be WS, inVM, RMI etc which is defined in the uddi.xml
                        Transport transport = uddiClient.getTransport("default");
                        // Now you create a reference to the UDDI API
                        security = transport.getUDDISecurityService();
                        publish = transport.getUDDIPublishService();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * This function shows you how to publish to UDDI using a fairly generic
         * mechanism that should be portable (meaning use any UDDI v3 library
         * with this code)
         */
        public void publish() {

                Scanner scan = new Scanner(System.in);
                try {
                        // Login aka retrieve its authentication token
                        GetAuthToken getAuthTokenMyPub = new GetAuthToken();

                        // username
                        System.out.println("Please enter your username:");
                        String userName = scan.nextLine();
                        getAuthTokenMyPub.setUserID(userName); 

                        // password
                        System.out.println("Please enter your password:");
                        String psd = scan.nextLine();                        
                        getAuthTokenMyPub.setCred(psd); 
                        // print authtoken
                        AuthToken myPubAuthToken = security.getAuthToken(getAuthTokenMyPub);
                        System.out.println(getAuthTokenMyPub.getUserID() + "'s AUTHTOKEN = " + myPubAuthToken.getAuthInfo());

                        // Creating the parent business entity that will contain our service.
                        BusinessEntity myBusEntity = new BusinessEntity();
                        Name myBusName = new Name();
                        Description myBusDes = new Description();
                        // String businessKey = new String();
                        // business key
                        System.out.println("Please enter business key:");
                        String businessKey = scan.nextLine();
                        // business entity name
                        System.out.println("Please enter Business Entity Name and language");
                        String businessEntityName = scan.nextLine();
                        String language = scan.nextLine();
                        myBusName.setValue(businessEntityName);
                        myBusName.setLang(language);
                        // business description
                        System.out.println("Please enter description and language:");
                        String description = scan.nextLine();
                        language = scan.nextLine();
                        // System.out.println("description is "+ description);
                        myBusDes.setValue(description);
                        myBusDes.setLang(language);
                        // save name, description and businesskey
                        myBusEntity.getName().add(myBusName);
                        myBusEntity.getDescription().add(myBusDes);
                        myBusEntity.setBusinessKey(businessKey);

                        // save business
                        SaveBusiness sb = new SaveBusiness();
                        sb.getBusinessEntity().add(myBusEntity);
                        sb.setAuthInfo(myPubAuthToken.getAuthInfo());
                        BusinessDetail bd = publish.saveBusiness(sb);
                        String myBusKey = bd.getBusinessEntity().get(0).getBusinessKey();
                        System.out.println("Mybusiness key:  " + myBusKey);

                        // Creating a service to save.  Only adding the minimum data: the parent business key retrieved from saving the business 
                        // above and a single name.
                        BusinessService myService = new BusinessService();
                        myService.setBusinessKey(myBusKey);
                        Name myServName = new Name();
                        Description mySerDes = new Description();
                        // service name
                        System.out.println("Please enter service key:");
                        String serviceKey = scan.nextLine();
                        System.out.println("Please enter Service Name and language:");
                        String serviceName = scan.nextLine();
                        language = scan.nextLine();
                        myServName.setValue(serviceName);
                        myServName.setLang(language);
                        System.out.println("Please enter description and language:");
                        description = scan.nextLine();
                        language = scan.nextLine();
                        // System.out.println("description is "+ description);
                        mySerDes.setValue(description);
                        mySerDes.setLang(language);

                        myService.getName().add(myServName);
                        myService.getDescription().add(mySerDes);
                        myService.setServiceKey(serviceKey);

                        // Add binding templates, etc...
                        BindingTemplate myBindingTemplate = new BindingTemplate();
                        AccessPoint accessPoint = new AccessPoint();
                        accessPoint.setUseType(AccessPointType.WSDL_DEPLOYMENT.toString());
                        accessPoint.setValue("http://example.org/services/myservice?wsdl");
                        myBindingTemplate.setAccessPoint(accessPoint);
                        BindingTemplates myBindingTemplates = new BindingTemplates();
                        //optional but recommended step, this annotations our binding with all the standard SOAP tModel instance infos
                        myBindingTemplate = UDDIClient.addSOAPtModels(myBindingTemplate);
                        myBindingTemplates.getBindingTemplate().add(myBindingTemplate);

                        myService.setBindingTemplates(myBindingTemplates);

                        // Adding the service to the "save" structure, using our publisher's authentication info and saving away.
                        SaveService ss = new SaveService();
                        ss.getBusinessService().add(myService);
                        ss.setAuthInfo(myPubAuthToken.getAuthInfo());
                        ServiceDetail sd = publish.saveService(ss);
                        String myServKey = sd.getBusinessService().get(0).getServiceKey();
                        System.out.println("myService key:  " + myServKey);

                        security.discardAuthToken(new DiscardAuthToken(myPubAuthToken.getAuthInfo()));
                        
                        System.out.println("Saved Successfully!");

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public static void main(String args[]) {
                // System.out.println("hello world");
                SimplePublishPortable sp = new SimplePublishPortable();
                sp.publish();
        }
}
