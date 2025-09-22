package cz.weber.oneposttriage.ai;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class PriorityScorer {

    private static final List<String> LEGAL  = List.of("výzva","rozhodnutí","exekuce","správní řízení","předžalobní","pokuta");
    private static final List<String> INVOICE= List.of("faktura","splatnost","variabilní symbol","zálohová");
    private static final List<String> NEWSL  = List.of("newsletter","odhlásit","unsubscribe","novinky");

    public record Result(Priority priority, int score, List<String> reasons, String action) {}

    public Result score(String sender, String subject, String body, Optional<LocalDate> deadline) {
        int score = 0;
        List<String> reasons = new ArrayList<>();
        String text = ((subject==null?"":subject) + " " + (body==null?"":body)).toLowerCase();

        if (deadline.isPresent()) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), deadline.get());
            score += 4; reasons.add("Nalezena lhůta: " + deadline.get());
            if (days <= 3) { score += 6; reasons.add("Lhůta do 3 dnů"); }
            else if (days <= 7) { score += 3; reasons.add("Lhůta do 7 dnů"); }
        }

        if (containsAny(text, LEGAL))  { score += 4; reasons.add("Právní klíčová slova"); }
        if (containsAny(text, INVOICE)){ score += 2; reasons.add("Faktura/platba"); }
        if (containsAny(text, NEWSL))  { score -= 5; reasons.add("Newsletter indikátory"); }

        if (isAuthority(sender)) { score += 4; reasons.add("Úřad/autorita"); }

        Priority p = (score >= 7) ? Priority.HIGH : (score >= 3) ? Priority.MEDIUM : Priority.LOW;
        String action = suggest(p, text);
        return new Result(p, score, reasons, action);
    }

    private boolean containsAny(String text, List<String> words) {
        for (String w : words) if (text.contains(w)) return true;
        return false;
    }
    private boolean isAuthority(String sender) {
        if (sender == null) return false;
        String s = sender.toLowerCase();
        return s.contains("finanční úřad") || s.contains("mfcr") || s.contains("justice") || s.contains("cnb") || s.contains("czso");
    }
    private String suggest(Priority p, String text) {
        if (p==Priority.HIGH && containsAny(text, LEGAL))   return "Forward:Legal";
        if (p==Priority.HIGH && containsAny(text, INVOICE)) return "CreateTask:Finance";
        return (p==Priority.LOW) ? "ArchiveOrUnsubscribe" : "ReviewToday";
    }
}
