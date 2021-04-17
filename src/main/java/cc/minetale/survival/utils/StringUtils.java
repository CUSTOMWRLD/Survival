package cc.minetale.survival.utils;

public class StringUtils {
    public static String snakeToCamelCase(String str) {
        str = str.substring(0, 1).toUpperCase()
                + str.substring(1);

        StringBuilder builder
                = new StringBuilder(str);

        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '_') {
                builder.deleteCharAt(i);
                //builder.append(" ");
                builder.replace(i, i + 1, " " + Character.toUpperCase(builder.charAt(i)));
            }
        }

        return builder.toString();
    }
}
