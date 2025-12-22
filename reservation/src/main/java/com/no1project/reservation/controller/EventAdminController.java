package com.no1project.reservation.controller;

import com.no1project.reservation.model.Event;
import com.no1project.reservation.service.EventService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            String note) {
    }

    public record UpdateEventRequest(
            String date,
            String deadline,
            String place,
            String item,
            Integer companyId,
            String note) {
    }

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
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> update(@PathVariable int eventId, @RequestBody UpdateEventRequest req) {
        Event e = new Event();
        e.setEventId(eventId);
        e.setDate(req.date());
        e.setDeadline(req.deadline());
        e.setPlace(req.place());
        e.setItem(req.item());
        e.setCompanyId(req.companyId() == null ? 0 : req.companyId());
        e.setNote(req.note());

        Event updated = eventService.update(e);
        return ResponseEntity.ok(updated);
    }

   @DeleteMapping("/{eventId}")
    public ResponseEntity<?> delete(@PathVariable int eventId) {
        try {
            eventService.delete(eventId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
