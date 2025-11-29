package net.myplayplanet.bwinfbackend.service.event;

import lombok.Getter;
import net.myplayplanet.bwinfbackend.dto.NewEvaluationEventDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class EventService {

    // Thread-safe sink to emit events manually
    @Getter
    private final Sinks.Many<NewEvaluationEventDto> sink = Sinks.many().replay().latest();

    public void emitEvent(NewEvaluationEventDto dto) {
        sink.tryEmitNext(dto);
    }
}
