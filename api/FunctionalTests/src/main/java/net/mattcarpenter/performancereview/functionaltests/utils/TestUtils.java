package net.mattcarpenter.performancereview.functionaltests.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TestUtils {

    private static final String BASE_URL = "http://localhost:8080";

    public static String makePath(String pathTemplate, String ... args) {
        return BASE_URL + String.format(pathTemplate, args);
    }

    public static String randomAlphaNumeric(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static Date getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }
}
