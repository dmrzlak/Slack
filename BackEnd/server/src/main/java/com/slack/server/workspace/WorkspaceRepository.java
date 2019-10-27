package com.slack.server.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.slack.server.workspace.Workspace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface WorkspaceRepository extends CrudRepository<Workspace, Integer>{

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Workspace w WHERE w.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT w FROM Workspace w WHERE w.name = :name")
    Workspace findbyName(@Param("name") String name);
}


