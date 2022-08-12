package kir.nclcorp.comm;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    public void insertToExcel(Map<String, Integer> ApiData, String date, Integer seq) {
        String filePath = "C:/Users/NCL-NT-0164/Desktop/TEST.xlsx";
        File excel = new File(filePath);
        XSSFSheet sheet;
        int rowIndex;
        int finalIndexrow=0;
        int finalIndexcol=2;
        XSSFCell cell1;
        XSSFCell cell2;
        XSSFCell cell3;
        XSSFCell cell4;
        XSSFCell finalcell;
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            sheet = xssfWorkbook.getSheet("rawdata");
            sheet.autoSizeColumn(1);

            rowIndex = findBlankRowIndex(sheet);
            XSSFRow row;

            FileOutputStream fos = new FileOutputStream(filePath);

            for(String key : ApiData.keySet()) {
                row = sheet.createRow(rowIndex);
                cell1 = row.createCell(1);
                cell2 = row.createCell(2);
                cell3 = row.createCell(3);
                cell4 = row.createCell(4);

                System.out.println("date = " + date + " rowIndex = " + rowIndex + " seq = " + seq + " key = " + key + " ApiData.get(key) = " + ApiData.get(key));
                cell1.setCellValue(date);
                cell2.setCellValue(Integer.parseInt(key));
                cell3.setCellValue(ApiData.get(key));
                cell4.setCellValue(seq);
                rowIndex++;
            }
            XSSFRow finalIndexRow = sheet.createRow(finalIndexrow);
            finalcell = finalIndexRow.createCell(finalIndexcol);
            finalcell.setCellValue(rowIndex);
            xssfWorkbook.write(fos);
            fos.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Integer findBlankRowIndex(XSSFSheet sheet) {
        int finalRow;
        int rowIndex = 0;
        int colIndex = 2;
        Double tempfinalIndex;
        XSSFRow row = sheet.getRow(rowIndex);
        XSSFCell cell = row.getCell(colIndex);

        tempfinalIndex = cell.getNumericCellValue();
        finalRow = tempfinalIndex.intValue();

        return finalRow;
    }

}
