package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Generic live event envelope used for SSE / WebSocket messages. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveEventEnvelopeDTO<T> {
    private String type; // e.g. assessment.completed | heartbeat
    private T payload;
    private String eventId; // optional server event id
    /** ISO 8601 UTC timestamp when emitted */
    private String emittedAt;
}
