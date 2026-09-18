package ru.academits;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static void setCellBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        style.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
    }

    public static void main(String[] args) {
        List<Person> personsList = new ArrayList<>();

        personsList.add(new Person("Ivan", "Petrov", 26, "123245"));
        personsList.add(new Person("Nikolay", "Sidorov", 30, "54234"));
        personsList.add(new Person("Irina", "Shevtsova", 25, "333556"));
        personsList.add(new Person("Ekaterina", "Ivanova", 34, "55235234"));

        String outputFilePath = "persons.xlsx";

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {

            XSSFSheet personsSheet = workbook.createSheet("Persons data");

            CellStyle whiteRowStyle = workbook.createCellStyle();

            whiteRowStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            whiteRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setCellBorders(whiteRowStyle);

            CellStyle greyRowStyle = workbook.createCellStyle();

            greyRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            greyRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setCellBorders(greyRowStyle);

            int rowNumber = 1;

            for (Person person : personsList) {
                Row personRow = personsSheet.createRow(rowNumber++);

                CellStyle currentStyle = (rowNumber % 2 == 0) ? greyRowStyle : whiteRowStyle;

                currentStyle.setAlignment(HorizontalAlignment.CENTER);
                currentStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                Cell firstNameCell = personRow.createCell(0);
                firstNameCell.setCellValue(person.getFirstName());
                firstNameCell.setCellStyle(currentStyle);

                Cell secondNameCell = personRow.createCell(1);
                secondNameCell.setCellValue(person.getSecondName());
                secondNameCell.setCellStyle(currentStyle);

                Cell ageCell = personRow.createCell(2);
                ageCell.setCellValue(person.getAge());
                ageCell.setCellStyle(currentStyle);

                Cell phoneNumberCell = personRow.createCell(3);
                phoneNumberCell.setCellValue(person.getPhoneNumber());
                phoneNumberCell.setCellStyle(currentStyle);
            }

            String[] headers = {"First Name", "Second Name", "Age", "Phone Number"};
            Row headerRow = personsSheet.createRow(0);

            CellStyle headerRowStyle = workbook.createCellStyle();

            headerRowStyle.setAlignment(HorizontalAlignment.CENTER);
            headerRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setCellBorders(headerRowStyle);

            Font headerFont = workbook.createFont();

            headerFont.setBold(true);
            headerRowStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; ++i) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerRowStyle);

                personsSheet.autoSizeColumn(i);

                int currentWidth = personsSheet.getColumnWidth(i);
                int padding = 3 * 256;

                personsSheet.setColumnWidth(i, Math.min(currentWidth + padding, 255 * 256));
            }

            workbook.write(fileOut);

            System.out.println("Excel file successfully created at: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}