package com.boylegu.springboot_vue.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.mcdCommon.annotations.ExcelColumn;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
@SuppressWarnings("all")
public class ExcelUtils {
	/**
	 * 创建excel文档，
	 * @param list 数据
	 * @param keys list中map的key数组集合
	 * @param columnNames excel的列名
	 * */
	public static Workbook createWorkBook(List<Map<String, Object>> list,String []keys,String []columnNames) {
		// 创建excel工作簿
		Workbook wb = new HSSFWorkbook();
		addToSheet(wb,"sheet1",list,keys,columnNames);
		return wb;
	}
	public  static <T> Workbook addToSheet(Workbook wb, String sheetName, List<T> list,Class<T> tClass, String []keys, String []columnNames){
		List<Map<String, Object>> mapList=new ArrayList<>();
		Map<String,Field> fieldMap=new HashMap<>();
		for (T t : list) {
			String str= JSON.toJSONString(t);
			JSONObject obj=JSONObject.parseObject(str);
			for (String key : obj.keySet()) {
				if(!fieldMap.containsKey(key)){
					Field f=FieldUtils.getField(tClass,key,true);
					if(f==null){
						continue;
					}
					fieldMap.put(key,f);
				}

				if(fieldMap.containsKey(key)){
					Field f=fieldMap.get(key);
					if(f.getType().equals(Date.class)){
						Date date=obj.getDate(key);
						obj.put(key,date);
					}
				}

			}
			mapList.add(obj);
		}
		return addToSheet(wb,sheetName,mapList,keys,columnNames);
	}
	public static Workbook addToSheet(Workbook wb,String sheetName,List<Map<String, Object>> list,String []keys,String []columnNames) {
		// 创建excel工作簿
		// 创建第一个sheet（页），并命名
		Sheet sheet = wb.createSheet(sheetName);
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		for(int i=0;i<keys.length;i++){
			sheet.setColumnWidth( i, (int) (35.7 * 150));
		}

		// 创建第一行
		Row row = sheet.createRow(0);

		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();

		// 创建两种字体
		Font f = wb.createFont();
		Font f2 = wb.createFont();

		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 10);
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// 创建第二种字体样式（用于值）
		f2.setFontHeightInPoints((short) 10);
		f2.setColor(IndexedColors.BLACK.getIndex());

		//      Font f3=wb.createFont();
		//      f3.setFontHeightInPoints((short) 10);
		//      f3.setColor(IndexedColors.RED.getIndex());

		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);

		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setBorderLeft(CellStyle.BORDER_THIN);
		cs2.setBorderRight(CellStyle.BORDER_THIN);
		cs2.setBorderTop(CellStyle.BORDER_THIN);
		cs2.setBorderBottom(CellStyle.BORDER_THIN);
		cs2.setAlignment(CellStyle.ALIGN_CENTER);
		//设置列名
		for(int i=0;i<columnNames.length;i++){
			Cell cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
			cell.setCellStyle(cs);
		}
		//设置每行每列的值
		for (short i = 0; i < list.size(); i++) {
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			Row row1 = sheet.createRow((short) i+1);
			// 在row行上创建一个方格
			for(short j=0;j<keys.length;j++){
				Cell cell = row1.createCell(j);
				Object value=list.get(i).get(keys[j]);

				cell.setCellValue(value == null?" ": formatValue(value));
				cell.setCellStyle(cs2);
			}
		}
		return wb;
	}

	private static String formatValue(Object value) {
		if(value.getClass().equals(Date.class)){
			return MCDDateUtil.formatDate((Date) value);
		}
		return value.toString();
	}

	public static Workbook createWorkBook(List<Map<String, Object>> data,String []columnNames){
		String []keys=(String[]) data.get(0).keySet().toArray();
		return createWorkBook(data,keys,columnNames);
	}



	public static <T> Workbook createWorkBook(List<T> data){
		List<Map<String, Object>> dataList=new ArrayList<Map<String,Object>>();

		BeanInfo info=null;
		try {
			info = Introspector.getBeanInfo(data.get(0).getClass(),Object.class);
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PropertyDescriptor []fields=info.getPropertyDescriptors();


		String []columnNames=new String[fields.length];
		String []keys=new String[fields.length];
		for (int i=0;i<fields.length;i++) {

			ExcelColumn column=fields[i].getReadMethod().getAnnotation(ExcelColumn.class);
			if(column==null||column.name()==null){
				columnNames[i]=fields[i].getName();
			}else{
				columnNames[i]=column.name();
			}
			keys[i]=fields[i].getName();
		}
		for(T d : data){
			Map<String, Object> dMap=new HashMap<String, Object>();
			for (PropertyDescriptor field : fields) {
				try {
					dMap.put(field.getName(), field.getReadMethod().invoke(d));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dataList.add(dMap);
		}
		return createWorkBook(dataList,keys,columnNames);
	}

	public static File getExcelFile(Workbook workbook){
		File excelFolder=new File("tmp/excel/");
		File excel=new File(excelFolder, new Date().getTime()+UUID.randomUUID().toString()+".xls");
		try {
			FileOutputStream outFile;
			if(!excelFolder.exists()){
				excelFolder.mkdirs();
			}
			if(!excel.exists()){
				excel.createNewFile();
			}

			outFile = new FileOutputStream(excel);
			workbook.write(outFile);
			outFile.close();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return excel;
	}


	public static List<Map<String, Object>> readFromExcel(File excel) throws Exception{
		Workbook book = getWorkbook(excel);
		List<Map<String, Object>> data = readFromSheet(book,0);
		return data;
	}

	public static List<List<Map<String, Object>>> readAllSheetFromExcel(File excel) throws Exception{
		Workbook book = getWorkbook(excel);
		List<List<Map<String, Object>>> data=new ArrayList<>();
		for (int i = 0; i < book.getNumberOfSheets(); i++) {
			List<Map<String, Object>> sheet = readFromSheet(book,i);
			data.add(sheet);
		}
		return data;
	}

	private static Workbook getWorkbook(File excel) throws IOException {
		Workbook book =null;
		InputStream xls=null,xlsx=null;
		try{
			xlsx = new FileInputStream(excel);
			book=new XSSFWorkbook(xlsx);
		}catch (Exception ex){
			xls = new FileInputStream(excel);
			book=new HSSFWorkbook(xls);
		}finally {
			if(xlsx!=null){
				xlsx.close();
			}
			if(xls!=null){
				xls.close();
			}
		}
		return book;
	}

	private static List<Map<String, Object>> readFromSheet(Workbook  book,int index) throws Exception {
		List<Map<String, Object>> data =new ArrayList<Map<String, Object>>();


		Sheet sheet = book.getSheetAt(index);
		int rows = sheet.getPhysicalNumberOfRows();
		Row header=sheet.getRow(0);
		if(header==null){
			return new ArrayList<>();
		}
		List<String> headerKeys=new ArrayList<String>();
		int headerCells = header.getPhysicalNumberOfCells();
		for(int i=0;i<headerCells;i++){
			headerKeys.add(header.getCell(i).getStringCellValue());
		}

		for (int i = 1; i < rows; i++) {
			// 读取左上端单元格
			Row row = sheet.getRow(i);
			// 行不为空
			if (row != null) {
				//获取到Excel文件中的所有的列
//				int cells = row.getPhysicalNumberOfCells();
				int cells = headerKeys.size();
				Map<String, Object> item=new HashMap<String, Object>();
				//遍历列
				for (int j = 0; j < cells; j++) {
					//获取到列的值
					String key=headerKeys.get(j);
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							item.put(key,cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							item.put(key,cell.getNumericCellValue());
							break;
						case Cell.CELL_TYPE_STRING:
							item.put(key,cell.getStringCellValue());
							break;
						default:
							item.put(key,null);
							break;
						}
					}
				}
				data.add(item);
			}
		}
		return data;
	}


	public static <T> List<T> readFromExcel(File excel,Class<?> T) throws Exception{
		List<Map<String, Object>> data=readFromExcel(excel);
		List<T> list=new ArrayList<T>();
		for (Map<String, Object> item : data) {
			String str=MCDJsonUtil.Serialize(item);
			T obj=(T) MCDJsonUtil.Deserialize(str,T );
			list.add(obj);
		}
		return list;
	}
	
	public static void main(String[] args) {

		class TestExcelData{

			String name;

			String data;

			int id;
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getData() {
				return data;
			}
			public void setData(String data) {
				this.data = data;
			}
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}

		};
		List<TestExcelData> data=new ArrayList<TestExcelData>();
		for (int i = 0; i <20; i++) {
			TestExcelData d=new TestExcelData();
			d.setId(i);
			d.setData("Data "+i);
			d.setName("name "+i);
			data.add(d);
		}
		Workbook book=createWorkBook(data);
		FileOutputStream outFile;
		try {
			File excel=new File("C:\\update.xls");
			if(!excel.exists()){
				excel.createNewFile();
			}
			outFile = new FileOutputStream(excel);
			book.write(outFile);
			outFile.close();
			
			List<TestExcelData> inData=readFromExcel(excel,TestExcelData.class);
			String str=MCDJsonUtil.Serialize(inData);
			System.out.println(str);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
