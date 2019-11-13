package com.slack.server.workspaceXRef;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Interface for the given db table. Springboot will make all of the CRUD functions for us
 * Anything past that that would require some kinda query, we need to put that SQL query here tied to a function
 */
public interface WorkspaceXRefRepository extends CrudRepository<WorkspaceXRef, Integer> {
    @Query("SELECT CASE WHEN COUNT(x) > 0 THEN true ELSE false END FROM WorkspaceXRef x WHERE x.wId = :wId AND x.uId = :uId")
    boolean exists(@Param("wId") int wId, @Param("uId") int uId);
}
