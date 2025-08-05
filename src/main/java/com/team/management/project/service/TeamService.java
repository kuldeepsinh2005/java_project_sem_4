package com.team.management.project.service;

import java.util.List;

import com.team.management.project.entity.Player;
import com.team.management.project.entity.Team;

public interface TeamService {

	public List<Team> findAll();
	
	public List<Team> findStarteam();
	
	public Team findById(Long teamId);
	
	public Team findByName(String teamName);
	
	public List<Team> findAvailableTeams(int maxPlayers);
	
	public List<Team> findTeamsWithExactPlayers(int playerCount);
	
	public Team save(Team team);
	
	public void update(Team team);
	
	public void deleteById(Long teamId);
	
	public void updateTeamName(Long teamId, String newTeamName);
	
	public void updateTeamPassword(Long teamId, String newTeamPassword);
	
	public void updateTeamNameAndPassword(Long teamId, String newTeamName, String newTeamPassword);
	
	public void removePlayerFromTeam(Long playerId);
	
	public boolean teamHasSpace(Long teamId, int maxPlayers);

} 