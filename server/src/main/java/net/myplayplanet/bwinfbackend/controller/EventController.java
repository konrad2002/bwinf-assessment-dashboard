package net.myplayplanet.bwinfbackend.controller;

import lombok.RequiredArgsConstructor;
import net.myplayplanet.bwinfbackend.dto.NewEvaluationEventDto;
import net.myplayplanet.bwinfbackend.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventController {

    private final EventService eventService;

    // SSE endpoint for frontend to subscribe
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NewEvaluationEventDto> streamEvents() {
        return this.eventService.getSink().asFlux();
    }


}
