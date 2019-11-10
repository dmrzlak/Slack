package com.slack.server.channel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.slack.server.workspace.Workspace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Interface for the given db table. Springboot will make all of the CRUD functions for us
 * Anything past that that would require some kinda query, we need to put that SQL query here tied to a function
 */
public interface ChannelRepository extends CrudRepository<Channel, Integer>{

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Channel c WHERE c.wId = :wId AND c.name = :name")
    boolean exists(@Param("wId") int wId, @Param("name") String name);

    @Query("SELECT c FROM Channel c WHERE c.wId = :wId AND c.name = :name")
    Channel find(@Param("wId") int wId, @Param("name") String name);


    @Query("SELECT c FROM Channel c WHERE c.id = :cId")
    Channel findByID( @Param("cId") int cId);

}


