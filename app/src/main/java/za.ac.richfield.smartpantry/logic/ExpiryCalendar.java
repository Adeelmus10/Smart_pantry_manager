package za.ac.richfield.smartpantry.logic;

import java.util.Calendar;
import java.util.Date;

public class ExpiryCalendar {

    public static long getDaysUntilExpiry(long expiryDateMillis) {
        if (expiryDateMillis <= 0) {
            return Long.MAX_VALUE;
        }

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        Calendar expiry = Calendar.getInstance();
        expiry.setTimeInMillis(expiryDateMillis);
        expiry.set(Calendar.HOUR_OF_DAY, 0);
        expiry.set(Calendar.MINUTE, 0);
        expiry.set(Calendar.SECOND, 0);
        expiry.set(Calendar.MILLISECOND, 0);

        long diffMillis = expiry.getTimeInMillis() - now.getTimeInMillis();
        return diffMillis / (24 * 60 * 60 * 1000L);
    }

    public static String getExpiryText(long expiryDateMillis) {
        if (expiryDateMillis <= 0) {
            return "No expiry set";
        }

        long days = getDaysUntilExpiry(expiryDateMillis);
        if (days == 0) {
            return "Expires today";
        } else if (days > 0) {
            return "Expires in " + days + " day(s)";
        } else {
            return "Expired " + Math.abs(days) + " day(s) ago";
        }
    }

    public static boolean isExpiringSoon(long expiryDateMillis, int alertThresholdDays) {
        if (expiryDateMillis <= 0) {
            return false;
        }
        long days = getDaysUntilExpiry(expiryDateMillis);
        return days <= alertThresholdDays;
    }
}
