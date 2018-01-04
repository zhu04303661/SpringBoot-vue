package com.boylegu.springboot_vue.service;


import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.HashMap;
import java.util.Map;

@AVClassName("Todo")
public class Todo extends AVObject {

    public Todo() {
        super();
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        this.put("content", content);
    }

    @Override
    public String toString() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("content", this.getString("content"));
        result.put("objectId", this.getObjectId());
        result.put("createdAt", this.getCreatedAt());
        return JSON.toJSONString(result);
    }
}
