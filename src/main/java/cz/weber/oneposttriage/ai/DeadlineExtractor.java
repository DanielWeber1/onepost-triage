package cz.weber.oneposttriage.ai;

import org.springframework.stereotype.Component;
import java.time.*;
import java.util.Optional;
import java.util.regex.*;

@Component
public class DeadlineExtractor {
    private static final Pattern REL_DAYS  = Pattern.compile("do\\s+(\\d+)\\s*dn[íu]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern REL_WEEKS = Pattern.compile("do\\s+(\\d+)\\s*týdn[ůu]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern ABS_DATE  = Pattern.compile("(?:do|nejpozději)?\\s*(\\d{1,2})\\.\\s*(\\d{1,2})\\.\\s*(\\d{4})", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public Optional<LocalDate> extract(String text, Instant receivedAt) {
        if (text == null) return Optional.empty();
        String t = text.toLowerCase();

        Matcher m1 = REL_DAYS.matcher(t);
        if (m1.find()) {
            int d = Integer.parseInt(m1.group(1));
            return Optional.of(LocalDateTime.ofInstant(receivedAt, ZoneId.systemDefault()).toLocalDate().plusDays(d));
        }
        Matcher m2 = REL_WEEKS.matcher(t);
        if (m2.find()) {
            int w = Integer.parseInt(m2.group(1));
            return Optional.of(LocalDateTime.ofInstant(receivedAt, ZoneId.systemDefault()).toLocalDate().plusWeeks(w));
        }
        Matcher m3 = ABS_DATE.matcher(t);
        if (m3.find()) {
            int day = Integer.parseInt(m3.group(1));
            int month = Integer.parseInt(m3.group(2));
            int year = Integer.parseInt(m3.group(3));
            try { return Optional.of(LocalDate.of(year, month, day)); } catch (Exception ignored) {}
        }
        return Optional.empty();
    }
}
