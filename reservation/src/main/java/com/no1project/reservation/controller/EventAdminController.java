package com.no1project.reservation.controller;

import com.no1project.reservation.model.Event;
import com.no1project.reservation.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/events")
public class EventAdminController {

    private final EventService eventService;

    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    public record CreateEventRequest(
            String date,
            String deadline,
            String place,
            String item,
            Integer companyId,
            String note
    ) {}

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateEventRequest req) {
        Event e = new Event();
        e.setDate(req.date());
        e.setDeadline(req.deadline());
        e.setPlace(req.place());
        e.setItem(req.item());
        e.setCompanyId(req.companyId() == null ? 0 : req.companyId());
        e.setNote(req.note());

        Event created = eventService.create(e);
        return ResponseEntity.ok(created); // 最小でOK
    }
}
