package preevisiontosimulink.util;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtils {

	public static Double convertStringToDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			System.out.println("Invalid string format for a double: " + str);
			return null; // Return a default value
		}
	}

	public static Integer convertStringToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			System.out.println("Invalid string format for an integer: " + str);
			return null; // Return a default value, e.g., 0
		}
	}

	public static Integer getIntegerValueFromCell(Cell cell) {
		switch (cell.getCellType()) {
		case NUMERIC:
			return (int) cell.getNumericCellValue();
		case STRING:
			return convertStringToInt(cell.getStringCellValue());
		default:
			throw new IllegalArgumentException("Invalid cell type: " + cell.getCellType());
		}
	}

	public static Double getNumericValueFromCell(Cell cell) {
		switch (cell.getCellType()) {
		case NUMERIC:
			return cell.getNumericCellValue();
		case STRING:
			return convertStringToDouble(cell.getStringCellValue());
		default:
			throw new IllegalArgumentException("Invalid cell type: " + cell.getCellType());
		}
	}
}
