package org.example.services;

import org.example.domain.teams.TeamDetails;

/**
 * A service class that manages team information.
 */
public interface TeamService {

    /**
     * Creates a team.
     *
     * @param teamDetails An instance of {@link TeamDetails}.
     */
    void createTeam(TeamDetails teamDetails);

    /**
     * Returns the team info as {@link TeamDetails}.
     *
     * @param teamId Represents team id
     * @return An instance of {@link TeamDetails}.
     */
    TeamDetails getTeamInfo(String teamId);

    /**
     * Deletes the team.
     *
     * @param teamId Represents team id
     */
    void deleteTeam(String teamId);
}
