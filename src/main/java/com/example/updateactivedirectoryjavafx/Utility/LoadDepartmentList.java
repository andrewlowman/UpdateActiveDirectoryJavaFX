package com.example.updateactivedirectoryjavafx.Utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Iterator;

public class LoadDepartmentList {
    String jdbcURL = "jdbc:mysql://localhost:3306/schema_db";
    Connection conn = null;
    String username = "root";
    String password = "Monsterenergy223";
    int batchSize = 20;
   /* public LoadDepartmentList(String path) {
        this.path = path;
    }*/

    public void loadDepartment(File file) {
        conn = DBConnection.startConnection();

        try {

            FileInputStream fileInputStream = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            //conn = DriverManager.getConnection(jdbcURL,username,password);
            conn.setAutoCommit(false);

            String sql = "INSERT INTO dept_location_mc (dept_code, dept_name, location_code, location_desc, mail_code, mail_code_secondary, mail_code_tertiary, hospital_mail_code, other_location) VALUES (?,?,?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            int count = 0;

            rowIterator.next();

            while(rowIterator.hasNext()){

                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while(cellIterator.hasNext()){
                    Cell nextCell = cellIterator.next();

                    int columnIndex = nextCell.getColumnIndex();

                    switch(columnIndex){
                        case 0:
                            String deptCode = nextCell.getStringCellValue();
                            preparedStatement.setString(1,deptCode);
                            break;
                        case 1:
                            if(nextCell.getCellType() == CellType.BLANK){
                                preparedStatement.setNull(2, Types.VARCHAR);
                            }else{String deptName = nextCell.getStringCellValue();
                                preparedStatement.setString(2,deptName);
                            }
                            break;
                        case 2:
                            if(nextCell.getCellType() == CellType.BLANK){
                                preparedStatement.setNull(3,Types.VARCHAR);
                            }else{
                                String locationCode = nextCell.getStringCellValue();
                                preparedStatement.setString(3,locationCode);
                            }
                            break;
                        case 3:
                            if(nextCell.getCellType() == CellType.BLANK){
                                preparedStatement.setNull(4,Types.VARCHAR);
                            }else {
                                String locationDesc = nextCell.getStringCellValue();
                                preparedStatement.setString(4, locationDesc);
                            }
                            break;
                        case 4:
                            if(nextCell.getCellType() == CellType.BLANK){
                                preparedStatement.setNull(5,Types.VARCHAR);
                            }else {
                                String mailCode = nextCell.getStringCellValue();
                                preparedStatement.setString(5, mailCode);
                            }
                            break;
                        case 5:
                            if(nextCell.getCellType() == CellType.BLANK){
                                preparedStatement.setNull(6,Types.VARCHAR);
                            }else {
                                String mailCodeTwo = nextCell.getStringCellValue();
                                preparedStatement.setString(6, mailCodeTwo);
                            }
                            break;
                        case 6:
                            if(nextCell.getCellType() == CellType.BLANK){
                                preparedStatement.setNull(7,Types.VARCHAR);
                            }else {
                                String mailCodeThree = nextCell.getStringCellValue();
                                preparedStatement.setString(7, mailCodeThree);
                            }
                            break;
                        case 7:
                            if(nextCell.getCellType() == CellType.BLANK){
                                preparedStatement.setNull(8,Types.VARCHAR);
                            }else {
                                String otherLocation = nextCell.getStringCellValue();
                                preparedStatement.setString(8, otherLocation);
                            }
                            break;
                        case 8:
                            if(nextCell.getCellType() == CellType.BLANK){
                                preparedStatement.setNull(9,Types.VARCHAR);
                            }else {
                                String hospitalMC = nextCell.getStringCellValue();
                                preparedStatement.setString(9, hospitalMC);
                            }
                            break;
                        default:
                            break;
                    }

                }
                preparedStatement.addBatch();

                if(count % batchSize == 0){
                    preparedStatement.executeBatch();
                }


            }
            workbook.close();

            preparedStatement.executeBatch();

            conn.commit();
            DBConnection.closeConnection();

            long end = System.currentTimeMillis();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void truncateTable(){
        try {
            conn = DriverManager.getConnection(jdbcURL, username, password);

            //clear table of old data
            String sql = "TRUNCATE TABLE dept_location_mc";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
