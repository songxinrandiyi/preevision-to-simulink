package preevisiontosimulink.util;

public class CalculatorUtils {
	// Spezifische Widerstände in Ohm·m (Ω·m)
	private static final double RESISTIVITY_COPPER = 1.68e-8;
	private static final double RESISTIVITY_ALUMINUM = 2.82e-8;
	
	// Spezifische thermische Kapazität in J/(kg·K)
	private static final double CAPACITY_COPPER = 385;
	private static final double CAPACITY_ALUMINUM = 897;
	private static final double CAPACITY_PE = 2300;
	
	// Dichte in kg/m³
	private static final double DENSITY_COPPER = 8960;
	private static final double DENSITY_ALUMINUM = 2700;
	private static final double DENSITY_PE = 950;

	// Thermische Leitfähigkeit der Isolierung in W/(m·K)
	private static final double THERMAL_CONDUCTIVITY_INSULATION = 0.4;

	// Wärmeübergangskoeffizient der Luft in W/(m²·K)
	private static final double HEAT_TRANSFER_COEFFICIENT_AIR = 25.0;

	// Statische Methode zum Berechnen des Widerstands
	public static double calculateResistance(double length, double crossSectionalArea) {
		if (length < 0 || crossSectionalArea < 0) {
			return 0.0;
		}
		// Querschnittsfläche von mm² in m² umrechnen (1 mm² = 1e-6 m²)
		double crossSectionalAreaM2 = crossSectionalArea * 1e-6;

		// Länge von mm in Meter umrechnen (1 mm = 1e-3 m)
		double lengthM = length * 1e-3;

		// Widerstand mit der Formel R = ρ * (L / A) berechnen und zurückgeben
		return 1.77e-8 * (lengthM / crossSectionalAreaM2);
	}

	/**
	 * Berechnet die Verlustleistung einer 1 Meter langen Leitung.
	 * 
	 * @param current          Der Strom in Ampere (A)
	 * @param material         Das Material der Leitung ("Cu" für Kupfer, "Al" für
	 *                         Aluminium)
	 * @param crossSectionArea Der Querschnittsbereich der Leitung in
	 *                         Quadratmillimetern (mm²)
	 * @return Die Verlustleistung in Watt (W)
	 * @throws IllegalArgumentException wenn das Material weder "Cu" noch "Al" ist
	 */
	public static double calculatePowerLoss(double current, double crossSectionArea, String material) {
		// Widerstand der Leitung (R = ρ * L / A)
		double resistance = calculateResistance(current, crossSectionArea, material);

		// Verlustleistung (P = I² * R)
		return current * current * resistance;
	}
	
	/**
	 * Berechnet das Wiederstand einer 1 Meter langen Leitung.
	 * 
	 * @param material         Das Material der Leitung ("Cu" für Kupfer, "Al" für
	 *                         Aluminium)
	 * @param crossSectionArea Der Querschnittsbereich der Leitung in
	 *                         Quadratmillimetern (mm²)
	 * @return Die Verlustleistung in Watt (W)
	 * @throws IllegalArgumentException wenn das Material weder "Cu" noch "Al" ist
	 */
	public static double calculateResistance(double current, double crossSectionArea, String material) {
		double resistivity;

		switch (material) {
		case "Cu":
			resistivity = RESISTIVITY_COPPER;
			break;
		case "Al":
			resistivity = RESISTIVITY_ALUMINUM;
			break;
		default:
			throw new IllegalArgumentException("Ungültiges Material: " + material);
		}

		// Umwandlung der Querschnittsfläche von mm² in m²
		double crossSectionAreaInMeters = crossSectionArea * 1e-6; // in Quadratmetern (m²)

		// Widerstand der Leitung (R = ρ * L / A)
		double resistance = resistivity * 1 / crossSectionAreaInMeters; // Länge L ist 1 Meter

		return resistance;
	}

	/**
	 * Berechnet den Wärmewiderstand zwischen der Leitung und der Isolierung.
	 * 
	 * @param radiusWire       Der Radius der Leitung in Millimetern (mm)
	 * @param radiusInsulation Der Radius der Isolierung in Millimetern (mm)
	 * @return Der Wärmewiderstand in K/W
	 * @throws IllegalArgumentException wenn der Radius der Isolierung kleiner oder
	 *                                  gleich dem Radius der Leitung ist
	 */
	public static double calculateThermalResistance(double radiusWire, double radiusInsulation) {
		if (radiusInsulation <= radiusWire) {
			throw new IllegalArgumentException(
					"Der Radius der Isolierung muss größer als der Radius der Leitung sein.");
		}

		// Umwandlung von Millimetern in Meter
		double radiusWireInMeters = radiusWire * 1e-3;
		double radiusInsulationInMeters = radiusInsulation * 1e-3;

		// Wärmewiderstand Formel: R = (1 / (2 * π * λ * L)) * ln(r2 / r1)
		double thermalResistance = (1 / (2 * Math.PI * THERMAL_CONDUCTIVITY_INSULATION * 1))
				* Math.log(radiusInsulationInMeters / radiusWireInMeters);

		return thermalResistance;
	}

	/**
	 * Berechnet den Wärmewiderstand zwischen der Isolierung und der Luft.
	 * 
	 * @param radiusInsulation Der Radius der Isolierung in Millimetern (mm)
	 * @return Der Wärmewiderstand in K/W
	 */
	public static double calculateThermalResistanceToAir(double radiusInsulation) {
		// Umwandlung von Millimetern in Meter
		double radiusInsulationInMeters = radiusInsulation * 1e-3;

		// Wärmewiderstand Formel: R = 1 / (2 * π * r * L * α)
		double thermalResistance = 1 / (2 * Math.PI * radiusInsulationInMeters * 1 * HEAT_TRANSFER_COEFFICIENT_AIR);

		return thermalResistance;
	}

	/**
	 * Berechnet den Radius in Millimetern (mm) aus der Querschnittsfläche in
	 * Quadratmillimetern (mm²).
	 * 
	 * @param crossSectionArea Die Querschnittsfläche in mm²
	 * @param insulationThickness Die Dicke der Isolierung in Millimetern (mm)
	 * @return Der Radius in mm
	 */
	public static double calculateRadiusFromCrossSectionArea(double crossSectionArea) {
		if (crossSectionArea <= 0) {
			throw new IllegalArgumentException("Die Querschnittsfläche muss größer als null sein.");
		}

		// Umwandlung der Querschnittsfläche in den Radius
		// Formel: A = π * r² -> r = sqrt(A / π)
		double radius = Math.sqrt(crossSectionArea / Math.PI);

		return radius;
	}
	
	/**
	 * Berechnet die thermische Kapazität der Leitung
	 * 
	 * @param crossSectionArea 	  Die Querschnittsfläche in mm²
	 * @param insulationThickness Die Dicke der Isolierung in Millimetern (mm)
	 * @param material            Das Material der Leitung ("Cu" für Kupfer, "Al"
	 *                            für Aluminium)
	 * @return Die Kapazität in J/K
	 * @throws IllegalArgumentException wenn die Querschnittsfläche kleiner oder
	 *                                  gleich null ist
	 */
	public static double calculateThermalCapacity(double crossSectionArea, double insulationThickness, String material) {
		if (crossSectionArea <= 0) {
			throw new IllegalArgumentException("Die Querschnittsfläche muss größer als null sein.");
		}
		// Umwandlung der Querschnittsfläche in den Radius
		// Formel: A = π * r² -> r = sqrt(A / π)
		double radiusWireInMeters = Math.sqrt(crossSectionArea / Math.PI) * 1e-3;
		double radiusInsulationInMeters = radiusWireInMeters + insulationThickness * 1e-3;
		
		double densityWire;
		double specificWireCapacity;
		switch (material) {
		case "Cu":
			densityWire = DENSITY_COPPER;
			specificWireCapacity = CAPACITY_COPPER;
			break;
		case "Al":
			densityWire = DENSITY_ALUMINUM;
			specificWireCapacity = CAPACITY_ALUMINUM;
			break;
		default:
			throw new IllegalArgumentException("Ungültiges Material: " + material);
		}
		
		double thermalCapacity = specificWireCapacity * densityWire * Math.PI * radiusWireInMeters * radiusWireInMeters 
				+ CAPACITY_PE * DENSITY_PE * Math.PI * radiusInsulationInMeters * radiusInsulationInMeters;

		return thermalCapacity;
	}
	
	public static double calculateTotalThermalResistance(double radiusWire, double radiusInsulation) {
		return calculateThermalResistance(radiusWire, radiusInsulation) + calculateThermalResistanceToAir(radiusInsulation);
	}

	/**
	 * Berechnet die Temperaturerhöhung in Kelvin.
	 * 
	 * @param crossSectionArea    Die Querschnittsfläche der Leitung in
	 *                            Quadratmillimetern (mm²)
	 * @param insulationThickness Die Dicke der Isolierung in Millimetern (mm)
	 * @param material            Das Material der Leitung ("Cu" für Kupfer, "Al"
	 *                            für Aluminium)
	 * @param current             Der Strom in Ampere (A)
	 * @return Die Temperaturerhöhung in Kelvin (K)
	 * @throws IllegalArgumentException wenn das Material weder "Cu" noch "Al" ist
	 */
	public static double calculateTemperatureIncrease(double crossSectionArea, double insulationThickness,
			String material, double current) {
		// Berechne den Radius der Leitung
		double radiusWire = calculateRadiusFromCrossSectionArea(crossSectionArea);

		// Berechne den Radius der Isolierung
		double radiusInsulation = radiusWire + insulationThickness;

		// Berechne die Verlustleistung der Leitung
		double powerLoss = calculatePowerLoss(current, crossSectionArea, material);

		// Berechne die Gesamttemperaturerhöhung
		double totalThermalResistance = calculateTotalThermalResistance(radiusWire, radiusInsulation);
		double temperatureIncrease = powerLoss * totalThermalResistance;

		return temperatureIncrease;
	}
}
