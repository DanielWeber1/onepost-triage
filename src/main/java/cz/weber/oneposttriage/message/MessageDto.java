package cz.weber.oneposttriage.message;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record MessageDto(
        @NotNull Instant receivedAt,
        @NotBlank String sender,
        @NotBlank String subject,
        @NotBlank String body
) {}
