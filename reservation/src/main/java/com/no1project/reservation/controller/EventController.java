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

    // 例: /api/events?page=0&size=10
    @GetMapping
    public EventPage getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventPage(page, size);
    }
}
