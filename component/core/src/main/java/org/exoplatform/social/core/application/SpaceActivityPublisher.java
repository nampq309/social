/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
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
package org.exoplatform.social.core.application;

import java.util.LinkedHashMap;
import java.util.Map;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.BaseActivityProcessorPlugin;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.activity.model.ExoSocialActivityImpl;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.SpaceListenerPlugin;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent;
import org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent.Type;
import org.exoplatform.social.core.storage.api.IdentityStorage;
import org.exoplatform.social.core.storage.api.SpaceStorage;

/**
 * This listener is responsible for initializing and notifying activity stream for the space. We create a special
 * opensocial user (with a group provider) ready to receive new activities.
 *
 * @author <a href="mailto:patrice.lamarque@exoplatform.com">Patrice Lamarque</a>
 * @version $Revision$
 */
public class SpaceActivityPublisher extends SpaceListenerPlugin {

  /**
   * The SPACE_DISPLAY_NAME_PARAM template param key
   * @since 1.2.8
   */
  public static final String SPACE_DISPLAY_NAME_PARAM = "SPACE_DISPLAY_NAME_PARAM";

  public static final String NUMBER_OF_PUBLIC_SPACE = "NUMBER_OF_PUBLIC_SPACE";

  public static final String USER_JOINED_PUBLIC_SPACE_TITLE_ID = "user_joined_public_space";
  
  public static final String USER_JOINED_PUBLIC_SPACES_TITLE_ID = "user_joined_public_spaces";

  /**
   * The USER_NAME_PARAM template param key
   * @since 1.2.8
   */
  public static final String USER_NAME_PARAM = "USER_NAME_PARAM";
  
  public static final String SPACE_DESCRIPTION_PARAM = "SPACE_DESCRIPTION_PARAM";
  
  public static final String SPACE_APP_ID = "exosocial:spaces";
  
  public static final String SPACE_PROFILE_ACTIVITY = "SPACE_ACTIVITY";
  
  public static final String USER_ACTIVITIES_FOR_SPACE = "USER_ACTIVITIES_FOR_SPACE";
  
  public static final String SPACE_CREATED_TITLE_ID = "space_created";
  
  public static final String MANAGER_GRANTED_TITLE_ID = "manager_role_granted";
  
  public static final String USER_JOINED_TITLE_ID = "user_joined";
  
  public static final String USER_SPACE_JOINED_TITLE_ID = "user_space_joined";
  
  public static final String MEMBER_LEFT_TITLE_ID = "member_left";
  
  public static final String MANAGER_REVOKED_TITLE_ID = "manager_role_revoked";
  
  public static final String SPACE_RENAMED_TITLE_ID = "space_renamed";
  
  public static final String SPACE_DESCRIPTION_EDITED_TITLE_ID = "space_description_edited";
  
  public static final String SPACE_AVATAR_EDITED_TITLE_ID = "space_avatar_edited";
  
  /**
   * The Logger.
   */
  private static final Log LOG = ExoLogger.getExoLogger(SpaceActivityPublisher.class);

  /**
   * activity manager for posting activities.
   */
  private ActivityManager activityManager;

  /**
   * identity manager for getting identities.
   */
  private IdentityManager identityManager;

  /**
   * Constructor.
   *
   * @param params the initial params
   * @param activityManager the activity manager
   * @param identityManager the identity manager
   */
  public SpaceActivityPublisher(final InitParams params,
                                final ActivityManager activityManager,
                                final IdentityManager identityManager) {
    this.activityManager = activityManager;
    this.identityManager = identityManager;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void spaceCreated(SpaceLifeCycleEvent event) {
    Space space = event.getSpace();
    final String activityMessage = space.getDisplayName() + " was created by @" + event.getTarget() + " .";
    Map<String, String> templateParams = new LinkedHashMap<String, String>();
    templateParams.put(SPACE_DISPLAY_NAME_PARAM, space.getDisplayName());
    templateParams.put(USER_NAME_PARAM, "@" + event.getTarget());
    templateParams.put(BaseActivityProcessorPlugin.TEMPLATE_PARAM_TO_PROCESS, USER_NAME_PARAM);
    recordActivity(event, activityMessage, SPACE_CREATED_TITLE_ID, templateParams);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void spaceRemoved(SpaceLifeCycleEvent event) {
    LOG.debug("space " + event.getSpace().getDisplayName() + " was removed!");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applicationActivated(SpaceLifeCycleEvent event) {
    LOG.debug("application <strong>" + event.getTarget() + "</strong> was activated in space "
            + event.getSpace().getDisplayName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applicationAdded(SpaceLifeCycleEvent event) {
    LOG.debug("application <strong>" + event.getTarget() + "</strong> was added in space "
            + event.getSpace().getDisplayName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applicationDeactivated(SpaceLifeCycleEvent event) {
    LOG.debug("application " + event.getTarget() + " was deactivated in space "
            + event.getSpace().getDisplayName());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applicationRemoved(SpaceLifeCycleEvent event) {
    LOG.debug("application " + event.getTarget() + " was removed in space "
            + event.getSpace().getDisplayName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void grantedLead(SpaceLifeCycleEvent event) {
    Space space = event.getSpace();
    final String activityMessage = "@" + event.getTarget() + " has been promoted as space's manager.";
    Map<String, String> templateParams = new LinkedHashMap<String, String>();
    templateParams.put(USER_NAME_PARAM, "@" + event.getTarget());
    templateParams.put(BaseActivityProcessorPlugin.TEMPLATE_PARAM_TO_PROCESS, USER_NAME_PARAM);
    
    recordActivity(new SpaceLifeCycleEvent(space, space.getEditor(), Type.GRANTED_LEAD), activityMessage, MANAGER_GRANTED_TITLE_ID, templateParams);
    LOG.debug("user " + event.getTarget() + " has been promoted as space's manager " + space.getDisplayName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void joined(SpaceLifeCycleEvent event) {
    final String activityMessage ="Has joined the space.";
    final String userSpaceActivityMessage = "I joined " + event.getSpace().getDisplayName() + " space";
    Map<String, String> templateParams = new LinkedHashMap<String, String>();
    
    //
    recordActivityForUserSpace(event, userSpaceActivityMessage, USER_SPACE_JOINED_TITLE_ID, templateParams, true);
    
    //
    recordActivity(event, activityMessage, USER_JOINED_TITLE_ID, templateParams);
    
    LOG.debug("user " + event.getTarget() + " joined space " + event.getSpace().getDisplayName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void left(SpaceLifeCycleEvent event) {
    final String activityMessage = "Has left the space.";
    Map<String, String> templateParams = new LinkedHashMap<String, String>();
    recordActivity(event, activityMessage, MEMBER_LEFT_TITLE_ID, templateParams);
    
    // update user space activity for space
    final String userSpaceActivityMessage = "I left " + event.getSpace().getDisplayName() + " space";
    recordActivityForUserSpace(event, userSpaceActivityMessage, USER_SPACE_JOINED_TITLE_ID, templateParams, false);
    
    LOG.debug("user " + event.getTarget() + " has left of space " + event.getSpace().getDisplayName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void revokedLead(SpaceLifeCycleEvent event) {
    Space space = event.getSpace();
    final String activityMessage = "@" + event.getTarget() + " has been revoked as space's manager.";
    Map<String, String> templateParams = new LinkedHashMap<String, String>();
    templateParams.put(USER_NAME_PARAM, "@" + event.getTarget());
    templateParams.put(BaseActivityProcessorPlugin.TEMPLATE_PARAM_TO_PROCESS, USER_NAME_PARAM);
    recordActivity(new SpaceLifeCycleEvent(space, space.getEditor(), Type.REVOKED_LEAD), activityMessage, MANAGER_REVOKED_TITLE_ID, templateParams);
    LOG.debug("user " + event.getTarget() + " has been revoked as space's manage "
            + event.getSpace().getDisplayName());
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void spaceRenamed(SpaceLifeCycleEvent event) {
    final String activityMessage = "Name has been updated to: "+event.getSpace().getDisplayName();
    Map<String, String> templateParams = new LinkedHashMap<String, String>();
    templateParams.put(SPACE_DISPLAY_NAME_PARAM, event.getSpace().getDisplayName());
    templateParams.put(BaseActivityProcessorPlugin.TEMPLATE_PARAM_TO_PROCESS, SPACE_DISPLAY_NAME_PARAM);
    recordActivity(event, activityMessage, SPACE_RENAMED_TITLE_ID, templateParams);
    LOG.debug("Name has been updated ");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void spaceDescriptionEdited(SpaceLifeCycleEvent event) {
    final String activityMessage = "Description has been updated to: "+event.getSpace().getDescription();
    Map<String, String> templateParams = new LinkedHashMap<String, String>();
    templateParams.put(SPACE_DESCRIPTION_PARAM, event.getSpace().getDescription());
    templateParams.put(BaseActivityProcessorPlugin.TEMPLATE_PARAM_TO_PROCESS, SPACE_DESCRIPTION_PARAM);
    recordActivity(event, activityMessage, SPACE_DESCRIPTION_EDITED_TITLE_ID, templateParams);
    LOG.debug("Description has been updated ");
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void spaceAvatarEdited(SpaceLifeCycleEvent event) {
    final String activityMessage = "Space has a new avatar.";
    Map<String, String> templateParams = new LinkedHashMap<String, String>();
    recordActivity(event, activityMessage, SPACE_AVATAR_EDITED_TITLE_ID, templateParams);
    LOG.debug("Space has a new avatar.");
  }

  /**
   * Records an activity based on space lifecyle event and the activity object.
   *
   * @param event the space lifecyle event
   * @param activityMessage the message of activity object
   * @param titleId the title of activity (comment)
   * @param templateParams 
   */
  private void recordActivity(SpaceLifeCycleEvent event, String activityMessage, String titleId,
                              Map<String, String> templateParams) {
    Space space = event.getSpace();
    Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
    String activityId = getStorage().getProfileActivityId(spaceIdentity.getProfile(), Profile.AttachedActivityType.SPACE);
    ExoSocialActivity activity = new ExoSocialActivityImpl();
    activity.setType(SPACE_PROFILE_ACTIVITY);
    activity.setTitle(activityMessage);
    if (activityId != null) {
      try {
        ExoSocialActivityImpl comment = new ExoSocialActivityImpl();
        comment.setTitle(activityMessage);
        comment.setTitleId(titleId);
        Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, event.getTarget(), false);
        comment.setUserId(identity.getId());
        comment.setType(SPACE_APP_ID);
        comment.setTemplateParams(templateParams);
        activity = (ExoSocialActivityImpl) activityManager.getActivity(activityId);
        activityManager.updateActivity(activity);
        activityManager.saveComment(activity, comment);
      } catch (Exception e) {
        LOG.debug("Run in case of activity deleted and reupdate");
        activityId = null;
      }
    }
    if (activityId == null) {
      activityManager.saveActivityNoReturn(spaceIdentity, activity);
      getStorage().updateProfileActivityId(spaceIdentity, activity.getId(), Profile.AttachedActivityType.SPACE);
    }
  }
  
  /**
   * Records an activity for user space based on space lifecyle event and the activity object.
   *
   * @param event the space life-cycle event
   * @param activityMessage the message of activity object
   * @param titleId the title of activity (comment)
   * @param templateParams 
   */
  private void recordActivityForUserSpace(SpaceLifeCycleEvent event, String userSpaceActivityMessage, String titleId,
                              Map<String, String> templateParams, boolean isJoined) {
    Space space = event.getSpace();
    if (space.getVisibility().equals(Space.HIDDEN)) {
      return;
    }
    Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, event.getTarget(), false);
    String activityId = getStorage().getProfileActivityId(identity.getProfile(), Profile.AttachedActivityType.RELATION);
    
    //
    ExoSocialActivity activity = null;
    if (activityId != null) {
      activity = (ExoSocialActivityImpl) activityManager.getActivity(activityId);
    } else {
      activity = new ExoSocialActivityImpl();  
    }
    
    int numberOfSpacesOfMember = getSpaceStorage().getSpacesOfMemberCount(identity.getRemoteId());
    
    activity.setTitleId(USER_JOINED_PUBLIC_SPACE_TITLE_ID);
    
    if (numberOfSpacesOfMember > 1) {
      activity.setTitle("I now member of " + numberOfSpacesOfMember + " spaces");
      activity.setTitleId(USER_JOINED_PUBLIC_SPACES_TITLE_ID);
    } else {
      activity.setTitle("I now member of " + numberOfSpacesOfMember + " space");
    } 

    activity.setType(USER_ACTIVITIES_FOR_SPACE);
    templateParams.put(NUMBER_OF_PUBLIC_SPACE, String.valueOf(numberOfSpacesOfMember));
    templateParams.put(BaseActivityProcessorPlugin.TEMPLATE_PARAM_TO_PROCESS, NUMBER_OF_PUBLIC_SPACE);
    activity.setTemplateParams(templateParams);
    
    if (activityId != null) {
      if (isJoined) {
        try {
          ExoSocialActivityImpl comment = new ExoSocialActivityImpl();
          comment.setTitle(userSpaceActivityMessage);
          comment.setTitleId(titleId);
          comment.setUserId(identity.getId());
          comment.setType(SPACE_APP_ID);
          templateParams.put(SPACE_DISPLAY_NAME_PARAM, event.getSpace().getDisplayName());
          templateParams.put(BaseActivityProcessorPlugin.TEMPLATE_PARAM_TO_PROCESS, SPACE_DISPLAY_NAME_PARAM);
          comment.setTemplateParams(templateParams);
          activityManager.updateActivity(activity);
          activityManager.saveComment(activity, comment);
        } catch (Exception e) {
          LOG.debug("Run in case of activity deleted");
          activityId = null;
        }
      } else { // for Spec then left space have no affect on comments
        //
        activityManager.updateActivity(activity);
      }
    }
    
    //
    if (activityId == null) {
      activityManager.saveActivityNoReturn(identity, activity);
      getStorage().updateProfileActivityId(identity, activity.getId(), Profile.AttachedActivityType.RELATION);
    }
  }
  
  private IdentityStorage getStorage() {
    return (IdentityStorage) PortalContainer.getInstance().getComponentInstanceOfType(IdentityStorage.class);
  }

  private SpaceStorage getSpaceStorage() {
    return (SpaceStorage) PortalContainer.getInstance().getComponentInstanceOfType(SpaceStorage.class);
  }

}
