package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Example {

    private String description;
    private String photoUrl;
    private List<SubscribedCourseList> subscribedCourseList = new ArrayList<SubscribedCourseList>();
    private List<AvailableCourseList> availableCourseList = new ArrayList<AvailableCourseList>();
    private String points;
    private String profileName;
    private String response;
    private String kind;
    private String etag;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The photoUrl
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     *
     * @param photoUrl
     * The photoUrl
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     *
     * @return
     * The subscribedCourseList
     */
    public List<SubscribedCourseList> getSubscribedCourseList() {
        return subscribedCourseList;
    }

    /**
     *
     * @param subscribedCourseList
     * The subscribedCourseList
     */
    public void setSubscribedCourseList(List<SubscribedCourseList> subscribedCourseList) {
        this.subscribedCourseList = subscribedCourseList;
    }

    /**
     *
     * @return
     * The availableCourseList
     */
    public List<AvailableCourseList> getAvailableCourseList() {
        return availableCourseList;
    }

    /**
     *
     * @param availableCourseList
     * The availableCourseList
     */
    public void setAvailableCourseList(List<AvailableCourseList> availableCourseList) {
        this.availableCourseList = availableCourseList;
    }

    /**
     *
     * @return
     * The points
     */
    public String getPoints() {
        return points;
    }

    /**
     *
     * @param points
     * The points
     */
    public void setPoints(String points) {
        this.points = points;
    }

    /**
     *
     * @return
     * The profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     *
     * @param profileName
     * The profileName
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     *
     * @return
     * The response
     */
    public String getResponse() {
        return response;
    }

    /**
     *
     * @param response
     * The response
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     *
     * @return
     * The kind
     */
    public String getKind() {
        return kind;
    }

    /**
     *
     * @param kind
     * The kind
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     *
     * @return
     * The etag
     */
    public String getEtag() {
        return etag;
    }

    /**
     *
     * @param etag
     * The etag
     */
    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
