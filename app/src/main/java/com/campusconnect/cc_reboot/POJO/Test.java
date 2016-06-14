package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import java.util.HashMap;
import java.util.Map;

public class Test {

    private String isAuthor;
    private String views;
    private String courseName;
    private String pages;
    private String uploaderName;
    private String lastUpdated;
    private String testDesc;
    private String testId;
    private String testTitle;
    private String dueDate;
    private String dueTime;
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
     * The courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     *
     * @param courseName
     * The courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     *
     * @return
     * The pages
     */
    public String getPages() {
        return pages;
    }

    /**
     *
     * @param pages
     * The pages
     */
    public void setPages(String pages) {
        this.pages = pages;
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
     * The testDesc
     */
    public String getTestDesc() {
        return testDesc;
    }

    /**
     *
     * @param testDesc
     * The testDesc
     */
    public void setTestDesc(String testDesc) {
        this.testDesc = testDesc;
    }

    /**
     *
     * @return
     * The testId
     */
    public String getTestId() {
        return testId;
    }

    /**
     *
     * @param testId
     * The testId
     */
    public void setTestId(String testId) {
        this.testId = testId;
    }

    /**
     *
     * @return
     * The testTitle
     */
    public String getTestTitle() {
        return testTitle;
    }

    /**
     *
     * @param testTitle
     * The testTitle
     */
    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}