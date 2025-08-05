package com.team.management.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.management.project.repository.PlayerRepository;
import com.team.management.project.repository.TeamRepository;
import com.team.management.project.repository.RequestRepository;
import com.team.management.project.entity.Player;
import com.team.management.project.entity.Team;
import com.team.management.project.entity.Request;

@Service
public class RequestServiceImpl implements RequestService {

	private final RequestRepository requestRepository;
	private final PlayerRepository playerRepository;
	private final TeamRepository teamRepository;
	private final TeamService teamService;
	
	@Autowired
	public RequestServiceImpl(RequestRepository requestRepository, 
							 PlayerRepository playerRepository, 
							 TeamRepository teamRepository,
							 TeamService teamService) {
		this.requestRepository = requestRepository;
		this.playerRepository = playerRepository;
		this.teamRepository = teamRepository;
		this.teamService = teamService;
	}
	
	@Override
	@Transactional
	public Request createRequest(Long playerId, Long teamId) {
		
		if (!playerRepository.existsById(playerId)) {
			throw new RuntimeException("Player not found with id: " + playerId);
		}
		
		
		if (!teamRepository.existsById(teamId)) {
			throw new RuntimeException("Team not found with id: " + teamId);
		}
		
		
		Player player = playerRepository.findById(playerId)
			.orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
			
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
		
		
		if (team.isTeamFull()) {
			throw new RuntimeException("Team is full and cannot accept more players");
		}
		
		
		if (player.getTeam() != null) {
			throw new RuntimeException("Player already belongs to a team");
		}
		
		
		if (requestRepository.existsByPlayerIdAndTeamId(playerId, teamId)) {
			throw new RuntimeException("Request already exists for this player and team");
		}
		
		
		Request request = new Request(playerId, teamId);
		return requestRepository.save(request);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Request> findRequestsByPlayerId(Long playerId) {
		
		if (!playerRepository.existsById(playerId)) {
			throw new RuntimeException("Player not found with id: " + playerId);
		}
		
		return requestRepository.findByPlayerId(playerId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Request> findRequestsByTeamId(Long teamId) {
		
		if (!teamRepository.existsById(teamId)) {
			throw new RuntimeException("Team not found with id: " + teamId);
		}
		
		return requestRepository.findByTeamId(teamId);
	}

	@Override
	@Transactional
	public void deleteRequestsByPlayerId(Long playerId) {
		
		if (!playerRepository.existsById(playerId)) {
			throw new RuntimeException("Player not found with id: " + playerId);
		}
		
		requestRepository.deleteByPlayerId(playerId);
	}

	@Override
	@Transactional
	public void deleteRequestsByTeamId(Long teamId) {
		
		if (!teamRepository.existsById(teamId)) {
			throw new RuntimeException("Team not found with id: " + teamId);
		}
		
		requestRepository.deleteByTeamId(teamId);
	}
	
	@Override
	@Transactional
	public void deleteRequest(Long playerId, Long teamId) {
		
		if (!requestRepository.existsByPlayerIdAndTeamId(playerId, teamId)) {
			throw new RuntimeException("Request not found for player id: " + playerId + " and team id: " + teamId);
		}
		
		requestRepository.deleteByPlayerIdAndTeamId(playerId, teamId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean requestExists(Long playerId, Long teamId) {
		return requestRepository.existsByPlayerIdAndTeamId(playerId, teamId);
	}

	@Override
	@Transactional
	public boolean acceptRequest(Long playerId, Long teamId) {
		
		if (!requestRepository.existsByPlayerIdAndTeamId(playerId, teamId)) {
			throw new RuntimeException("Request not found for player id: " + playerId + " and team id: " + teamId);
		}
		
		
		Player player = playerRepository.findById(playerId)
			.orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
		
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
		
		
		if (team.isTeamFull()) {
			throw new RuntimeException("Team is full and cannot accept more players");
		}
		
		
		if (player.getTeam() != null) {
			throw new RuntimeException("Player already belongs to a team");
		}
		
		
		player.setTeam(team);
		
		
		playerRepository.save(player);
		
		requestRepository.deleteByPlayerId(playerId);
		
		requestRepository.deleteByPlayerIdAndTeamId(playerId, teamId);
		
		if (team.isTeamFull()) {
			requestRepository.deleteByTeamId(teamId);
		}
		
		return true;
	}
} 