package com.slack.server.workspaceXRef;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface WorkspaceXRefRepository extends CrudRepository<WorkspaceXRef, Integer> {
    @Query("SELECT CASE WHEN COUNT(x) > 0 THEN true ELSE false END FROM WorkspaceXRef x WHERE x.wId = :wId AND x.uId = :uId")
    boolean exists(@Param("wId") int wId, @Param("uId") int uId);
}
