package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Note {

    private String classNumber;
    private List<String> urlList = new ArrayList<String>();
    private String date;
    private String description;
    private String title;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The classNumber
     */
    public String getClassNumber() {
        return classNumber;
    }

    /**
     *
     * @param classNumber
     * The classNumber
     */
    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
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
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
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
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}


