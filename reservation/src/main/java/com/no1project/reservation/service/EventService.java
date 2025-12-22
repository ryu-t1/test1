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
        if (page < 0)
            page = 0;

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

        public List<Event> getEvents() {
            return events;
        }

        public int getPage() {
            return page;
        }

        public int getSize() {
            return size;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }

    // 説明会追加
    public Event create(Event e) {
        if (e.getCompanyId() <= 0)
            throw new IllegalArgumentException("会社は必須です");
        if (e.getDate() == null || e.getDate().isBlank())
            throw new IllegalArgumentException("開催日は必須です");
        if (e.getDeadline() == null || e.getDeadline().isBlank())
            throw new IllegalArgumentException("締切は必須です");
        if (e.getPlace() == null || e.getPlace().isBlank())
            throw new IllegalArgumentException("場所は必須です");

        int newId = eventRepository.insert(e);
        e.setEventId(newId);
        return e;
    }

    // 説明会情報更新

    public Event update(Event e) {
        if (e.getEventId() <= 0)
            throw new IllegalArgumentException("eventId は必須です");
        if (e.getCompanyId() <= 0)
            throw new IllegalArgumentException("会社は必須です");
        if (e.getDate() == null || e.getDate().isBlank())
            throw new IllegalArgumentException("開催日は必須です");
        if (e.getDeadline() == null || e.getDeadline().isBlank())
            throw new IllegalArgumentException("締切は必須です");
        if (e.getPlace() == null || e.getPlace().isBlank())
            throw new IllegalArgumentException("場所は必須です");

        int updated = eventRepository.update(e);
        if (updated == 0)
            throw new IllegalArgumentException("対象の説明会が見つかりません");

        // JOINして companyName も返したいなら findById を取り直す
        return eventRepository.findById(e.getEventId());
    }

    //説明会削除
   public void delete(int eventId) {
    int cnt = eventRepository.countReservationsByEventId(eventId);
    if (cnt > 0) {
        throw new IllegalStateException("この説明会は予約されています（" + cnt + "件）。削除できません。");
    }

    int deleted = eventRepository.deleteById(eventId);
    if (deleted == 0)
        throw new IllegalArgumentException("対象の説明会が見つかりません");
}

}
