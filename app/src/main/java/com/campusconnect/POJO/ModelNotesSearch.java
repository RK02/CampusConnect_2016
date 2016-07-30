package com.campusconnect.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarthak on 7/4/16.
 */
public class ModelNotesSearch {

    private List<NoteBookList> noteBookList = new ArrayList<>();
    private String response;
    private String description;
    private String kind;
    private String etag;

    /**
     *
     * @return
     * The noteBookList
     */
    public List<NoteBookList> getNoteBookList() {
        return noteBookList;
    }

    /**
     *
     * @param noteBookList
     * The noteBookList
     */
    public void setNoteBookList(List<NoteBookList> noteBookList) {
        this.noteBookList = noteBookList;
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
