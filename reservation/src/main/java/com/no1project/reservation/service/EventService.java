package com.no1project.reservation.service;

import com.no1project.reservation.model.Event;
import com.no1project.reservation.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // ★追加：ページング + 検索
    public EventPage getEventPage(int page, int size, String q, String from, String to) {
        if (page < 0)
            page = 0;
        if (size < 1)
            size = 10;

        // ★「今年度3/1〜次年度3/31」範囲を作る（自動で毎年切り替え）
        FiscalRange range = calcFiscalRangeMarchToMarch();

        List<Event> events = eventRepository.findPageFiltered(
                page, size,
                range.start, range.end,
                q, from, to);

        int totalCount = eventRepository.countFiltered(range.start, range.end, q, from, to);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        return new EventPage(events, page, size, totalCount, totalPages);
    }

    // ★年度範囲（3/1〜翌3/31）計算
    private FiscalRange calcFiscalRangeMarchToMarch() {
        LocalDate today = LocalDate.now();

        // 3月開始：1〜2月は「前年の3月開始」
        int startYear = (today.getMonthValue() >= 3) ? today.getYear() : today.getYear() - 1;

        LocalDateTime start = LocalDateTime.of(startYear, 3, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(startYear + 1, 3, 31, 23, 59, 59);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new FiscalRange(start.format(fmt), end.format(fmt));
    }

    private record FiscalRange(String start, String end) {
    }

    public static class EventPage {
        private List<Event> events;
        private int page;
        private int size;
        private int totalCount;
        private int totalPages;

        public EventPage(List<Event> events, int page, int size, int totalCount, int totalPages) {
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

    // 説明会削除
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
