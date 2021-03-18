package org.example.persistence;

import java.util.List;
import java.util.Optional;

import org.example.domain.teams.TeamDetails;

/**
 * Persistence layer to save team information.
 */
public interface PersistenceEngine {

    /**
     * Creates a team.
     *
     * @param teamDetails An instance of {@link TeamDetails}.
     */
    void createTeam(TeamDetails teamDetails);

    /**
     * Returns the team info as {@link TeamDetails}.
     * If not present, it return {@link Optional#empty()}.
     *
     * @param teamId Represents team id
     * @return An {@link Optional} holding {@link TeamDetails}.
     */
    Optional<TeamDetails> getTeamInfo(String teamId);

    /**
     * Returns all the teams info.
     *
     * @return An {@link List} holding {@link TeamDetails}.
     */
    List<TeamDetails> getTeams();

    /**
     * Deletes the team.
     *
     * @param teamId Represents team id
     */
    void deleteTeam(String teamId);
}
