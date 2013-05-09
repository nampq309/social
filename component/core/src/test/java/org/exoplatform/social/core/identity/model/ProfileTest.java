/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU Affero General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.social.core.identity.model;

import junit.framework.TestCase;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.junit.Before;

public class ProfileTest extends TestCase {
  
  private static Log log = ExoLogger.getLogger(ProfileTest.class);
  
  @Before
  public void setUp() throws Exception {
    log.info("Running "+this.getName());
  }
  
  public void testAddPhone() throws Exception {
    Profile profile = new Profile(new Identity("organization", "root"));
    profile.addPhone("Home", "123456789");
    profile.addPhone("Work", "12345");
    profile.addPhone("Other", "54321");
    
    assertEquals("123456789", profile.homePhones().at(0));
    assertEquals("12345", profile.workPhones().at(0));
    assertEquals("54321", profile.otherPhones().at(0));
    
    //add more work phone
    profile.addPhone("Work", "45678");
    profile.addPhone("Work", "7890");
    assertEquals("45678", profile.workPhones().at(1));
    assertEquals("7890", profile.workPhones().at(2));
    
    //more Other phone
    profile.addPhone("Other", "12355555");
    assertEquals("12355555", profile.otherPhones().at(1));
    
  }
  
  public void testAddURL() throws Exception {
    Profile profile = new Profile(new Identity("organization", "root"));
    
    //check URLs list is empty
    assertEquals(0, profile.getURLs().size());

    String url = "http://exoplatform.com";
    //add one URL to profile
    profile.addURL(url);
    
    assertEquals(url, profile.getURLs().get(0));
    //add more URLs to Profile
    url = "http://test.exoplatform.com";
    profile.addURL(url);
    
    for(String sURL : profile.getURLs()){
      log.info("URL ='"+sURL+ "'");
    }
    
    assertEquals(2, profile.getURLs().size());
  }
  
  public void testAddURLIsNull() throws Exception {
    Profile profile = new Profile(new Identity("organization", "root"));
    profile.addURL(null);
    assertTrue(profile.getURLs().size() == 0);
  }
  
  public void testAddInstanceMessage() throws Exception {
    Profile profile = new Profile(new Identity("organization", "root"));
    String imAccount = "username@gtalk";
    String imType = "Gtalk";
    profile.addIM(imType, imAccount);
    
    assertEquals(imAccount, profile.getIMs(imType).get(0));
    
    profile.addIM(null, null);
    assertEquals(0, profile.getIMs(null).size());
    
    imAccount = "username@yahoo";
    imType = "Yahoo";
    profile.addIM(imType, imAccount);
    assertEquals(imAccount, profile.getIMs(imType).get(0));
    
  }

}
