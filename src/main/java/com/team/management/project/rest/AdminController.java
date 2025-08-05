package com.team.management.project.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.management.project.entity.Player;
import com.team.management.project.entity.Team;
import com.team.management.project.service.PlayerService;
import com.team.management.project.service.TeamService;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    
    private PlayerService playerService;
    private TeamService teamService;
    
    @Autowired
    public AdminController(PlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }
    
    @GetMapping("/list")
    public String listPlayers(Model theModel) {
        
        List<Player> thePlayers = playerService.findAll();

        
        theModel.addAttribute("players", thePlayers);

        return "players/list-players";
    }
    @PostMapping("/showFormForUpdate")
	   String showFormForUpdate(@RequestParam("playerId") Long theId,@RequestParam int killCount,
	                                    Model theModel) {
	        
	        Player thePlayer = playerService.findById(theId);
	        thePlayer.setKillCount(killCount);
	        playerService.update(thePlayer);
	        List<Player> thePlayers = playerService.findAll();

	        
	        theModel.addAttribute("players", thePlayers);
	        
	        theModel.addAttribute("player", thePlayer);

	        
	        return "players/list-players";
	    }
	  
		@GetMapping("/qualified")
		public String getQualifiedTeams(Model model) {

			List<Team> teams = teamService.findTeamsWithExactPlayers(Team.MAX_TEAM_SIZE);
			model.addAttribute("teams", teams);
			return "teams/qualified-team";
		}
		@PostMapping("/updateWinCount")
		public String updateWinCount(@RequestParam("teamId") Long theId,@RequestParam int numberOfWins, Model theModel) {
			Team theTeam = teamService.findById(theId);
			theTeam.setNumberOfWins(numberOfWins);
			teamService.update(theTeam);
			theModel.addAttribute("team", theTeam);
			List<Team> teams = teamService.findTeamsWithExactPlayers(Team.MAX_TEAM_SIZE);
			theModel.addAttribute("teams", teams);
			return "teams/qualified-team";
		}
		@GetMapping("/starteams")
		public String getStarTeams(Model model) {

			List<Team> teams = teamService.findStarteam();
			model.addAttribute("teams", teams);
			return "teams/starteam";
		}
		@GetMapping("/starplayers")
		public String getStarPlayers(Model model) {

			List<Player> players = playerService.findStarPlayers();
			List<Player> temp = new ArrayList<Player>();
			if(players.size()>10) {
				for(int i = 0;i<10;i++) {
					temp.add((players.get(i)));
				}
				players = temp;
//				System.out.println(players);
			}
			model.addAttribute("players", players);
			return "players/starplayer";
		}
} 