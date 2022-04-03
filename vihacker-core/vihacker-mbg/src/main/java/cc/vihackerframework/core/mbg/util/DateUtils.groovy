package cc.vihackerframework.core.mbg.util

import java.text.SimpleDateFormat

/**
 * Created by Ranger on 2022/4/3.
 */
class DateUtils {

    public final static String DATE_PATTERN = "yyyy/MM/dd";

    static def format(Date date) {
        return format(date, DATE_PATTERN);
    }

    static def format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
}
