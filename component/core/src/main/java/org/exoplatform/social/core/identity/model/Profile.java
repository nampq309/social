/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.social.core.identity.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.exoplatform.social.core.chromattic.entity.ActivityProfileEntity;
import org.exoplatform.social.core.model.AvatarAttachment;

/**
 * The Class Profile.
 */
public class Profile {

  /** gender key. */
  public static final String        GENDER         = "gender";

  /** username key. */
  public static final String        USERNAME       = "username";

  /** firstname key. */
  public static final String        FIRST_NAME     = "firstName";

  /** lastname key. */
  public static final String        LAST_NAME      = "lastName";

  /** lastname key. */
  public static final String        FULL_NAME      = "fullName";
  
  /** email key. */
  public static final String        EMAIL          = "email";

  /** profile of a deleted user */
  public static final String        DELETED        = "deleted";

  /**
   * property of type {@link AvatarAttachment} that contains the avatar
   */
  public static final String        AVATAR         = "avatar";

  /**
   * url of the avatar (can be used instead of {@link #AVATAR})
   */
  @Deprecated
  public static final String        AVATAR_URL     = "avatarUrl";

  /** EXPERIENCE. */
  public static final String        EXPERIENCES    = "experiences";

  /** COMPANY. */
  public static final String        EXPERIENCES_COMPANY     = "company";

  /** POSITION. */
  public static final String        EXPERIENCES_POSITION    = "position";

  /** POSITION. */
  public static final String        EXPERIENCES_SKILLS      = "skills";

  /** START DATE OF EXPERIENCE. */
  public static final String        EXPERIENCES_START_DATE  = "startDate";

  /** END DATE OF EXPERIENCE. */
  public static final String        EXPERIENCES_END_DATE    = "endDate";

  /** CURRENT OR PAST EXPERIENCE. */
  public static final String        EXPERIENCES_IS_CURRENT  = "isCurrent";

  /** DESCRIPTION OF EXPERIENCE. */
  public static final String        EXPERIENCES_DESCRIPTION = "description";

  /** POSITION. */
  public static final String        POSITION       = "position";

  /**
   * An optional url for this profile
   */
  @Deprecated
  public static final String        URL            = "Url";

  /** PHONES key. */
  public static final String        CONTACT_PHONES = "phones";

  /** IMS key. */
  public static final String        CONTACT_IMS    = "ims";

  /** URLS key. */
  public static final String        CONTACT_URLS   = "urls";

  /** url postfix */
  public static final String        URL_POSTFIX    = "Url";

  /** Resized subfix */
  public static final String        RESIZED_SUBFIX = "RESIZED_";
  
  /** Key of Phone/URL/Image maps to reference to name of param. */
  public static final String KEY = "key";

  /** Key of of Phone/URL/Image maps to reference to value of param. */
  public static final String VALUE = "value";
  
  /** Define all of the phone types */
  static enum PHONE_TYPE {
    HOME("Home"), WORK("Work"), OTHER("Other");
    private String name;
    private PHONE_TYPE(String name){
      this.name = name;
    }
    public String getName() {
      return name;
    }
  }
  
  /** Define all of the instance message types */
  static enum IM_TYPE {
    GTALK("Gtalk"), MSN("Msn"), SKYPE("Skype"), YAHOO("Yahoo"), OTHER("Other");
    private String name;
    private IM_TYPE(String name){
      this.name = name;
    }
    public String getName() {
      return name;
    }
  }

  /** Types of updating of profile. */
  public static enum                UpdateType 
                                      {
                                        POSITION,
                                        BASIC_INFOR,
                                        CONTACT,
                                        EXPERIENCES,
                                        AVATAR
                                      };
                                      
  public static enum                AttachedActivityType
                                      {
                                        USER("userProfileActivityId"),
                                        SPACE("spaceProfileActivityId"),
                                        RELATION("relationActivityId"),
                                        RELATIONSHIP("relationShipActivityId");
                                        
                                        private String type;
                                        private AttachedActivityType(String type) {
                                          this.type = type;
                                        }
                                        public String value() {
                                          return this.type;
                                        }
                                        public void setActivityId(ActivityProfileEntity entity, String activityId) {
                                          switch (this) {
                                            case USER: {
                                              entity.setUserProfileActivityId(activityId);
                                              break;
                                            }
                                            case SPACE: {
                                              entity.setSpaceProfileActivityId(activityId);
                                              break;
                                            }
                                            case RELATION: {
                                              entity.setRelationActivityId(activityId);
                                              break;
                                            }
                                            case RELATIONSHIP: {
                                              entity.setRelationShipActivityId(activityId);
                                              break;
                                            }
                                            default :
                                              break;
                                          }
                                        }
                                        public String getActivityId(ActivityProfileEntity entity) {
                                          switch (this) {
                                          case USER: {
                                            return entity.getUserProfileActivityId();
                                          }
                                          case SPACE: {
                                            return entity.getSpaceProfileActivityId();
                                          }
                                          case RELATION: {
                                            return entity.getRelationActivityId();
                                          }
                                          case RELATIONSHIP: {
                                            return entity.getRelationShipActivityId();
                                          }
                                          default : {
                                            return null;
                                          }
                                        }
                                        }
                                      };
  
                                      
  /** The properties. */
  private final Map<String, Object> properties     = new HashMap<String, Object>();

  private static final Map<UpdateType, String[]> updateTypes = new HashMap<UpdateType, String[]>();
  static {
    updateTypes.put(UpdateType.POSITION, new String[] {POSITION});
    updateTypes.put(UpdateType.BASIC_INFOR, new String[] {FIRST_NAME, LAST_NAME, EMAIL});
    updateTypes.put(UpdateType.CONTACT, new String[] {GENDER, CONTACT_PHONES, CONTACT_IMS, CONTACT_URLS});
    updateTypes.put(UpdateType.EXPERIENCES, new String[] {EXPERIENCES});
    updateTypes.put(UpdateType.AVATAR, new String[] {AVATAR});
  }

  /** The identity. */
  private final Identity            identity;
  
  /** The contact phone object*/
  private ContactPhone PHONE;
  
  /** The contact URLs object*/
  private ContactURLs URLs;
  
  /** The contact image object*/
  private ContactInstanceMessage IM;

  /** The id. */
  private String                    id;

  /** The last loaded time */
  private long                      lastLoaded;

  /** Indicates whether or not the profile has been modified locally */
  private boolean                   hasChanged;

  /** Indicates the type of profile are being modified locally */
  private UpdateType                updateType;

  /** Profile url, this will never be stored */
  private String                    url;

  /** Profile url, this will never be stored */
  private String                    avatarUrl;

  private AttachedActivityType      attachedActivityType;
  
  /** Profile created time **/
  private long                      createdTime;

  /**
   * Instantiates a new profile.
   *
   * @param identity the identity
   */
  public Profile(final Identity identity) {
    this.identity = identity;
    PHONE = new ContactPhone(this);
    URLs = new ContactURLs(this);
    IM = new ContactInstanceMessage(this);
  }
  
  /**
   * Populate the contact phones of profile 
   */
  class ContactPhone {
    
    private Profile profile;
    
    private List<String> phoneList;
    
    public ContactPhone(Profile profile) {
      this.profile = profile; 
    }
  
    /** 
     * Add one Phone number with its phone type.
     * @param phoneType Type of phone (home, work or other)
     * @param phoneNumber Phone number
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void addPhone(String phoneType, String phoneNumber) throws Exception {
      if(phoneType != null && phoneNumber != null) {
        List<Map<String, String>> mapPhones = (List<Map<String, String>>) profile.getProperty(CONTACT_PHONES);
        boolean isExist = false;
        if(mapPhones == null){
          mapPhones = new ArrayList<Map<String,String>>();
        } else {
          for(Map<String, String> phoneMap : mapPhones){
            if(phoneType.equals(phoneMap.get(KEY))) {
              if(phoneNumber.equals(phoneMap.get(VALUE))) isExist = true;
            }
          }
        }
        if(!isExist) {
          Map<String, String> map = new HashMap<String, String>();
          map.put(KEY, phoneType);
          map.put(VALUE, phoneNumber);
          mapPhones.add(map);
          //set to profile
          profile.setProperty(CONTACT_PHONES, mapPhones);
        }
      }
    }
    
    /**
     * Get Phone by the phone type.
     * @param phoneType Type of phone to get
     * @return list of phones
     * @throws Exception
     */
    private List<String> getPhones(String phoneType) throws Exception {
      //
      List<String> phones = new ArrayList<String>();
      if(profile.contains(CONTACT_PHONES)){
        phones = profile.getData(phoneType, CONTACT_PHONES);
      }
      return phones;
    }
    
    /** Get the home phones. */
    private List<String> getHomePhones() throws Exception {
      phoneList = getPhones(PHONE_TYPE.HOME.getName());
      return phoneList;
    }
    
    /** Get the work phones. */
    private List<String> getWorkPhones() throws Exception {
      phoneList = getPhones(PHONE_TYPE.WORK.getName());
      return phoneList;
    }

    /** Get the other phones. */
    private List<String> getOtherPhones() throws Exception {
      phoneList = getPhones(PHONE_TYPE.OTHER.getName());
      return phoneList;
    }
    
    /** 
     * Get the phone at specific index. 
     * @return phone
     */
    public String at(int index) throws Exception {
      return this.phoneList.get(index);
    }

    /** 
     * Get all of the phone list
     * @return list of phone
     */
    public List<String> all() throws Exception {
      return this.phoneList;
    }
    
  }

  /**
   * Populate the contact URLs of profile 
   */
  class ContactURLs {
    
    private Profile profile;
    
    public ContactURLs(Profile profile) {
      this.profile = profile;
    }
    
    /** 
     * Add the contact URL to Profile.
     * @param url
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void addURL(String url) throws Exception {
      if(url != null) {
        List<Map<String, String>> mapUrls = (List<Map<String, String>>) profile.getProperty(CONTACT_URLS);
        boolean isExist = false;
        if(mapUrls == null){
          mapUrls = new ArrayList<Map<String,String>>();
        } else {
          for(Map<String, String> urlMap : mapUrls){
            if(url.equals(urlMap.get(VALUE))) isExist = true;
          }
        }
        if(!isExist){
          Map<String, String> map = new HashMap<String, String>();
          map.put(KEY, URL_POSTFIX.toLowerCase());
          map.put(VALUE, url);
          mapUrls.add(map);
          
          //set to profile
          profile.setProperty(CONTACT_URLS, mapUrls);
        }
      }
    }
    
    /** Get the list of contact's URL from Profile. */
    public List<String> getURLs() throws Exception {
      //
      List<String> urls = new ArrayList<String>();
      if(profile.contains(CONTACT_URLS)) {
        urls = profile.getData(CONTACT_URLS);
      }
      return urls;
    }
    
  }

  /**
   * Populate the contact images of profile 
   */
  class ContactInstanceMessage {
    
    private Profile profile;
    
    public ContactInstanceMessage(Profile profile) {
      this.profile = profile;
    }
    
    /**
     * Add one contact's IM to profile.
     * @param imgType
     * @param account
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void addIM(String imgType, String account) throws Exception{
      if(imgType != null && account != null){
        List<Map<String, String>> mapIms = (List<Map<String, String>>) profile.getProperty(CONTACT_IMS);
        boolean isExist = false;
        if(mapIms == null){
          mapIms = new ArrayList<Map<String,String>>();
        } else {
          for(Map<String, String> imMap : mapIms){
            if(imgType.equals(imMap.get(KEY))){
              if(account.equals(imMap.get(VALUE))) isExist = true;
            }
          }
        }
        if(!isExist){
          Map<String, String> map = new HashMap<String, String>();
          map.put(KEY, imgType);
          map.put(VALUE, account);
          mapIms.add(map);
          
          //set to profile
          profile.setProperty(CONTACT_IMS, mapIms);
        }
      }
    }
    
    /** Get IMs from profile. */
    public List<String> getIMs(String imType) throws Exception {
      //
      List<String> images = new ArrayList<String>();
      if(profile.contains(CONTACT_IMS)){
        images = profile.getData(imType, CONTACT_IMS);
      }
      return images;
    }
    
  }
  
  /**
   * Get data from list of hash map with type of parameter
   * @param paramType Type of parameter
   * @param property The property name 
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private List<String> getData(String paramType, String property) throws Exception {
    if (paramType == null) return getData(property);
     //
     List<Map<String, String>> listOfMap = (List<Map<String, String>>) getProperty(property);
     if (listOfMap == null) return Collections.emptyList();
     //
     List<String> result = new ArrayList<String>(); 
     for(Map<String, String> mapInfo : listOfMap) {
       if (mapInfo == null) continue;
       if(paramType.equals(mapInfo.get(KEY))){
         result.add(mapInfo.get(VALUE));
       }
     }
     return result;
   }
  
  /**
   * Get data from list of hash map without type of parameter
   * @param property The property name
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private List<String> getData(String property) throws Exception {
    if (property == null) return Collections.emptyList();
     //
     List<Map<String, String>> listOfMap = (List<Map<String, String>>) getProperty(property);
     if (listOfMap == null) return Collections.emptyList();
     //
     List<String> result = new ArrayList<String>();
     for(Map<String, String> mapInfo : listOfMap) {
       if (mapInfo == null) continue;
       result.add(mapInfo.get(VALUE));
     }
     return result;
   }
  
  /**
   * Gets the identity.
   *
   * @return the identity
   */
  public final Identity getIdentity() {
    return identity;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public final String getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public final void setId(final String id) {
    this.id = id;
  }

  /**
   * Gets the last loaded time.
   *
   * @return the last loaded time
   */
  public final long getLastLoaded() {
    return lastLoaded;
  }

  /**
   * Sets the last loaded time.
   *
   * @param lastLoaded the new last loaded time
   */
  public final void setLastLoaded(final long lastLoaded) {
    this.lastLoaded = lastLoaded;
  }

  /**
   * Indicates whether or not the profile has been modified locally.
   *
   * @return <code>true</code> if it has been modified locally, <code>false</code> otherwise.
   */
  public final boolean hasChanged() {
    return hasChanged;
  }

  /**
   * Clear the has changed flag.
   */
  public final void clearHasChanged() {
     setHasChanged(false);
  }

  /**
   * Gets type of update.
   * @return the updated type for a profile
   * @since 1.2.0-GA
   */
  public UpdateType getUpdateType() {
    return updateType;
  }

  public AttachedActivityType getAttachedActivityType() {
    return attachedActivityType;
  }

  public void setAttachedActivityType(AttachedActivityType attachedActivityType) {
    this.attachedActivityType = attachedActivityType;
  }

  /**
   * Sets type of update.
   * 
   * @param updateType
   * @since 1.2.0-GA
   */
  protected void setUpdateType(String updateType) {
    for (UpdateType key : updateTypes.keySet()) {
      String[] updateTypeValues = updateTypes.get(key);
      for (String value : updateTypeValues) {
        if(value.equals(updateType)) {
          this.updateType = key;
          break;
        }
      }
    }
  }

  /**
   * Sets the value of the property <code>hasChanged<code>.
   *
   * @param hasChanged the new hasChanged
   */
  private void setHasChanged(final boolean hasChanged) {
    this.hasChanged = hasChanged;
  }

  /**
   * Gets the property.
   *
   * @param name the name
   * @return the property
   */
  public final Object getProperty(final String name) {

    // TODO : remove with Profile.URL
    if (URL.equals(name)) {
      return this.url;
    }

    // TODO : remove with Profile.AVATAR_URL
    if (AVATAR_URL.equals(name)) {
      return this.avatarUrl;
    }

    return properties.get(name);
  }

  /**
   * Sets the property.
   *
   * @param name the name
   * @param value the value
   */
  public final void setProperty(final String name, final Object value) {

    // TODO : remove with Profile.URL
    if (URL.equals(name)) {
      this.url = value.toString();
      return;
    }

    // TODO : remove with Profile.AVATAR_URL
    if (AVATAR_URL.equals(name)) {
      this.avatarUrl = value.toString();
      return;
    }

    properties.put(name, value);
    setHasChanged(true);
    setUpdateType(name);

  }

  /**
   * Contains.
   *
   * @param name the name
   * @return true, if successful
   */
  public final boolean contains(final String name) {
    return properties.containsKey(name);
  }

  /**
   * Gets the properties.
   *
   * @return the properties
   */
  public final Map<String, Object> getProperties() {
    return properties;
  }

  /**
   * Removes the property.
   *
   * @param name the name
   */
  public final void removeProperty(final String name) {
    properties.remove(name);
    setHasChanged(true);
  }

  /**
   * Gets the property value.
   *
   * @param name the name
   * @return the property value
   * @deprecated use {@link #getProperty(String)}. Will be removed at 1.3.x
   * @return
   */
  public final Object getPropertyValue(final String name) {
    return getProperty(name);
  }

  /**
   * Gets the full name.
   *
   * @return the full name
   */
  public final String getFullName() {
    String first = (String) getProperty(FIRST_NAME);
    String last = (String) getProperty(LAST_NAME);
    String fullName = getProperty(FULL_NAME) != null ? (String) getProperty(FULL_NAME) : "";
    String all = (first != null) ? first : "";
    all += (last != null) ? " " + last : "";
    return all.length() > 0 ? all : fullName;
  }

  /**
   * Get this profile URL
   * 
   * @return this profile URL
   */
  public final String getUrl() {
    return url;
  }

  /**
   * Set this profile URL
   */
  public void setUrl(final String url) {
    this.url = url;
  }


  /**
   * Gets email address of this profile.
   * 
   * @return email in String format
   */
  public final String getEmail() {
    return (String) getProperty(EMAIL);
  }
  
  /**
   * Add or modify properties of the profile
   * 
   * @param props
   */
  public final void addOrModifyProperties(final Map<String, Object> props) {
    Iterator<Map.Entry<String, Object>> it = props.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Object> entry = it.next();
      String key = entry.getKey();
      // we skip all the property that are jcr related
      if (key.contains(":")) {
        continue;
      }
      setProperty(key, entry.getValue());
    }
    setHasChanged(true);
  }

  /**
   * Gets avatar url
   * 
   * @return avatar image source
   * @deprecated use {@link #getAvatarUrl()}. Will be removed at 1.3.x
   */
  @Deprecated
  public final String getAvatarImageSource() {
    return getAvatarUrl();
  }

  /**
   * Gets avatar url
   * 
   * @return avatar image source
   * @since 1.2.0-GA
   */
  public final String getAvatarUrl() {
    return avatarUrl;
  }

  /**
   * Sets avatar url
   *
   * @since 1.2.0-GA
   */
  public void setAvatarUrl(final String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  /**
   * Gets position
   * 
   * @return position
   * @since 1.2.0-GA
   */
  public final String getPosition() {
    return (String) getProperty(Profile.POSITION);
  }
  
  /**
   * Gets gender
   * 
   * @return gender of user
   * @since 4.0.0.Alpha1
   */
  public final String getGender() {
    return (String) getProperty(Profile.GENDER);
  }
  
  /**
   * Gets Phones
   * 
   * @return list of user's phone numbers
   * @since 4.0.0.Alpha1
   */
  @SuppressWarnings("unchecked")
  public final List<Map<String, String>> getPhones() {
    return (List<Map<String, String>>) getProperty(Profile.CONTACT_PHONES);
  }

  public long getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Long createdTime) {
    if (createdTime != null) {
      this.createdTime = createdTime;
    } else {
      this.createdTime = System.currentTimeMillis();
    }
  }
  
  /**
   * Set the phoneList by work phones.
   * @throws Exception
   */
  public Profile workPhones() throws Exception {
    this.PHONE.getWorkPhones();
    return this;
  }
  
  /**
   * Set the phoneList by home phones.
   * @throws Exception
   */
  public Profile homePhones() throws Exception {
    this.PHONE.getHomePhones();
    return this;
  }
  
  /** Set the phoneList by other phones. */
  public Profile otherPhones() throws Exception {
    this.PHONE.getOtherPhones();
    return this;
  }
  
  /** 
   * Get the phone at specific index. 
   * This method just only use after the workPhones()/homePhones()/otherPhones() function 
   * @return phone
   */
  public String at(int index) throws Exception {
    return this.PHONE.at(index);
  }

  /** 
   * Get all of the phone list.
   * This method just only use after homePhones()/workPhones()/otherPhones() function.
   * @return list of phone
   */
  public List<String> all() throws Exception {
    return this.PHONE.all();
  }

  /**
   * Add IM to profile
   * @param imType Instance Message type
   * @param account Account of IM
   * @throws Exception
   */
  public void addIM(String imType, String account) throws Exception {
    this.IM.addIM(imType, account);
  }

  /**
   * Get IM by IM type
   * @param imType
   * @throws Exception
   */
  public List<String> getIMs(String imType) throws Exception {
    return this.IM.getIMs(imType);
  }
  
  /**
   * Add phone to profile
   * @param phoneType The phone type (Home, Work or Other)
   * @param phoneNumber Phone number
   * @throws Exception
   */
  public void addPhone(String phoneType, String phoneNumber) throws Exception {
    this.PHONE.addPhone(phoneType, phoneNumber);
  }
  
  /**
   * Add URL to profile
   * @param url
   * @throws Exception
   */
  public void addURL(String url) throws Exception {
    this.URLs.addURL(url);
  }
  
  /** Get URLs from ContactURLs. */
  public List<String> getURLs() throws Exception {
    return this.URLs.getURLs();
  }

  /*
     * Get uuid, identity, properties of profile
     * @see java.lang.Object#toString()
     */
  @Override
  public final String toString() {
    return "[uuid : " + id + " identity : " + identity.getId() + " properties: " + properties;
  }
}
