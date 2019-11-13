package com.slack.server.workspace;

import com.slack.server.user.User;
import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.slack.server.workspace.Workspace;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

/**
 * Interface for the given db table. Springboot will make all of the CRUD functions for us
 * Anything past that that would require some kinda query, we need to put that SQL query here tied to a function
 */
public interface WorkspaceRepository extends CrudRepository<Workspace, Integer>{

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Workspace w WHERE w.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT w FROM Workspace w WHERE w.name = :name")
    Workspace findbyName(@Param("name") String name);

}


