package com.team.management.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.management.project.repository.PlayerRepository;
import com.team.management.project.entity.Player;

@Service
public class PlayerServiceImpl implements PlayerService , UserDetailsService {

	private final PlayerRepository playerRepository;
	
	@Autowired
	public PlayerServiceImpl(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Player> findAll() {
		return playerRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Player findById(Long playerId) { //.orElseThrow Used to if null is returned
		return playerRepository.findById(playerId)
				.orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Player findByUsername(String username) {
		return playerRepository.findByUsername(username).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Player> findByTeam_TeamId(Long teamId) {
		List<Player> players = playerRepository.findByTeam_TeamId(teamId);
		if (players.isEmpty()) {
			throw new RuntimeException("No players found for team with id: " + teamId);
		}
		return players;
	}

	@Override
	@Transactional
	public Player save(Player player) {
		return playerRepository.save(player);
	}

	@Override
	@Transactional
	public void update(Player player) {
		if (!playerRepository.existsById(player.getPlayerId())) {
			throw new RuntimeException("Player not found with id: " + player.getPlayerId());
		}
		playerRepository.save(player);
	}

	@Override
	@Transactional
	public void deleteById(Long playerId) {
		if (!playerRepository.existsById(playerId)) {
			throw new RuntimeException("Player not found with id: " + playerId);
		}
		playerRepository.deleteById(playerId);
	}
	
	@Override
	@Transactional
	public void updatePlayerPassword(Long playerId, String newPassword) {
		Player player = findById(playerId);
		player.setPassword(newPassword); // Store plain text password because we encoded password in controller.
		playerRepository.save(player);
	}
	
	@Override
	@Transactional
	public void updatePlayerUsername(Long playerId, String newUsername) {
		if (!playerRepository.existsById(playerId)) {
			throw new RuntimeException("Player not found with id: " + playerId);
		}
		
		// Check if username is already in use by another player
		playerRepository.findByUsername(newUsername)
				.filter(player -> !player.getPlayerId().equals(playerId))
				.ifPresent(player -> {
					throw new RuntimeException("Username already exists: " + newUsername);
				});
		
		playerRepository.updateUsernameById(playerId, newUsername);
	}
	
	@Override
	@Transactional
	public void updatePlayerUsernameAndPassword(Long playerId, String newUsername, String newPassword) {
		Player player = findById(playerId);
		player.setUsername(newUsername);
		player.setPassword(newPassword); 
		playerRepository.save(player);
	}
	
	@Override
	@Transactional
	public void leaveTeam(Long playerId) {
		if (!playerRepository.existsById(playerId)) {
			throw new RuntimeException("Player not found with id: " + playerId);
		}
		playerRepository.leaveTeamById(playerId);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional <Player> user = playerRepository.findByUsername(username);
		if(user.isPresent()) {
			var userObject =  user.get();
			return User.builder().username(userObject.getUsername()).password(userObject.getPassword()).roles(userObject.getRole()).build();
		} else {
			throw new UsernameNotFoundException(username);
		}
			
			// set user for authentication and authorization.
		
	}

	@Override
	public List<Player> findStarPlayers() {
		return playerRepository.findAllByOrderByKillCountDesc();
	}
	
} 