package com.campusconnect.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import java.util.ArrayList;
import java.util.List;

public class ModelTestList {

    private List<ModelTest> examList = new ArrayList<>();
    private String response;
    private String description;
    private String kind;
    private String etag;

    /**
     *
     * @return
     * The examList
     */
    public List<ModelTest> getExamList() {
        return examList;
    }

    /**
     *
     * @param examList
     * The examList
     */
    public void setExamList(List<ModelTest> examList) {
        this.examList = examList;
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

}
