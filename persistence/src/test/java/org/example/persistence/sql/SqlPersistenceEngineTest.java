package org.example.persistence.sql;

import java.util.Optional;

import org.example.domain.teams.TeamDetails;
import org.example.persistence.sql.config.SqlDBConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@EnableAutoConfiguration
@TestPropertySource("classpath:myservice-api-db-test.properties")
@SpringBootTest(classes = SqlDBConfig.class)
public class SqlPersistenceEngineTest {

    @Autowired
    private SqlPersistenceEngine sqlPersistenceEngine;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("When team creation succeeds")
    public void createTeamSucceeds() {
        final TeamDetails teamDetails = TeamDetails.builder()
                .teamDesc("some team")
                .teamId("US123")
                .teamName("Phenoix")
                .build();
        this.sqlPersistenceEngine.createTeam(teamDetails);
        final Optional<TeamDetails> savedTeamDetails = this.sqlPersistenceEngine.getTeamInfo(teamDetails.getTeamId());

        assertThat(teamDetails.getTeamId(), equalTo(savedTeamDetails.get().getTeamId()));
    }
}
