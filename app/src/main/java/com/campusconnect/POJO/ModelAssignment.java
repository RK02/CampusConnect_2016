package com.campusconnect.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAssignment {

    private String isAuthor;
    private String description;
    private String views;
    private String uploaderName;
    private String lastUpdated;
    private String dueDate;
    private List<String> urlList = new ArrayList<String>();
    private String assignmentDesc;
    private String assignmentTitle;
    private String response;
    private String dueTime;
    private String kind;
    private String etag;
    private String courseName;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The isAuthor
     */
    public String getIsAuthor() {
        return isAuthor;
    }

    /**
     *
     * @param isAuthor
     * The isAuthor
     */
    public void setIsAuthor(String isAuthor) {
        this.isAuthor = isAuthor;
    }

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
     * The views
     */
    public String getViews() {
        return views;
    }

    /**
     *
     * @param views
     * The views
     */
    public void setViews(String views) {
        this.views = views;
    }

    /**
     *
     * @return
     * The uploaderName
     */
    public String getUploaderName() {
        return uploaderName;
    }

    /**
     *
     * @param uploaderName
     * The uploaderName
     */
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    /**
     *
     * @return
     * The lastUpdated
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     *
     * @param lastUpdated
     * The lastUpdated
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     *
     * @return
     * The dueDate
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     *
     * @param dueDate
     * The dueDate
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     *
     * @return
     * The urlList
     */
    public List<String> getUrlList() {
        return urlList;
    }

    /**
     *
     * @param urlList
     * The urlList
     */
    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    /**
     *
     * @return
     * The assignmentDesc
     */
    public String getAssignmentDesc() {
        return assignmentDesc;
    }

    /**
     *
     * @param assignmentDesc
     * The assignmentDesc
     */
    public void setAssignmentDesc(String assignmentDesc) {
        this.assignmentDesc = assignmentDesc;
    }

    /**
     *
     * @return
     * The assignmentTitle
     */
    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    /**
     *
     * @param assignmentTitle
     * The assignmentTitle
     */
    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
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
     * The dueTime
     */
    public String getDueTime() {
        return dueTime;
    }

    /**
     *
     * @param dueTime
     * The dueTime
     */
    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
