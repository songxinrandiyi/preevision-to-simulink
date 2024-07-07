package preevisiontosimulink.samples;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

    public static void main(String[] args) {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // .xlsx format
        // Workbook workbook = new HSSFWorkbook(); // .xls format

        // Create a Sheet
        Sheet sheet = workbook.createSheet("ExampleSheet");

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create Cells
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Header1");

        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Header2");

        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("Header3");

        // Create data rows
        for (int i = 1; i <= 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue("Data" + i);
            row.createCell(1).setCellValue(i);
            row.createCell(2).setCellValue(Math.random());
        }

        // Resize columns to fit the content
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("example.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Closing the workbook
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

