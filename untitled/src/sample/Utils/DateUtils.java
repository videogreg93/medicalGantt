package sample.Utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
        public static Date addDays(Date date, int days)
        {
            if (days == 0)
                return date;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days); //minus number would decrement the days
            return cal.getTime();
        }

}
