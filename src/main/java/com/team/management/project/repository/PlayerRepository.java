package com.team.management.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.management.project.entity.Player;
import com.team.management.project.entity.Team;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    
    Optional<Player> findByUsername(String username);
    
    
    List<Player> findByTeam_TeamId(Long teamId);
    
    List<Player> findAllByOrderByKillCountDesc();
    
    @Modifying
    @Query("UPDATE Player p SET p.password = :password WHERE p.playerId = :id")
    void updatePasswordById(@Param("id") Long id, @Param("password") String password);
    
    
    @Modifying
    @Query("UPDATE Player p SET p.username = :username WHERE p.playerId = :id")
    void updateUsernameById(@Param("id") Long id, @Param("username") String username);
    
    
    @Modifying
    @Query("UPDATE Player p SET p.username = :username, p.password = :password WHERE p.playerId = :id")
    void updateUsernameAndPasswordById(@Param("id") Long id, @Param("username") String username, @Param("password") String password);
    
    // Leave team (set team to null)
    @Modifying
    @Query("UPDATE Player p SET p.team = NULL WHERE p.playerId = :id")
    void leaveTeamById(@Param("id") Long id);
} 