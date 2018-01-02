package com.boylegu.springboot_vue.service;

import cn.leancloud.LeanEngine;
import com.avos.avoscloud.*;
import com.sun.tools.javac.comp.Todo;
import groovy.lang.Singleton;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LeanCloud {
    static final String appId = "Eul6rG90rOjIO5imP853JOmn-gzGzoHsz";
    static final String appKey = "XdmDTh1MQGHCYrJjp1B5Jyh1";
    static final String appMasterKey ="8f51dePoE2N9xItvT0jp5jHB";


    private LeanCloud(){

        // 参数依次为 AppId、AppKey、MasterKey 初始化AVOSCloud，请保证在整个项目中间只初始化一次
        AVOSCloud.initialize(appId,appKey,appMasterKey);
        // 打开 debug 日志
        AVOSCloud.setDebugLogEnabled(true);

    }

    private static LeanCloud instance;
    public static synchronized LeanCloud getInstance() {
        if (instance == null) {
            instance = new LeanCloud();
        }
            return instance;
    }
    public boolean creatTable(String tableName){
        // 构造方法传入的参数，对应的就是控制台中的 Class Name
        AVObject todo = new AVObject(tableName);
        return true;
    }

    public static List<Todo> getNotes( int offset) throws AVException {
        AVQuery<Todo> query = AVObject.getQuery(Todo.class);
        query.orderByDescending("createdAt");
        query.include("createdAt");
        query.skip(offset);
        try {
            return query.find();
        } catch (AVException e) {
            if (e.getCode() == 101) {
                // 该错误的信息为：{ code: 101, message: 'Class or object doesn\'t exists.' }，说明 Todo 数据表还未创建，所以返回空的
                // Todo 列表。
                // 具体的错误代码详见：https://leancloud.cn/docs/error_code.html
                return new ArrayList<>();
            }
            throw e;
        }
    }

    protected void doGet(String offsetParam)
            throws  IOException {
//        String offsetParam = req.getParameter("offset");
        int offset = 0;
        if (!AVUtils.isBlankString(offsetParam)) {
            offset = Integer.parseInt(offsetParam);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("offset", offset);
        try {
            List<Todo> data = AVCloud.rpcFunction("list", params);
//            req.setAttribute("todos", data);

        } catch (AVException e) {
            e.printStackTrace();
        }
//        req.getRequestDispatcher("/todos.jsp").forward(req, resp);
    }


    protected void doPost(String content)
            throws  IOException {
//        String content = req.getParameter("content");

        try {
            AVObject note = new Todo();
            note.put("content", content);
            note.save();
        } catch (AVException e) {
            e.printStackTrace();
        }
//        resp.sendRedirect("/todos");
    }

}
