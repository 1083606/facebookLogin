package com.example.facebooklogin.ui.history;

public class history {
    private String chatroom_id;
    private String user_id;
    private String habbit_name;
    private String habbit_status;
    private String completion;
    private String max_completion;
    private String original_intention;
    private String goodness;
    private String badness;
    private String created_at;
    private String updated_at;

    public history() {
    }

    public history(String chatroom_id, String user_id, String habbit_name, String habbit_status, String completion, String max_completion, String original_intention, String goodness, String badness, String created_at, String updated_at) {
        this.chatroom_id = chatroom_id;
        this.user_id = user_id;
        this.habbit_name = habbit_name;
        this.habbit_status = habbit_status;
        this.completion = completion;
        this.max_completion = max_completion;
        this.original_intention = original_intention;
        this.goodness = goodness;
        this.badness = badness;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    //getter

    public String getChatroom_id() {
        return chatroom_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getHabbit_name() {
        return habbit_name;
    }

    public String getHabbit_status() {
        return habbit_status;
    }

    public String getCompletion() {
        return completion;
    }

    public String getMax_completion() {
        return max_completion;
    }

    public String getOriginal_intention() {
        return original_intention;
    }

    public String getGoodness() {
        return goodness;
    }

    public String getBadness() {
        return badness;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }


    //setter

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setHabbit_name(String habbit_name) {
        this.habbit_name = habbit_name;
    }

    public void setHabbit_status(String habbit_status) {
        this.habbit_status = habbit_status;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public void setMax_completion(String max_completion) {
        this.max_completion = max_completion;
    }

    public void setOriginal_intention(String original_intention) {
        this.original_intention = original_intention;
    }

    public void setGoodness(String goodness) {
        this.goodness = goodness;
    }

    public void setBadness(String badness) {
        this.badness = badness;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
