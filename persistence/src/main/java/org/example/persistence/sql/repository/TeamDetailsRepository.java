package org.example.persistence.sql.repository;

import org.example.persistence.sql.entity.TeamDetailsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamDetailsRepository extends CrudRepository<TeamDetailsEntity, String> {
}
