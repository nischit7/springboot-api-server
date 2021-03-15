package org.example.persistence.sql;

import java.util.Optional;

import org.example.domain.teams.TeamDetails;
import org.example.persistence.PersistenceEngine;
import org.example.persistence.sql.entity.TeamDetailsEntity;
import org.example.persistence.sql.repository.TeamDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link PersistenceEngine}.
 * Specially uses SQL as the persistence layer.
 */
@Component
public class SqlPersistenceEngine implements PersistenceEngine {

    private final TeamDetailsRepository teamDetailsRepository;

    @Autowired
    public SqlPersistenceEngine(final TeamDetailsRepository teamDetailsRepository) {
        this.teamDetailsRepository = teamDetailsRepository;
    }

    @Override
    public void createTeam(final TeamDetails teamDetails) {
        final TeamDetailsEntity teamDetailsEntity = TeamDetailsEntity.builder()
                .teamId(teamDetails.getTeamId())
                .teamName(teamDetails.getTeamName())
                .teamDesc(teamDetails.getTeamDesc())
                .build();
        teamDetailsRepository.save(teamDetailsEntity);
    }

    @Override
    public Optional<TeamDetails> getTeamInfo(final String teamId) {
        final Optional<TeamDetailsEntity> teamEntity = teamDetailsRepository.findById(teamId);
        return teamEntity.map(teamDetailsEntity -> Optional.of(TeamDetails.builder()
                    .teamId(teamDetailsEntity.getTeamId())
                    .teamName(teamDetailsEntity.getTeamName())
                    .teamDesc(teamDetailsEntity.getTeamDesc())
                    .build()))
                .orElse(Optional.empty());
    }

    @Override
    public void deleteTeam(final String teamId) {
        teamDetailsRepository.deleteById(teamId);
    }
}
