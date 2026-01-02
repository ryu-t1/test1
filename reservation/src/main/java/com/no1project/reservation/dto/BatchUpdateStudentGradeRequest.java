package com.no1project.reservation.dto;

public class BatchUpdateStudentGradeRequest {
    // +1 か -1 だけ許可する（フロントもこれしか送らない）
    private int delta; // 1 or -1

    public int getDelta() { return delta; }
    public void setDelta(int delta) { this.delta = delta; }
}