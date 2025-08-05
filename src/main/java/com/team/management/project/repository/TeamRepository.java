package com.team.management.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.management.project.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    
    
    Optional<Team> findByTeamName(String teamName);
    
    
    List<Team> findByNumberOfPlayersLessThan(int maxPlayers);
    
    
    List<Team> findByNumberOfPlayersEquals(int playerCount);
    
    
    boolean existsByTeamIdAndNumberOfPlayersLessThan(Long teamId, int maxPlayers);
    
    @Query("SELECT t FROM Team t ORDER BY t.numberOfWins DESC")
    List<Team> findAllTeamsSortedByWins();
    
    @Modifying
    @Query("UPDATE Team t SET t.teamName = :teamName WHERE t.teamId = :id")
    void updateTeamNameById(@Param("id") Long id, @Param("teamName") String teamName);
    
    
    @Modifying
    @Query("UPDATE Team t SET t.teamPassword = :teamPassword WHERE t.teamId = :id")
    void updateTeamPasswordById(@Param("id") Long id, @Param("teamPassword") String teamPassword);
    
    
    @Modifying
    @Query("UPDATE Team t SET t.teamName = :teamName, t.teamPassword = :teamPassword WHERE t.teamId = :id")
    void updateTeamNameAndPasswordById(@Param("id") Long id, @Param("teamName") String teamName, @Param("teamPassword") String teamPassword);
} 