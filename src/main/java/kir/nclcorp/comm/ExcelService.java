package kir.nclcorp.comm;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    public void insertToExcel(List<Map<String, String>> ApiData) {
        String filePath = "C:/Users/NCL-NT-0164/Desktop/TEST.xlsx";
        File excel = new File(filePath);

        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            XSSFSheet xssfSheet = xssfWorkbook.getSheet("대종로네거리");
            XSSFRow row = xssfSheet.createRow(2);
            XSSFCell cell = row.createCell(2);
            cell.setCellValue("안녕");
            FileOutputStream fos = new FileOutputStream(filePath);
            xssfWorkbook.write(fos);
            fos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
