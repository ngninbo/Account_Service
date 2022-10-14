class Util {
    public static String capitalize(String str) {
        System.out.printf("Before: %s\n", str);
        if (str == null || str.isBlank()) {
            System.out.printf("After: %s\n", str);
            return str;
        }

        final String result;
        if (str.length() == 1) {
            result = str.toUpperCase();
            System.out.printf("After: %s\n", result);
            return result;
        }

        result = Character.toUpperCase(str.charAt(0)) + str.substring(1);
        System.out.printf("After: %s\n", result);
        return result;
    }
}