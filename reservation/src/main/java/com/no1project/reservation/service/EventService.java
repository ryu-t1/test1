package com.no1project.reservation.service;

import com.no1project.reservation.model.Event;
import com.no1project.reservation.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventPage getEventPage(int page, int size) {
        // マイナス指定が来たときの保険
        if (page < 0) page = 0;

        List<Event> events = eventRepository.findPage(page, size);
        int totalCount = eventRepository.countAll();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        return new EventPage(events, page, size, totalCount, totalPages);
    }

    /** フロントに返す DTO */
    public static class EventPage {
        private List<Event> events;
        private int page;
        private int size;
        private int totalCount;
        private int totalPages;

        public EventPage(List<Event> events, int page, int size,
                         int totalCount, int totalPages) {
            this.events = events;
            this.page = page;
            this.size = size;
            this.totalCount = totalCount;
            this.totalPages = totalPages;
        }

        public List<Event> getEvents() { return events; }
        public int getPage() { return page; }
        public int getSize() { return size; }
        public int getTotalCount() { return totalCount; }
        public int getTotalPages() { return totalPages; }
    }
}
