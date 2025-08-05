package com.team.management.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.management.project.repository.TeamRepository;
import com.team.management.project.repository.PlayerRepository;
import com.team.management.project.entity.Team;
import com.team.management.project.entity.Player;

@Service
public class TeamServiceImpl implements TeamService {

	private final TeamRepository teamRepository;
	private final PlayerRepository playerRepository;
	
	@Autowired
	public TeamServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository) {
		this.teamRepository = teamRepository;
		this.playerRepository = playerRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Team> findAll() {
		return teamRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Team findById(Long teamId) {
		return teamRepository.findById(teamId)
				.orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Team findByName(String teamName) {
		return teamRepository.findByTeamName(teamName).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Team> findAvailableTeams(int maxPlayers) {
		return teamRepository.findByNumberOfPlayersLessThan(maxPlayers);
	}

	@Override
	@Transactional
	public Team save(Team team) {
		return teamRepository.save(team);
	}

	@Override
	@Transactional
	public void update(Team team) {
		if (!teamRepository.existsById(team.getTeamId())) {
			throw new RuntimeException("Team not found with id: " + team.getTeamId());
		}
		teamRepository.save(team);
	}

	@Override
	@Transactional
	public void deleteById(Long teamId) {
		if (!teamRepository.existsById(teamId)) {
			throw new RuntimeException("Team not found with id: " + teamId);
		}
		teamRepository.deleteById(teamId);
	}
	
	@Override
	@Transactional
	public void updateTeamName(Long teamId, String newTeamName) {
		if (!teamRepository.existsById(teamId)) {
			throw new RuntimeException("Team not found with id: " + teamId);
		}
		
		// Check if team name is already in use
		teamRepository.findByTeamName(newTeamName)
				.filter(team -> !team.getTeamId().equals(teamId))
				.ifPresent(team -> {
					throw new RuntimeException("Team name already exists: " + newTeamName);
				});
		
		teamRepository.updateTeamNameById(teamId, newTeamName);
	}
	
	@Override
	@Transactional
	public void updateTeamPassword(Long teamId, String newPassword) {
		Team team = findById(teamId);
		team.setTeamPassword(newPassword); 
		teamRepository.save(team);
	}
	
	@Override
	@Transactional
	public void updateTeamNameAndPassword(Long teamId, String newName, String newPassword) {
		Team team = findById(teamId);
		team.setTeamName(newName);
		team.setTeamPassword(newPassword); 
		teamRepository.save(team);
	}
	
	@Override
	@Transactional
	public void removePlayerFromTeam(Long playerId) {
		if (!playerRepository.existsById(playerId)) {
			throw new RuntimeException("Player not found with id: " + playerId);
		}
		playerRepository.leaveTeamById(playerId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean teamHasSpace(Long teamId, int maxPlayers) {
		return teamRepository.existsByTeamIdAndNumberOfPlayersLessThan(teamId, maxPlayers);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Team> findTeamsWithExactPlayers(int playerCount) {
		return teamRepository.findByNumberOfPlayersEquals(playerCount);
	}
	@Override
	@Transactional(readOnly = true)
	public List<Team> findStarteam(){
		return teamRepository.findAllTeamsSortedByWins();

	}
} 