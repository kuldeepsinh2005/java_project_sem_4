package com.team.management.project.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.management.project.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
    
    
    List<Request> findByPlayerId(Long playerId);
    
    
    List<Request> findByTeamId(Long teamId);
    
    
    Optional<Request> findByPlayerIdAndTeamId(Long playerId, Long teamId);
    
    
    boolean existsByPlayerIdAndTeamId(Long playerId, Long teamId);
    
    
    void deleteByPlayerId(Long playerId);
    
    
    void deleteByTeamId(Long teamId);
    
    
    void deleteByPlayerIdAndTeamId(Long playerId, Long teamId);
} 