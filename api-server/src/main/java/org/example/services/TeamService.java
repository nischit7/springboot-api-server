package org.example.services;

import java.util.List;

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
     * Returns the team info as {@link TeamDetails}.
     *
     * @return An instance of collection of {@link TeamDetails}.
     */
    List<TeamDetails> getTeams();

    /**
     * Deletes the team.
     *
     * @param teamId Represents team id
     */
    void deleteTeam(String teamId);
}
