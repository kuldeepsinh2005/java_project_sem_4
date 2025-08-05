package com.team.management.project.service;

import java.util.List;

import com.team.management.project.entity.Player;

public interface PlayerService {

	public List<Player> findAll();
	
	public Player findById(Long playerId);
	
	public Player findByUsername(String username);
	
	public List<Player> findByTeam_TeamId(Long teamId);
	
	List<Player> findStarPlayers();
	
	public Player save(Player player);
	
	public void update(Player player);
	
	public void deleteById(Long playerId);
	
	public void updatePlayerPassword(Long playerId, String newPassword);
	
	public void updatePlayerUsername(Long playerId, String newUsername);
	
	public void updatePlayerUsernameAndPassword(Long playerId, String newUsername, String newPassword);
	
	public void leaveTeam(Long playerId);
} 