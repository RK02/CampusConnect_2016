package com.campusconnect.cc_reboot.POJO;

/**
 * Created by Sumit Arora on 15-Jul-16.
 */
public class ModelStudent {

    String profileName;
    String isAdmin;
    String photoUrl;
    String profileId;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public ModelStudent() {

    }

    public ModelStudent(String profileName, String isAdmin, String photoUrl, String profileId) {

        this.profileName = profileName;
        this.isAdmin = isAdmin;
        this.photoUrl = photoUrl;
        this.profileId = profileId;
    }
}