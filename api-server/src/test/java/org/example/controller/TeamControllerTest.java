package org.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.example.domain.teams.TeamDetails;
import org.example.services.TeamService;
import org.example.services.impl.TeamNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @Mock
    private TeamService mockTeamService;

    private TeamController teamController;

    @BeforeEach
    void setUp() {
        this.teamController = new TeamController(this.mockTeamService);
    }

    @Test
    @DisplayName("When team creation is successful")
    public void createTeamSucceeds() {
        final TeamDetails teamDetails = TeamDetails.builder()
                .teamDesc("some team")
                .teamId("US123")
                .teamName("Phenoix")
                .build();
        final ResponseEntity<Void> voidResponseEntity = this.teamController.createTeam(teamDetails);
        assertThat(voidResponseEntity.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        verify(this.mockTeamService).createTeam(ArgumentMatchers.any(TeamDetails.class));
    }

    @Test
    @DisplayName("When team deletion is successful")
    public void deleteTeamSucceeds() {
        final ResponseEntity<Void> voidResponseEntity = this.teamController.deleteTeam("US123");
        assertThat(voidResponseEntity.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        verify(this.mockTeamService).deleteTeam(eq("US123"));
    }

    @Test
    @DisplayName("When team retrieval is successful")
    public void teamRetrievalSucceeds() {
        when(this.mockTeamService.getTeamInfo(eq("US123"))).thenReturn(TeamDetails.builder()
                .teamDesc("some team")
                .teamId("US123")
                .teamName("Phenoix")
                .build());
        final ResponseEntity<TeamDetails> teamDetailsResponseEntity = this.teamController.getTeamInfo("US123");
        assertThat(teamDetailsResponseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(teamDetailsResponseEntity.getBody().getTeamId(), equalTo("US123"));
    }

    @Test
    @DisplayName("When team is not found")
    public void teamIsNotFound() {
        when(this.mockTeamService.getTeamInfo(eq("US123"))).thenThrow(new TeamNotFoundException());
        Assertions.assertThrows(TeamNotFoundException.class, () -> this.teamController.getTeamInfo("US123"));
    }

    @Test
    @DisplayName("When team is not found")
    public void whenGetAllTeamSucceeds() {
        final TeamDetails teamDetails1 = TeamDetails.builder()
                .teamId("1")
                .teamName("11")
                .teamDesc("11")
                .build();
        final TeamDetails teamDetails2 = TeamDetails.builder()
                .teamId("2")
                .teamName("22")
                .teamDesc("22")
                .build();
        final List<TeamDetails> teamDetails = new ArrayList<>();
        when(this.mockTeamService.getTeams()).thenReturn(teamDetails);
        final ResponseEntity<List<TeamDetails>> teamDetailsResponseEntity = this.teamController.getTeams();
        assertThat(teamDetailsResponseEntity.getStatusCode(), equalTo(HttpStatus.OK));
    }
}
