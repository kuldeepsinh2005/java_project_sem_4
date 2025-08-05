package com.team.management.project.service;

import java.util.List;

import com.team.management.project.entity.Request;

public interface RequestService {

	Request createRequest(Long playerId, Long teamId);

	List<Request> findRequestsByPlayerId(Long playerId);

	List<Request> findRequestsByTeamId(Long teamId);
	
	void deleteRequestsByPlayerId(Long playerId);
	
	void deleteRequestsByTeamId(Long teamId);
	
	void deleteRequest(Long playerId, Long teamId);
	
	boolean requestExists(Long playerId, Long teamId);
	
	boolean acceptRequest(Long playerId, Long teamId);
	
} 