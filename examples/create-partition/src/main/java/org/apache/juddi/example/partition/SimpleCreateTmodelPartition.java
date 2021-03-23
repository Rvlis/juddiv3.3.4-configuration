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
package org.apache.juddi.example.partition;

import java.util.Properties;
import javax.xml.ws.BindingProvider;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.juddi.v3.client.config.UDDIClerk;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.config.UDDINode;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.api_v3.AuthToken;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.CategoryBag;
import org.uddi.api_v3.GetAuthToken;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.SaveTModel;
import org.uddi.api_v3.TModel;
import org.uddi.api_v3.TModelDetail;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;
import java.util.*;

/**
 * An example for creating a key partition, aka key generator, aka 'special'
 * tModel
 *
 * @author <a href="mailto:alexoree@apache.org">Alex O'Ree</a>
 */
public class SimpleCreateTmodelPartition {

        private static UDDISecurityPortType security = null;
        private static UDDIPublicationPortType publish = null;

        private static UDDIClient uddiClient = null;

        /**
         * This sets up the ws proxies using uddi.xml in META-INF
         */
        public SimpleCreateTmodelPartition() {
                try {
                        // create a manager and read the config in the archive; 
                        // you can use your config file name
                        uddiClient = new UDDIClient("META-INF/partition-uddi.xml");
                        uddiClient.start();

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
         * Main entry point
         *
         * @param args
         */
        public static void main(String args[]) throws ConfigurationException {

                SimpleCreateTmodelPartition sp = new SimpleCreateTmodelPartition();
                sp.TmodelsTheEasyWay(args);
                
                uddiClient.stop();
        }

        public void TmodelsTheEasyWay(String[] args) {
                Scanner scan = new Scanner(System.in);
                try {
                        
                        //This reads from the config file
                        UDDIClerk clerk = uddiClient.getClerk("defaultClerk");
                        
                        // username and password
                        System.out.println("Please enter your name:");
                        String userName = scan.nextLine();
                        clerk.setPublisher(userName);
                        System.out.println("Please enter your password:");
                        String psd = scan.nextLine();
                        clerk.setPassword(psd);
                        
                        // tmodelkey,name and language
                        System.out.println("Please enter tmodel key:");
                        String tModelKey = scan.nextLine();
                        System.out.println("Enter a name describing the key:");
                        String keyName = scan.nextLine();
                        System.out.println("Enter language:");
                        String language = scan.nextLine();
                        TModel keygen = UDDIClerk.createKeyGenator(tModelKey, keyName, language);
                        clerk.register(keygen);

                        System.out.println("Creation of Partition Success!");

                        clerk.discardAuthToken();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
 
        private enum AuthStyle {

                // HTTPS,
                HTTP,
                UDDI_AUTH

        }

        /**
         * Gets a UDDI style auth token, otherwise, appends credentials to the
         * ws proxies (not yet implemented)
         *
         * @param username
         * @param password
         * @param style
         * @return
         */
        private String GetAuthKey(String username, String password, AuthStyle style) {
                switch (style) {
                        case HTTP:
                                ((BindingProvider) publish).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
                                ((BindingProvider) publish).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
                                return null;
                        case UDDI_AUTH:
                                try {

                                        GetAuthToken getAuthTokenRoot = new GetAuthToken();
                                        getAuthTokenRoot.setUserID(username);
                                        getAuthTokenRoot.setCred(password);

                                        // Making API call that retrieves the authentication token for the 'root' user.
                                        AuthToken rootAuthToken = security.getAuthToken(getAuthTokenRoot);
                                        System.out.println(username + " AUTHTOKEN = (don't log auth tokens!)");
                                        return rootAuthToken.getAuthInfo();
                                } catch (Exception ex) {
                                        System.out.println("Could not authenticate with the provided credentials " + ex.getMessage());
                                }
                }

                return null;
        }
}
