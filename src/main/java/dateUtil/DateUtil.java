package dateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public static Date stringToDate(String dataStr) {
        try {
            return sdf.parse(dataStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format");
        }

        return null;
    }

    public static String dateToString(Date date) {
        return sdf.format(date);
    }
}