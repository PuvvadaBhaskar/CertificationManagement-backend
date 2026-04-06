package com.project.dto;

public class DashboardResponseDto {

    private long total;
    private long active;
    private long expired;
    private long expiringSoon;

    public DashboardResponseDto(long total, long active, long expired, long expiringSoon) {
        this.total = total;
        this.active = active;
        this.expired = expired;
        this.expiringSoon = expiringSoon;
    }

    public long getTotal() { return total; }
    public long getActive() { return active; }
    public long getExpired() { return expired; }
    public long getExpiringSoon() { return expiringSoon; }
}