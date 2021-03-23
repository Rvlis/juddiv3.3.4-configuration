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
package org.apache.juddi.example.browse;

import java.util.List;
import org.apache.juddi.api_v3.AccessPointType;
import org.apache.juddi.v3.client.UDDIConstants;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.api_v3.AuthToken;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessInfos;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.CategoryBag;
import org.uddi.api_v3.Contacts;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.DiscardAuthToken;
import org.uddi.api_v3.FindBusiness;
import org.uddi.api_v3.GetAuthToken;
import org.uddi.api_v3.GetBusinessDetail;
import org.uddi.api_v3.GetServiceDetail;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.ServiceDetail;
import org.uddi.api_v3.ServiceInfos;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDISecurityPortType;
import java.util.*;

/**
 * A Simple UDDI Browser that dumps basic information to console
 *
 * @author <a href="mailto:alexoree@apache.org">Alex O'Ree</a>
 */
public class SimpleBrowse {

        private static UDDISecurityPortType security = null;
        private static UDDIInquiryPortType inquiry = null;

        /**
         * This sets up the ws proxies using uddi.xml in META-INF
         */
        public SimpleBrowse() {
                try {
        	// create a manager and read the config in the archive; 
                        // you can use your config file name
                        UDDIClient client = new UDDIClient("META-INF/simple-browse-uddi.xml");
        	// a UDDIClient can be a client to multiple UDDI nodes, so 
                        // supply the nodeName (defined in your uddi.xml.
                        // The transport can be WS, inVM, RMI etc which is defined in the uddi.xml
                        Transport transport = client.getTransport("default");
                        // Now you create a reference to the UDDI API
                        security = transport.getUDDISecurityService();
                        inquiry = transport.getUDDIInquiryService();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * Main entry point
         *
         * @param args
         */
        public static void main(String args[]) {

                SimpleBrowse sp = new SimpleBrowse();
                sp.Browse(args);
        }

        public void Browse(String[] args) {
                Scanner scan = new Scanner(System.in);
                
                try {
                        System.out.println("Please enter your name:");
                        String userName = scan.nextLine();
                        System.out.println("Please enter your password:");
                        String psd = scan.nextLine();

                        String token = GetAuthKey(userName, psd);

                        BusinessList findBusiness = GetBusinessList(token);
                        PrintBusinessInfo(findBusiness.getBusinessInfos());
                        // PrintBusinessDetails(findBusiness.getBusinessInfos(), token);
                        // PrintServiceDetailsByBusiness(findBusiness.getBusinessInfos(), token);

                        security.discardAuthToken(new DiscardAuthToken(token));

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * Find all of the registered businesses. This list may be filtered
         * based on access control rules
         *
         * @param token
         * @return
         * @throws Exception
         */
        private BusinessList GetBusinessList(String token) throws Exception {
                FindBusiness fb = new FindBusiness();
                fb.setAuthInfo(token);
                org.uddi.api_v3.FindQualifiers fq = new org.uddi.api_v3.FindQualifiers();
                fq.getFindQualifier().add(UDDIConstants.APPROXIMATE_MATCH);

                fb.setFindQualifiers(fq);
                Name searchname = new Name();
                searchname.setValue(UDDIConstants.WILDCARD);
                fb.getName().add(searchname);
                BusinessList findBusiness = inquiry.findBusiness(fb);
                return findBusiness;

        }


        private void PrintServiceDetail(BusinessService get) {
                if (get == null) {
                        return;
                }
                System.out.println("Name: " + ListToString(get.getName()));
                System.out.println("Description: " + ListToDescString(get.getDescription()));
                System.out.println("Key: " + (get.getServiceKey()));

        }

        
        private enum AuthStyle {

                HTTP_BASIC,
                HTTP_DIGEST,
                HTTP_NTLM,
                UDDI_AUTH,
                HTTP_CLIENT_CERT
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
        private String GetAuthKey(String username, String password) {
                try {

                        GetAuthToken getAuthTokenRoot = new GetAuthToken();
                        getAuthTokenRoot.setUserID(username);
                        getAuthTokenRoot.setCred(password);

                        // Making API call that retrieves the authentication token for the user.
                        AuthToken rootAuthToken = security.getAuthToken(getAuthTokenRoot);
                        // System.out.println(username + " AUTHTOKEN = (don't log auth tokens!");
                        return rootAuthToken.getAuthInfo();
                } catch (Exception ex) {
                        System.out.println("Could not authenticate with the provided credentials " + ex.getMessage());
                }
                return null;
        }

        

        private void PrintBusinessInfo(BusinessInfos businessInfos) {
                if (businessInfos == null) {
                        System.out.println("No data returned");
                } else {
                        for (int i = 0; i < businessInfos.getBusinessInfo().size(); i++) {
                                System.out.println("=================================================================================");
                                System.out.println("Business Key: " + businessInfos.getBusinessInfo().get(i).getBusinessKey());
                                System.out.println("Name: " + ListToString(businessInfos.getBusinessInfo().get(i).getName()));

                                System.out.println("Description: " + ListToDescString(businessInfos.getBusinessInfo().get(i).getDescription()));
                                System.out.println("Services:");
                                PrintServiceInfo(businessInfos.getBusinessInfo().get(i).getServiceInfos());
                        }
                }
        }

        private String ListToString(List<Name> name) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < name.size(); i++) {
                        sb.append(name.get(i).getValue()).append(" ");
                }
                return sb.toString();
        }

        private String ListToDescString(List<Description> name) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < name.size(); i++) {
                        sb.append(name.get(i).getValue()).append(" ");
                }
                return sb.toString();
        }

        private void PrintServiceInfo(ServiceInfos serviceInfos) {
                for (int i = 0; i < serviceInfos.getServiceInfo().size(); i++) {
                        System.out.println("------------------------------------------------------------------------");
                        System.out.println("Service Key: " + serviceInfos.getServiceInfo().get(i).getServiceKey());
                        System.out.println("Owning Business Key: " + serviceInfos.getServiceInfo().get(i).getBusinessKey());
                        System.out.println("Name: " + ListToString(serviceInfos.getServiceInfo().get(i).getName()));
                }
        }
}
