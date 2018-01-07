package com.boylegu.springboot_vue.service;

import com.boylegu.springboot_vue.LeanCloud.Table1;
import com.boylegu.springboot_vue.controller.util.ExcelImportUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ExcelService {
    /**
     * 上传excel文件到临时目录后并开始解析
     * @param fileName
     * @param mfile
     * @return
     */
    public List<Table1> batchImport(String fileName, MultipartFile mfile){

//        File uploadDir = new File("E:\\fileupload");
        File uploadDir = new File("/tmp/fileupload/");
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
//        File tempFile = new File("E:\\fileupload\\" + new Date().getTime() + ".xlsx");
        File tempFile = new File("/tmp/fileupload/" + new Date().getTime() + ".xlsx");
        //初始化输入流
        InputStream is = null;


        List<Table1> tables = new ArrayList<>();

        try{
            //将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);


            List<Map<String, Object>> data= ExcelUtils.readFromExcel(tempFile);
            for(Map<String, Object> item: data){
                Table1 table1 = new Table1();
                table1.setName(item.get("名称").toString());
                table1.setPhone(item.get("电话").toString());
                table1.setDescription(item.get("描叙").toString());

                tables.add(table1);

            }
            return  tables;

        }catch(Exception e) {
            e.printStackTrace();
        }catch(Error er){
            er.printStackTrace();

        } finally{
            if(is !=null)
            {
                try{
                    is.close();
                }catch(IOException e){
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return  tables;
//        return "导入出错！请检查数据格式！";
    }


}
