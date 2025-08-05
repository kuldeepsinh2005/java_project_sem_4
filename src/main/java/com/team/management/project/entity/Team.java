package com.team.management.project.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(unique = true, nullable = false)
    private String teamName;

    @Column(name = "team_password")
    private String teamPassword;

    private int numberOfPlayers;
    private int numberOfWins;
    
    public static final int MAX_TEAM_SIZE = 4;

    public Team() {
        this.numberOfPlayers = 0;
        this.numberOfWins = 0;
    }

    public Team(Long teamId, String teamName, String teamPassword, int numberOfPlayers, int numberOfWins) {
        super();
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamPassword = teamPassword;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfWins = numberOfWins;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamPassword() {
        return teamPassword;
    }

    public void setTeamPassword(String teamPassword) {
        this.teamPassword = teamPassword;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    @Override
    public String toString() {
        return "Team [ teamId=" + teamId + "\n, teamName=" + teamName + "\n, teamPassword=" + teamPassword
                + "\n, numberOfPlayers=" + numberOfPlayers + "\n, numberOfWins=" + numberOfWins 
                + " ]";
    }

    public boolean canAcceptPlayer() {
        return this.numberOfPlayers < MAX_TEAM_SIZE;
    }

    public void incrementPlayerCount() {
        if (this.numberOfPlayers < MAX_TEAM_SIZE) {
            this.numberOfPlayers++;
        }
    }
    
    public void decrementPlayerCount() {
        if (this.numberOfPlayers > 1) {
            this.numberOfPlayers--;
        }
    }

    public boolean isTeamFull() {
        return this.numberOfPlayers >= MAX_TEAM_SIZE;
    }
}