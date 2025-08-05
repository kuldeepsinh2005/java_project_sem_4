package com.team.management.project.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.team.management.project.entity.Team;
import com.team.management.project.service.TeamService;
import com.team.management.project.entity.Player;
import com.team.management.project.entity.Request;
import com.team.management.project.service.PlayerService;
import com.team.management.project.service.RequestService;

@Controller
@RequestMapping("/teams")
public class TeamRestController {

	private final TeamService teamService;
	private PlayerService playerService;
	private RequestService requestService;

	@Autowired
	public TeamRestController(TeamService teamService, PlayerService playerService, RequestService requestService) {
		this.teamService = teamService;
		this.playerService = playerService;
		this.requestService = requestService;
	}
	@GetMapping("/team-all")
	public String showAllTeams(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();   // Returns the username
		    Player player = playerService.findByUsername(username);
		    List<Team> teams = teamService.findAll();
		    model.addAttribute("teams", teams);
		    Player players = playerService.findById(player.getPlayerId());
		    model.addAttribute("currentplayer", players);
		    return "teams/all-teams";
		}
 
		return "players/error";
	    
	}

	@PostMapping("/save")
	public String saveTeam(@ModelAttribute("team") Team theTeam,Model model) {
		
		teamService.save(theTeam);
		return "redirect:/teams/list";
	}

	@PostMapping("/remove-player")
	public String removePlayerFromTeam(@RequestParam Long teamId, @RequestParam Long playerId,@RequestParam Long leaderId,Model model) {
		try {
			
			if(playerId!=leaderId) {	//means leader is removing player from team.
				teamService.removePlayerFromTeam(playerId);
				Team currentteam = teamService.findById(teamId);
				currentteam.decrementPlayerCount();
				teamService.update(currentteam);
				Player player = playerService.findById(leaderId);
				Team team = teamService.findById(player.getTeam().getTeamId());
				List<Player> teamMembers = playerService.findByTeam_TeamId(team.getTeamId());
	            List<Request> joinRequests = requestService.findRequestsByTeamId(team.getTeamId());
	            
	            Map<Long, Player> playersMap = new HashMap<>();
	            if (joinRequests != null) {
	                for (Request request : joinRequests) {
	                    Player requester = playerService.findById(request.getPlayerId());
	                    if (requester != null) {
	                        playersMap.put(request.getPlayerId(), requester);
	                    }
	                }
	            }

	            model.addAttribute("leader", player != null ? player : new Player());
	            model.addAttribute("team", team != null ? team : new Team());
	            model.addAttribute("teamMembers", teamMembers != null ? teamMembers : List.of());
	            model.addAttribute("joinRequests", joinRequests != null ? joinRequests : List.of());
	            model.addAttribute("playersMap", playersMap);

				return "auth/team-leader";
			} else {	// player is leaving team
				teamService.removePlayerFromTeam(playerId);
				Team currentteam = teamService.findById(teamId);
				List<Team> teams=teamService.findAvailableTeams(4);
				currentteam.decrementPlayerCount();
				teamService.update(currentteam);
				Player player = playerService.findById(leaderId);
				model.addAttribute("player", player);
				model.addAttribute("teams", teams);
				return "auth/solo-player";
			}
		} catch (Exception e) {
			model.addAttribute("error", "Some thing went wrong at remove player from team");
			return "players/error";
		}
	}
	
	// Update an existing team
	@PostMapping("/update")
	public String updateTeam(@ModelAttribute Team team,@RequestParam("playerId") Long leaderId,Model model) {
		try {
			if(team!=null) {// here we are not encoding team password because all player login using player password that is already encoded.
				Team temp = teamService.findById(team.getTeamId());
				team.setNumberOfWins(temp.getNumberOfWins());
				teamService.update(team);
				Player player = playerService.findById(leaderId);
				List<Player> teamMembers = playerService.findByTeam_TeamId(team.getTeamId());
				List<Request> joinRequests = requestService.findRequestsByTeamId(team.getTeamId());
		        
		        Map<Long, Player> playersMap = new HashMap<>();
		        if (joinRequests != null) {
		            for (Request request : joinRequests) {
		                Player requester = playerService.findById(request.getPlayerId());
		                if (requester != null) {
		                    playersMap.put(request.getPlayerId(), requester);
		                }
		            }
		        }
		        
		        model.addAttribute("leader", player != null ? player : new Player());
		        model.addAttribute("team", team != null ? team : new Team());
		        model.addAttribute("teamMembers", teamMembers != null ? teamMembers : List.of());
		        model.addAttribute("joinRequests", joinRequests != null ? joinRequests : List.of());
		        model.addAttribute("playersMap", playersMap);
		        
		        return "auth/team-leader";
			} else {
				model.addAttribute("error","Same Team Name Already Exist");
				return "players/error";		
			}
		} catch (Exception e) {
    		model.addAttribute("error","Same Team Name Already Exist");
			return "players/error";		
		}
	}
	
	// Delete a team
	@PostMapping("/delete")
	public String deleteTeam(@RequestParam Long teamId) {
		
		Team team = teamService.findById(teamId);
		if (team == null) {
			throw new RuntimeException("Team id not found - " + teamId);
		}
		List<Player> players = playerService.findByTeam_TeamId(teamId);
		for(Player temp : players) {
			temp.setTeam(null);
			if(temp.getRole().equals("LEADER")) {
				temp.setRole("HEALER");
			}
			playerService.update(temp);
		}
		requestService.deleteRequestsByTeamId(teamId);
		teamService.deleteById(teamId);
		
		return "redirect:/login";
	}
	
} 