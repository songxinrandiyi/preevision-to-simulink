package preevisiontosimulink.parser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
    public static void main(String[] args) {
        String excelFilePath = "Verbindungsliste_EEA2.1.xlsx";
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("Sheet: " + sheet.getSheetName());
                iterateRowsAndCells(sheet);
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void iterateRowsAndCells(Sheet sheet) {
        int rowCount = 0;
        for (Row row : sheet) {
            if (rowCount >= 500) {
                break;
            }
            for (Cell cell : row) {
                printCellValue(cell);
            }
            System.out.println();
            rowCount++;
        }
    }

    private static void printCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                System.out.print(cell.getStringCellValue() + "\t");
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue() + "\t");
                } else {
                    System.out.print(cell.getNumericCellValue() + "\t");
                }
                break;
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue() + "\t");
                break;
            case FORMULA:
                printFormulaCell(cell);
                break;
            case BLANK:
                System.out.print(" \t");
                break;
            default:
                System.out.print(" \t");
                break;
        }
    }

    private static void printFormulaCell(Cell cell) {
        switch (cell.getCachedFormulaResultType()) {
            case STRING:
                System.out.print(cell.getRichStringCellValue() + "\t");
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue() + "\t");
                } else {
                    System.out.print(cell.getNumericCellValue() + "\t");
                }
                break;
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue() + "\t");
                break;
            default:
                System.out.print(" \t");
                break;
        }
    }
}
