package com.boylegu.springboot_vue.LeanCloud;

import com.avos.avoscloud.AVObject;

/**
 * Created by zhuenqing on 2018/1/6.
 */
//public class Table1 extends AVObject {
public class Table1  {
    String name;
    String phone;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
