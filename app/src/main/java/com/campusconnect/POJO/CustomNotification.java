package com.campusconnect.POJO;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by sarthak on 7/12/16.
 */
public class CustomNotification extends SugarRecord {
    private String messageBody;
    private String title;
    private String type;

    @Unique
    private String messageId;


    public CustomNotification(){

    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
