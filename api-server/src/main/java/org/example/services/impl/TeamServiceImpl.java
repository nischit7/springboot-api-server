package org.example.services.impl;

import java.util.List;
import java.util.Optional;

import org.example.domain.teams.TeamDetails;
import org.example.persistence.PersistenceEngine;
import org.example.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link TeamService}.
 */
@Service
public class TeamServiceImpl implements TeamService {

    private final PersistenceEngine persistenceEngine;

    @Autowired
    public TeamServiceImpl(final PersistenceEngine persistenceEngine) {
        this.persistenceEngine = persistenceEngine;
    }

    @Override
    public void createTeam(final TeamDetails teamDetails) {
        this.persistenceEngine.createTeam(teamDetails);
    }

    @Override
    public TeamDetails getTeamInfo(final String teamId) {
        final Optional<TeamDetails> teamDetails = this.persistenceEngine.getTeamInfo(teamId);
        return teamDetails.map(teamDet -> teamDet).orElseThrow(() -> new TeamNotFoundException(teamId));
    }

    @Override
    public List<TeamDetails> getTeams() {
        return this.persistenceEngine.getTeams();
    }

    @Override
    public void deleteTeam(final String teamId) {
        this.persistenceEngine.deleteTeam(teamId);
    }
}
