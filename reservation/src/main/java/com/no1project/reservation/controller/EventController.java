package com.no1project.reservation.controller;

import com.no1project.reservation.service.EventService;
import com.no1project.reservation.service.EventService.EventPage;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // 例:
    // /events?page=0&size=10
    // /events?page=0&size=10&q=トヨタ
    // /events?page=0&size=10&from=2025-12-01 00:00:00&to=2025-12-31 23:59:59
    @GetMapping
    public EventPage getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        return eventService.getEventPage(page, size, q, from, to);
    }
}
