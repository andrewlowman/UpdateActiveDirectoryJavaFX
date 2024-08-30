package com.example.updateactivedirectoryjavafx.Utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoadExcel {

    File excelFile;
    int sheetNumber = 12;

    public LoadExcel(File file, int sheet) {
        this.sheetNumber = sheet;
        this.excelFile = file;
    }

    public int getDeptCode(int rowNumber){

        //could return zero if cell is colored
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFile);

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            int lastRow = sheet.getLastRowNum();

            Row row;
            Cell cell = null;

            if(rowNumber <= lastRow){
                row = sheet.getRow(rowNumber);
                cell = row.getCell(7);
                Cell colorCell = row.getCell(0);
                changeCellBackgroundColor(workbook,colorCell,excelFile);
            }else{
                return 0;
            }
            assert cell != null;
            return (int) cell.getNumericCellValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getNameOfDept(int rowNumber){
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFile);

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            int lastRow = sheet.getLastRowNum();

            Row row;
            Cell cell = null;

            if(rowNumber <= lastRow){
                row = sheet.getRow(rowNumber);
                cell = row.getCell(8);
                Cell colorCell = row.getCell(0);
                changeCellBackgroundColor(workbook,colorCell,excelFile);
            }else{
                return "empty";
            }
            assert cell != null;
            return cell.getStringCellValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getEmployeeID(int rowNumber){
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFile);

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            int lastRow = sheet.getLastRowNum();

            Row row;
            Cell cell = null;

            if(rowNumber <= lastRow){
                row = sheet.getRow(rowNumber);
                cell = row.getCell(0);
                if(checkIfCellColored(cell)){
                    return 0;
                }
            }else{
                //do something here like a popup
            }
            assert cell != null;
            return (int) cell.getNumericCellValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getEmployeeFirstName(int rowNumber){
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFile);

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            int lastRow = sheet.getLastRowNum();

            Row row;
            Cell cell = null;

            if(rowNumber <= lastRow){
                row = sheet.getRow(rowNumber);
                cell = row.getCell(3);
            }else{
                return "empty";
            }
            assert cell != null;
            return cell.getStringCellValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getEmployeeLastName(int rowNumber){
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFile);

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            int lastRow = sheet.getLastRowNum();
            // System.out.println("Last row: " + lastRow);

            Row row;
            Cell cell = null;

            if(rowNumber <= lastRow){
                row = sheet.getRow(rowNumber);
                cell = row.getCell(2);
            }else{
                return "empty";
            }
            assert cell != null;
            return cell.getStringCellValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void changeCellBackgroundColor(XSSFWorkbook workbook, Cell cell, File file) throws IOException {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(cellStyle);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    public boolean checkIfCellColored(Cell cell){
        if(cell.getCellStyle().getFillPattern()==FillPatternType.SOLID_FOREGROUND){
            //System.out.println("Fill pattern is real");
            return true;
        }
        return false;
    }

}
