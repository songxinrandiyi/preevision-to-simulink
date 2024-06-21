package preevisiontosimulink.util;


public class CalculatorUtils {
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
}
