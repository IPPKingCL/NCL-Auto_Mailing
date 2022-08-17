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

    public String insertToExcel(List<Map<String, Integer>> ApiData, String date, Integer seq) {
        String filePath = "C:/Users/NCL-NT-0164/Desktop/TEST.xlsx";
        File excel = new File(filePath);
        XSSFSheet sheet;
        XSSFSheet validSheet;
        int rowIndex;
        int finalIndexrow=0;
        int finalIndexcol=2;
        boolean isDateExist;
        XSSFCell cell0;
        XSSFCell cell1;
        XSSFCell cell2;
        XSSFCell cell3;
        XSSFCell cell4;
        XSSFCell finalcell;

        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            sheet = xssfWorkbook.getSheet("rawdata");
            validSheet = xssfWorkbook.getSheet("Validation");
            rowIndex = findBlankRowIndex(sheet);
            XSSFRow row;

            isDateExist = validateInfoAlreadyExist(validSheet,date);

            if(isDateExist) {
                return date;
            }

            FileOutputStream fos = new FileOutputStream(filePath);
            for(int i = 0; i < ApiData.size(); i++) {
                for (String key : ApiData.get(i).keySet()) {
                    row = sheet.createRow(rowIndex);
                    cell0 = row.createCell(0);
                    cell1 = row.createCell(1);
                    cell2 = row.createCell(2);
                    cell3 = row.createCell(3);
                    cell4 = row.createCell(4); // 셀 생성

                    cell0.setCellValue(date + Integer.parseInt(key) + seq);
                    cell1.setCellValue(date);
                    cell2.setCellValue(Integer.parseInt(key));
                    cell3.setCellValue(ApiData.get(i).get(key));
                    cell4.setCellValue(seq); // 생선된 셀에 값 입력
                    rowIndex++;
                }
                seq++;
            }
            XSSFRow finalIndexRow = sheet.createRow(finalIndexrow);
            finalcell = finalIndexRow.createCell(finalIndexcol);
            finalcell.setCellValue(rowIndex);
            xssfWorkbook.write(fos); // 입력
            fos.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return "success";
    }

    public Integer findBlankRowIndex(XSSFSheet sheet) { //엑셀 안에 있는 마지막 인덱스 번호를 불러와서 저장된 데이터 이후부터 다시 작성하도록 하는 함수
        int finalRow;
        int rowIndex = 0; //마지막으로 작성한 셀의 행 인덱스
        int colIndex = 2; // 마지막으로 작성한 셀의 열 인덱스
        Double tempfinalIndex;
        XSSFRow row = sheet.getRow(rowIndex);
        XSSFCell cell = row.getCell(colIndex);

        tempfinalIndex = cell.getNumericCellValue(); // 더블형 반환
        finalRow = tempfinalIndex.intValue(); // 정수로 변환

        return finalRow;
    }

    public boolean validateInfoAlreadyExist(XSSFSheet sheet, String apiDate) {
        List<Integer> startEndIndex = new ArrayList<>();
        int startRowIndex = 0;
        int startColIndex = 0;
        int endRowIndex = 1;
        int endColIndex = 0;
        int valueOfStartCellIndex;
        int valueOfEndCellIndex;

        Double doubleTypeValueOfStartCellIndex;
        Double doubleTypeValueOfEndCellIndex;

        XSSFRow startrow = sheet.getRow(startRowIndex);
        XSSFCell startCell = startrow.getCell(startColIndex);
        XSSFRow endrow = sheet.getRow(endRowIndex);
        XSSFCell endCell = endrow.getCell(endColIndex);

        doubleTypeValueOfStartCellIndex = startCell.getNumericCellValue();
        doubleTypeValueOfEndCellIndex = endCell.getNumericCellValue();

        valueOfStartCellIndex = doubleTypeValueOfStartCellIndex.intValue();
        valueOfEndCellIndex = doubleTypeValueOfEndCellIndex.intValue();

        for(int i = valueOfStartCellIndex; i<=valueOfEndCellIndex; i++) {
            if(valueOfStartCellIndex == valueOfEndCellIndex) {
                startEndIndex.add(valueOfStartCellIndex);
                startEndIndex.add(valueOfEndCellIndex);
                modifyEndIndex(sheet, startEndIndex,apiDate);
                return false;
            }
            else if (i < valueOfEndCellIndex){
                XSSFRow rowDate = sheet.getRow(i);
                XSSFCell date = rowDate.getCell(0);
                if(apiDate.equals(date.getStringCellValue())) {
                    return true;
                }
            } else {
                break;
            }
        }
        startEndIndex.add(valueOfStartCellIndex);
        startEndIndex.add(valueOfEndCellIndex);
        modifyEndIndex(sheet, startEndIndex,apiDate);
        return false;
    }

    public void modifyEndIndex(XSSFSheet sheet, List<Integer> startEndIndex, String apiDate) {
        int endRowIndex = 1;
        int endColIndex = 0;
        int dateRowIndex = startEndIndex.get(1);
        int dateColIndex = 0;
        XSSFRow endrow = sheet.getRow(endRowIndex);
        XSSFCell endCell = endrow.getCell(endColIndex);
        XSSFRow dateRow = sheet.createRow(dateRowIndex);
        XSSFCell dateCell = dateRow.createCell(dateColIndex);

        dateCell.setCellValue(apiDate);
        endCell.setCellValue(startEndIndex.get(1)+1);
    }

}
