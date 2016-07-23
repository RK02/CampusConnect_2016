package com.campusconnect.POJO;

import java.util.List;

/**
 * Created by Sumit Arora on 15-Jul-16.
 */
public class ModelStudentList {


    String isAdmin;
    String response;
    List<ModelStudent> studentList;
    String description;
    String kind;
    String etag;

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<ModelStudent> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<ModelStudent> studentList) {
        this.studentList = studentList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public ModelStudentList() {

    }

    public ModelStudentList(String isAdmin, String response, List<ModelStudent> studentList, String description, String kind, String etag) {

        this.isAdmin = isAdmin;
        this.response = response;
        this.studentList = studentList;
        this.description = description;
        this.kind = kind;
        this.etag = etag;
    }
}
