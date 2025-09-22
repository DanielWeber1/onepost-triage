package cz.weber.oneposttriage.api;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TriageResponse(
        UUID id,
        String priority,
        LocalDate deadlineDate,
        List<String> reasons,
        String suggestedAction
) {}
