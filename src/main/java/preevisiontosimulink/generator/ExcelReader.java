package preevisiontosimulink.generator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import preevisiontosimulink.util.CellUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
	public static void main(String[] args) {
		String excelFilePath = "Wire Connection List.xlsx";
		try (FileInputStream fis = new FileInputStream(excelFilePath); Workbook workbook = new XSSFWorkbook(fis)) {

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
		int rowBegin = 1; // Start from the second row
		int rowCount = 0;
		for (int i = rowBegin; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (rowCount >= 500) {
	            break;
	        }
			if (row == null) {
				continue; // Skip if row is null
			}
			Cell connector1 = row.getCell(2);
	        Cell pinnummer1 = row.getCell(3);
	        Cell connector2 = row.getCell(11);
	        Cell pinnummer2 = row.getCell(12);
	        Cell currentValue = row.getCell(7);
	        
	        String connectorName1 = null;
	        String connectorName2 = null;
	        Integer pin1 = null;
	        Integer pin2 = null;
	        Double current = null;
	        
	        Boolean notBlank = pinnummer1.getCellType() != CellType.BLANK && pinnummer2.getCellType() != CellType.BLANK 
	        		&& connector1.getCellType() != CellType.BLANK && connector2.getCellType() != CellType.BLANK
	        		&& currentValue.getCellType() != CellType.BLANK;

            if (notBlank) {
            	connectorName1 = connector1.getStringCellValue();
                if (!connectorName1.isEmpty()) {
                	System.out.println("Leftconnector: " + connectorName1);
                } else {
	                System.out.println("Empty leftconnector");
                }
                pin1 = CellUtils.getIntegerValueFromCell(pinnummer1);
                if (pin1 == null) {
					pin1 = 1;
				}
                System.out.println("Leftpin: " + pin1);

				connectorName2 = connector2.getStringCellValue();
				if (!connectorName2.isEmpty()) {
					System.out.println("Rightconnector: " + connectorName2);
				} else {
					System.out.println("Empty rightconnector");
				}
                pin2 = CellUtils.getIntegerValueFromCell(pinnummer2);
				if (pin2 == null) {
					pin2 = 1;
				}
                System.out.println("Rightpin: " + pin2);
                
                current = CellUtils.getNumericValueFromCell(currentValue);
				if (current == null) {
					current = 0.0;
				}
				System.out.println("Current: " + current);
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
