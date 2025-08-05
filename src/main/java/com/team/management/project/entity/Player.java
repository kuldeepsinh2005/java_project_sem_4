package com.team.management.project.entity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(nullable = false)
    private String role; 

    private int killCount;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;

    public Player() {
        this.team = null; // Initialize as solo player
    }
    
    public Player(Long playerId, String username, String password, String role, int killCount, Team team) {
        super();
        this.playerId = playerId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.killCount = killCount;
        this.team = team;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Player [ playerId=" + playerId + "\n, username=" + username + "\n, password=" + password + "\n, role=" + role
                + "\n, killCount=" + killCount + "\n, team=" + team + " ]";
    }
    
    public boolean isSoloPlayer() {
        return this.team == null;
    }
    
    public boolean isTeamLeader() {
        return "LEADER".equals(this.role);
    }

    public void makeTeamLeader() {
        this.role = "LEADER";
    }

    public void removeFromTeam() {
        this.team = null;
    }
}
