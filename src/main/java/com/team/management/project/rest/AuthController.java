package com.team.management.project.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.team.management.project.entity.Player;
import com.team.management.project.entity.Request;
import com.team.management.project.entity.Team;
import com.team.management.project.service.PlayerService;
import com.team.management.project.service.RequestService;
import com.team.management.project.service.TeamService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final RequestService requestService;

    @Autowired
    public AuthController(PlayerService playerService, TeamService teamService,RequestService requestService) {
        this.playerService = playerService;
        this.teamService = teamService;
		this.requestService = requestService;

    }
    
  @GetMapping("/leader-page")  
  public String leaderPage(Model model) {
	  
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    if (authentication != null && authentication.isAuthenticated()) {
	        String username = authentication.getName();   // Returns the username
	        Player player = playerService.findByUsername(username);
	     
            Team team = player.getTeam();
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
	    } 
	    model.addAttribute("error", "You are not team leader");
	  return "players/error";
  }
    
  
@GetMapping("/member-page") 
public String memberPage(Model model) {
	
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (authentication != null && authentication.isAuthenticated()) {
        String username = authentication.getName();   
        Player player = playerService.findByUsername(username);
     
        Team team = player.getTeam();
        if(team!=null&&!player.getRole().equals("LEADER")) {
	        List<Player> teamMembers = playerService.findByTeam_TeamId(team.getTeamId());
	        model.addAttribute("player", player);
	        model.addAttribute("team", team != null ? team : new Team());
	        model.addAttribute("teamMembers", teamMembers != null ? teamMembers : List.of());
	        return "auth/team-member";
        }
    }
	model.addAttribute("error", "You are not team member");
	return "players/error";
}
@GetMapping("/solo-page") 
public String soloPage(Model model) {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (authentication != null && authentication.isAuthenticated()) {
        String username = authentication.getName();   
        Player player = playerService.findByUsername(username);
     
        Team team = player.getTeam();
        if(team==null) {
        	List<Team> teams=teamService.findAvailableTeams(4);
            model.addAttribute("player", player);
            model.addAttribute("teams", teams);
	        return "auth/solo-player";
        }
    } 
	model.addAttribute("error", "You are not solo player");
	return "players/error";
}

} 