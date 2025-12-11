package com.no1project.reservation.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // バリデーション系
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage()); // 400
    }

    // 状態エラー（締切超過・二重予約など）
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()); // 409
    }

    // 外部キー制約などのDB制約違反
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException ex) {
        // 本番ならログだけ出してメッセージはマイルドに
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("データ整合性エラー（ユーザーまたはイベントが存在しません）。");
    }
}
