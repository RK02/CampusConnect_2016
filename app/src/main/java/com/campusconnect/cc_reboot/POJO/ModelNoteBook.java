package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelNoteBook {

    private String isAuthor;
    private String rated;
    private String description;
    private String views;
    private String courseName;
    private List<Note> notes = new ArrayList<>();
    private String uploaderName;
    private String lastUpdated;
    private String response;
    private String frequency;
    private String totalRating;
    private String pages;
    private String kind;
    private String etag;
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
     * The rated
     */
    public String getRated() {
        return rated;
    }

    /**
     *
     * @param rated
     * The rated
     */
    public void setRated(String rated) {
        this.rated = rated;
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
     * The notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     *
     * @param notes
     * The notes
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
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