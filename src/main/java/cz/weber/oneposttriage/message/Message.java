package cz.weber.oneposttriage.message;

import jakarta.persistence.*;
import java.time.*;
import java.util.UUID;

@Entity
public class Message {
    @Id @GeneratedValue
    private UUID id;
    private Instant receivedAt;
    private String sender;

    @Column(length = 500)
    private String subject;

    @Column(length = 20000)
    private String body;

    private LocalDate deadlineDate;     // výsledek extractoru
    private String priority;            // HIGH/MEDIUM/LOW
    private String suggestedAction;     // např. Forward:Legal

    @Column(length = 4000)
    private String reasons;             // jednoduché uložení jako text (např. "[..]")

    /// ////////////////////////////////////////////////////////////////////////////////////////////////

    public UUID getId() { return id; }
    public Instant getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Instant receivedAt) { this.receivedAt = receivedAt; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDate getDeadlineDate() { return deadlineDate; }
    public void setDeadlineDate(LocalDate deadlineDate) { this.deadlineDate = deadlineDate; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getSuggestedAction() { return suggestedAction; }
    public void setSuggestedAction(String suggestedAction) { this.suggestedAction = suggestedAction; }
    public String getReasons() { return reasons; }
    public void setReasons(String reasons) { this.reasons = reasons; }
}
