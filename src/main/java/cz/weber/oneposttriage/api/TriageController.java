package cz.weber.oneposttriage.api;

import cz.weber.oneposttriage.ai.DeadlineExtractor;
import cz.weber.oneposttriage.ai.PriorityScorer;
import cz.weber.oneposttriage.message.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class TriageController {

    private final DeadlineExtractor deadlineExtractor;
    private final PriorityScorer scorer;
    private final MessageRepository repo;

    public TriageController(DeadlineExtractor deadlineExtractor, PriorityScorer scorer, MessageRepository repo) {
        this.deadlineExtractor = deadlineExtractor;
        this.scorer = scorer;
        this.repo = repo;
    }

    @PostMapping("/triage")
    public ResponseEntity<TriageResponse> triage(@RequestBody @Valid MessageDto dto) {
        // 1) Sestavíme entitu ze vstupu
        var msg = new Message();
        msg.setReceivedAt(dto.receivedAt());
        msg.setSender(dto.sender());
        msg.setSubject(dto.subject());
        msg.setBody(dto.body());

        // 2) AI logika: najdi lhůtu + spočti prioritu a důvody
        var deadline = deadlineExtractor.extract(dto.subject() + " " + dto.body(), dto.receivedAt());
        var result = scorer.score(dto.sender(), dto.subject(), dto.body(), deadline);

        // 3) Ulož do DB
        msg.setDeadlineDate(deadline.orElse(null));
        msg.setPriority(result.priority().name());
        msg.setSuggestedAction(result.action());
        msg.setReasons(result.reasons().toString()); // pro PoC stačí jako text
        repo.save(msg);

        // 4) Postav výstupní DTO
        var response = new TriageResponse(
                msg.getId(),
                msg.getPriority(),
                msg.getDeadlineDate(),
                result.reasons(),
                result.action()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages")
    public List<Message> list() {
        return repo.findAll();
    }
}
