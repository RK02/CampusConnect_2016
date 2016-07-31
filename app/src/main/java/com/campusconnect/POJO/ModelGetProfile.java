package com.campusconnect.POJO;

/**
 * Created by sarthak on 7/31/16.
 */

/*
.putString("profileId", profileId)
                           .putString("collegeId", collegeId)
                           .putString("personId", personId)
                           .putString("collegeName", collegeName.getText().toString())
                           .putString("batchName", batchName.getText().toString())
                           .putString("branchName", branchName.getText().toString())
                           .putString("sectionName", sectionName.getText().toString())
                           .putString("profileName", personName)
                           .putString("email", personEmail)
                           .putString("photourl", personPhoto)
 */
public class ModelGetProfile {
    private String response;
    private String profileId;
    private String collegeId;
    private String collegeName;
    private String batchName;
    private String branchName;
    private String sectionName;
    private String photourl;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
