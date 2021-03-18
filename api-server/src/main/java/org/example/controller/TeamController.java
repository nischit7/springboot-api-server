package org.example.controller;

import org.example.domain.teams.TeamDetails;
import org.example.services.TeamService;
import org.example.support.ApiUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Team controller REST API.
 */
@RestController
@RequestMapping(ApiUrls.TEAMS_API_URI)
public class TeamController {

    private static final String TEAM_ID = "teamId";
    private static final String TEAM_ID_URI = "/{" + TEAM_ID + "}";

    private final TeamService teamService;

    @Autowired
    public TeamController(final TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Creates the team using details provided by {@link TeamDetails}.
     *
     * @param teamDetails An instance of {@link TeamDetails}.
     * @return An instance of {@link ResponseEntity}.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTeam(@RequestBody final TeamDetails teamDetails) {
        teamService.createTeam(teamDetails);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    /**
     * Returns team information.
     *
     * @param teamId Represents the team id.
     * @return An instance of {@link ResponseEntity} holding {@link TeamDetails}.
     */
    @GetMapping(value = TEAM_ID_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDetails> getTeamInfo(@PathVariable(TEAM_ID) final String teamId) {
        return new ResponseEntity<>(teamService.getTeamInfo(teamId), HttpStatus.OK);
    }

    /**
     * Deletes the team information.
     *
     * @param teamId Represents the team id.
     * @return An instance of {@link ResponseEntity}.
     */
    @DeleteMapping(value = TEAM_ID_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTeam(@PathVariable(TEAM_ID) final String teamId) {
        teamService.deleteTeam(teamId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
