package com.team.management.project.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.team.management.project.entity.Player;
import com.team.management.project.entity.Team;
import com.team.management.project.service.PlayerService;
import com.team.management.project.service.RequestService;
import com.team.management.project.service.TeamService;

@Controller
@RequestMapping("/players")
public class PlayerController {

    private PlayerService playerService;
    private TeamService teamService;
    private RequestService requestService;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public PlayerController(PasswordEncoder passwordEncoder,RequestService requestService,PlayerService thePlayerService, TeamService theTeamService) {
        playerService = thePlayerService;
        teamService = theTeamService;
        this.requestService = requestService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register-solo")
    public String showRegistrationForm(Model model) {
        model.addAttribute("player", new Player());
        return "players/register-solo";
    }

    @PostMapping("/register-solo")
    public String registerSoloPlayer(@ModelAttribute Player player, Model model) {
        try {

        	player.setTeam(null);
        	player.setRole(player.getRole().toUpperCase());
        	player.setPassword(passwordEncoder.encode(player.getPassword()));
        	playerService.save(player);
            model.addAttribute("message", "Registration successful! Please log in.");
            model.addAttribute("player", player);

            return "redirect:/login";  
        } catch (Exception e) {
            model.addAttribute("error", "Same username already exist");
            System.out.println("Some thing is wrong kd");
            return "players/error";
        }
    }
    
    @GetMapping("/create-team")
    public String showCreateTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("player",new Player());
        return "players/register-team";
    }

    @PostMapping("/create-team")
    public String createTeam(@ModelAttribute Team team,@ModelAttribute Player player ,Model model) {
    	 try {
    		 
    		 Team cureentTeam = teamService.findByName(team.getTeamName());
             Player currentPlayer = playerService.findByUsername(player.getUsername());
             if(currentPlayer!=null||cureentTeam!=null) {
            	 throw new Exception();
             }
	        team.setNumberOfPlayers(1); // Leader is the first player
	        team.setNumberOfWins(0);
	        teamService.save(team);
	
	        // Set player as team leader
	        player.setRole("LEADER");
	        player.setPassword(passwordEncoder.encode(player.getPassword()));
	        player.setTeam(team);
	        playerService.save(player);
	        model.addAttribute("message", "Registration successful! Please log in.");
	        return "redirect:/login"; // Redirect to login page after team creation
    	 } catch (Exception e) {
             model.addAttribute("error", "Same username or team name already exist");
             System.out.println("Some thing is wrong kd");
             return "players/error";
         }
    }

    @GetMapping("/all-list")
    public String showAllPlayers(Model model) {
    	
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 		if (authentication != null && authentication.isAuthenticated()) {
 			String username = authentication.getName();   // Returns the username
 		    Player player = playerService.findByUsername(username);
 		    List<Player> players = playerService.findAll();
 	        model.addAttribute("players", players);
 	        return "players/all-players";
 		}
    	return "players/error";
        
    }
    
    @PostMapping("/update")
    public String updatePlayer(@ModelAttribute("player") Player thePlayer,Model model) {
    	try {
	    	Player temp = playerService.findById(thePlayer.getPlayerId());
	    	temp.setUsername(thePlayer.getUsername());
	    	System.out.println(thePlayer.getPassword());
	    	temp.setPassword(passwordEncoder.encode(thePlayer.getPassword()));
	    	temp.setRole(thePlayer.getRole().toUpperCase());
	    	if(temp.getTeam()!=null)
	    		thePlayer.setTeam(temp.getTeam());
	        // save the player
	        playerService.save(temp);
	        
	        // use a redirect to prevent duplicate submissions
	        return "redirect:/login";
    	} catch (Exception e) {
    		model.addAttribute("error","Same Username Already exist");
			return "players/error";		
		}
    }
    
    @PostMapping("/delete")
    public String delete(@RequestParam("playerId") Long theId,Model model) {
        
    	Player currentplayer = playerService.findById(theId);
    	if(currentplayer.getTeam()!=null) {
    		Team team = teamService.findById(currentplayer.getTeam().getTeamId());
    		team.decrementPlayerCount();
    		teamService.update(team);
    	} else {
    		requestService.deleteRequestsByPlayerId(theId);
    	}
        playerService.deleteById(theId);
        model.addAttribute("error", "Account Deleted successfully");
        return "players/error";
    }
    
	@GetMapping("/dashboard")
	public String dashboard() {
		return "players/dashboard";
	}

} 