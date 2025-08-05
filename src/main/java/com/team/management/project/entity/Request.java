package com.team.management.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "request", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"player_id", "team_id"})
})
public class Request {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;
    
    @Column(name = "player_id", nullable = false)
    private Long playerId;
    
    @Column(name = "team_id", nullable = false)
    private Long teamId;
    
    // Default constructor required by JPA
    public Request() {
    }
    
    // Constructor with fields
    public Request(Long playerId, Long teamId) {
        this.playerId = playerId;
        this.teamId = teamId;
    }
    
    // Getters and setters
    public Long getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    
    public Long getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
    
    public Long getTeamId() {
        return teamId;
    }
    
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    
    @Override
    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", playerId=" + playerId +
                ", teamId=" + teamId +
                '}';
    }
} 