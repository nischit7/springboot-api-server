package org.example.services.impl;

import org.example.domain.teams.TeamDetails;
import org.example.persistence.PersistenceEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {

    @Mock
    private PersistenceEngine mockPersistenceEngine;

    private TeamServiceImpl teamService;

    @BeforeEach
    public void setup() {
        this.teamService = new TeamServiceImpl(this.mockPersistenceEngine);
    }

    @Test
    @DisplayName("When team creation succeeds")
    public void createTeamSucceeds() {
        final TeamDetails teamDetails = TeamDetails.builder()
                .teamDesc("some team")
                .teamId("US123")
                .teamName("Phenoix")
                .build();
        this.teamService.createTeam(teamDetails);
        verify(this.mockPersistenceEngine).createTeam(ArgumentMatchers.any(TeamDetails.class));
    }

    @Test
    @DisplayName("When team deletion succeeds")
    public void deleteTeamSucceeds() {
        this.teamService.deleteTeam("US123");
        verify(this.mockPersistenceEngine).deleteTeam(eq("US123"));
    }
}
