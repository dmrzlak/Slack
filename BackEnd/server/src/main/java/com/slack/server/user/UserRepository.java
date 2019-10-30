package com.slack.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.slack.server.user.User;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;

public interface UserRepository extends CrudRepository<User, Integer>{
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    User findByName(@Param("name") String name);

    @Query("Select u.name "+
            "From User u Left Join WorkspaceXRef x on u.id = x.uId "+
            "where x.wId = (select id from Workspace w where w.name = :wName)")
    Iterable<String> findUsers(@Param("wName") String name);

}
