package preevisiontosimulink.util;

public class StringUtil {

    /**
     * Extracts the first part of a string separated by an underscore.
     *
     * @param str the input string
     * @return the first part of the string before the underscore
     */
    public static String getFirstPart(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        int underscoreIndex = str.indexOf('_');
        if (underscoreIndex == -1) {
            return str; // No underscore found, return the whole string
        }
        return str.substring(0, underscoreIndex);
    }
}
