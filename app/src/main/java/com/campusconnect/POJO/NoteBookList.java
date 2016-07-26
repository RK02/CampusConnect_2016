package com.campusconnect.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import java.util.HashMap;
import java.util.Map;
public class NoteBookList {

    private String noteBookId;
    private String views;
    private String courseName;
    private String uploaderName;
    private String lastUpdated;
    private String frequency;
    private String totalRating;
    private String pages;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    /**
     *
     * @return
     * The noteBookId
     */
    public String getNoteBookId() {
        return noteBookId;
    }

    /**
     *
     * @param noteBookId
     * The noteBookId
     */
    public void setNoteBookId(String noteBookId) {
        this.noteBookId = noteBookId;
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
     * The frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     *
     * @param frequency
     * The frequency
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     *
     * @return
     * The totalRating
     */
    public String getTotalRating() {
        return totalRating;
    }

    /**
     *
     * @param totalRating
     * The totalRating
     */
    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
