package com.boylegu.springboot_vue.LeanCloud;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import org.springframework.stereotype.Component;

import java.io.IOException;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuenqing on 2018/1/6.
 */

@Component
public class TableProjo {
    public static  final String tableName = "project1";

    public void doPost(Table1 content)
            throws IOException , IllegalArgumentException, IllegalAccessException{

        Field[] fields = content.getClass().getDeclaredFields();

        try {
            AVObject todoFolder = new AVObject(tableName);// 构建对象




            for (int i = 0; i < fields.length; i++) {

                todoFolder.put(fields[i].getName(), fields[i].get(content));// 设置名称

//                System.out.print("成员变量" + i + "类型 : " + fields[i].getType().getName());
//                System.out.print("\t成员变量" + i + "变量名: " + fields[i].getName() + "\t");
//                System.out.println("成员变量" + i + "值: " + fields[i].get(aInstance));
            }

            todoFolder.save();// 保存到服务端


        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public void doPostAll(List<Table1> contentList)
            throws IOException , IllegalArgumentException, IllegalAccessException{



        try {
//            AVObject todoFolder = new AVObject(tableName);// 构建对象

            List<AVObject> todos = new ArrayList<>();


            for(Table1 content:contentList){
                AVObject todo =  new AVObject(tableName);
                Field[] fields = content.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    todo.put(fields[i].getName(), fields[i].get(content));// 设置名称
                }
                todos.add(todo);
            }


            AVObject.saveAll(todos);// 保存到服务端


        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    public List<AVObject> doGet(int limt)
            throws IOException, AVException {
        try {
            AVQuery<AVObject> query = new AVQuery<>(tableName);
//            Date now = new Date();
//            query.whereLessThanOrEqualTo("createdAt", now);//查询今天之前创建的 Todo
            query.limit(limt);// 最多返回 10 条结果

            List<AVObject> list = query.find();
            return list;
        }catch (AVException e) {
            e.printStackTrace();
        }

        return null;

    }
    public  List<Table1> doTableGet() throws IOException, AVException {

        List<Table1> table1s = new ArrayList<>();
        List<AVObject> avObjects = doGet(10);

        for(AVObject avObject : avObjects){
            Table1 table1 = new Table1();
//            Field[] fields = table1.getClass().getDeclaredFields();

            table1.setName(avObject.getString("name"));
            table1.setPhone(avObject.getString("phone"));
            table1.setDescription(avObject.getString("description"));

//            // 获取三个特殊属性
//            String objectId = avObject.getObjectId();
//            Date updatedAt = avObject.getUpdatedAt();
//            Date createdAt = avObject.getCreatedAt();

            table1s.add(table1);
        }
        return  table1s;


    }
}
