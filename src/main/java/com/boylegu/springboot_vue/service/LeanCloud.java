package com.boylegu.springboot_vue.service;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    public boolean creatTable(String tableName) throws AVException {
        // 构造方法传入的参数，对应的就是控制台中的 Class Name
        AVObject testObject = new AVObject(tableName);
        testObject.put("words","init");
        testObject.save();

        return true;
    }



    public List<AVObject> doGet(int limt, String table)
            throws IOException, AVException {
        try {
            AVQuery<AVObject> query = new AVQuery<>(table);
            Date now = new Date();
            query.whereLessThanOrEqualTo("createdAt", now);//查询今天之前创建的 Todo
            query.limit(limt);// 最多返回 10 条结果

            List<AVObject> list = query.find();
            return list;
        }catch (AVException e) {
            e.printStackTrace();
        }

        return null;

    }

    public AVObject doGetObjectById( String objectId , String table)
            throws IOException, AVException {
        try {
//            String objectId = "558e20cbe4b060308e3eb36c";
            AVQuery<AVObject> avQuery = new AVQuery<>(table);
            AVObject object = avQuery.get(objectId);

            return object;
        }catch (AVException e) {
            e.printStackTrace();
        }

        return null;

    }


public void doPost(String content, String table)
            throws IOException {
        try {
            AVObject todoFolder = new AVObject(table);// 构建对象
            todoFolder.put("name", "工作");// 设置名称
            todoFolder.put("priority", 1);// 设置优先级
            todoFolder.save();// 保存到服务端

        } catch (AVException e) {
            e.printStackTrace();
        }
//        resp.sendRedirect("/todos");
    }

}
