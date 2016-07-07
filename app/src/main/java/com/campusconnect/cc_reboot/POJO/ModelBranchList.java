package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 7/7/16.
 */
import java.util.ArrayList;
import java.util.List;

public class ModelBranchList {

    private List<String> branchList = new ArrayList<String>();
    private String description;
    private String response;
    private String kind;
    private String etag;

    /**
     *
     * @return
     * The branchList
     */
    public List<String> getBranchList() {
        return branchList;
    }

    /**
     *
     * @param branchList
     * The branchList
     */
    public void setBranchList(List<String> branchList) {
        this.branchList = branchList;
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

}