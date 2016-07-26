package com.campusconnect.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarthak on 7/17/16.
 */
public class ModelNotificationList {

    private String total;
    private List<ModelNotification> notificationList = new ArrayList<>();

    /**
     *
     * @return
     * The total
     */
    public String getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     *
     * @return
     * The notificationList
     */
    public List<ModelNotification> getNotificationList() {
        return notificationList;
    }

    /**
     *
     * @param notificationList
     * The notificationList
     */
    public void setNotificationList(List<ModelNotification> notificationList) {
        this.notificationList = notificationList;
    }

}