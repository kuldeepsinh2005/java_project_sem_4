package com.team.management.project.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team.management.project.entity.Player;
import com.team.management.project.entity.Team;
import com.team.management.project.entity.Request;
import com.team.management.project.service.PlayerService;
import com.team.management.project.service.RequestService;
import com.team.management.project.service.TeamService;

@Controller
@RequestMapping("/api")
public class RequestRestController {

	private final RequestService requestService;
	private final PlayerService playerService;
	private final TeamService teamService;
	
	@Autowired
	public RequestRestController(RequestService requestService, PlayerService playerService, TeamService teamService) {
		this.requestService = requestService;
		this.playerService = playerService;
		this.teamService = teamService;
	}
	
	@GetMapping("/requests/page")
	public String rederRequest(Model theModel) {		
		 
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.isAuthenticated()) {
				String username = authentication.getName();   
			    Player player = playerService.findByUsername(username);
				List<Team> teams=teamService.findAvailableTeams(4);
				theModel.addAttribute("player", player);
			    theModel.addAttribute("teams", teams);
				return "auth/add-request";
			}
			theModel.addAttribute("error", "Some error has occured at rederRequest");
			return "players/error";
		
	}
	@PostMapping("/requests/player/addrequest")
	public String addRequest(
			@RequestParam("playerId") Long playerId,
			@RequestParam("teamId") Long teamId,
	        Model theModel) {
	    Player player = playerService.findById(playerId);
	    Team team = teamService.findById(teamId);
	    List<Team> teams=teamService.findAvailableTeams(4);

		theModel.addAttribute("player", player);
        theModel.addAttribute("teams", teams);

	    if (player == null) {
	    	theModel.addAttribute("error", "Player not found.");
	        return "auth/add-request";
	    }

	    
	    
	    if (team == null) {
	    	theModel.addAttribute("error", "Team not found.");
	        return "auth/add-request";
	    }

	    
	    if (team.isTeamFull()) {
	    	theModel.addAttribute("error", "Team is full and cannot accept more players.");
	        return "auth/add-request";
	    }

	    
	    if (player.getTeam() != null) {
	    	theModel.addAttribute("error", "Player already belongs to a team.");
	        return "auth/add-request";
	    }

	    
	    if (requestService.requestExists(playerId, teamId)) {
	    	theModel.addAttribute("error", "Request already exists.");
	        return "auth/add-request";
	    }


	    Request request = requestService.createRequest(playerId, teamId);
        theModel.addAttribute("success", "Request sent successfully!");

	    return "auth/add-request";
	}
	
	// Accept a request (team leader accepts player's request to join)
	@PostMapping("/requests/accept")
	public String acceptRequest(@RequestParam("leaderId") Long leaderId,
			@RequestParam("playerId") Long playerId,
			@RequestParam("teamId")Long teamId,
			Model model) {
		
		
		if (!requestService.requestExists(playerId, teamId)) {
			model.addAttribute("error","request doesn't exists");
			return "players/error";
		}
		
		try {
			
			Player player = playerService.findById(leaderId);
			Team team = teamService.findById(player.getTeam().getTeamId());
			if (team.isTeamFull()) {
				throw new RuntimeException();
			}
			boolean accepted = requestService.acceptRequest(playerId, teamId);
			if (accepted) {
				team.incrementPlayerCount();
				teamService.update(team);
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
				model.addAttribute("error","some error");
				return "players/error";
			}
		} catch (RuntimeException e) {
			model.addAttribute("error","Your team is full you can't accept new member go back");
			return "players/error";
		}
	}
	@PostMapping("/requests/reject")
	public String rejectRequest(@RequestParam("leaderId") Long leaderId,
			@RequestParam("playerId") Long playerId,
			@RequestParam("teamId")Long teamId,
			Model model) {
		
		if (!requestService.requestExists(playerId, teamId)) {
			model.addAttribute("error","request doesn't exists");
			return "players/error";
		}
		
		try {
			requestService.deleteRequest(playerId, teamId);
			
			
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
			
		} catch (RuntimeException e) {
			model.addAttribute("error","some error");
			return "players/error";
		}
	}
} 