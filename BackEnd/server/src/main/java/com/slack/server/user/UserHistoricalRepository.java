package com.slack.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.slack.server.user.User;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Interface for the given db table. Springboot will make all of the CRUD functions for us
 * Anything past that that would require some kinda query, we need to put that SQL query here tied to a function
 */
public interface UserHistoricalRepository extends CrudRepository<UserHistory, Integer>{
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserHistory u WHERE u.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserHistory u WHERE u.id = :id")
    boolean existsByID(@Param("id") int id);

    @Query("SELECT u FROM UserHistory u WHERE u.name = :name")
    User findByName(@Param("name") String name);

    @Query("SELECT u FROM UserHistory u WHERE u.id = :id")
    User findByID(@Param("id") int id);

    @Query("Select u.name "+
            "From UserHistory u Left Join WorkspaceXRef x on u.id = x.uId "+
            "where x.wId = (select id from Workspace w where w.name = :wName)")
    Iterable<String> findUsers(@Param("wName") String name);

    @Query("Select u.name "+
            "From UserHistory u Left Join UserXRef x on u.id = x.uId "+
            "where x.fId = (select id from UserHistory u where u.id = :uId)")
    Iterable<String> viewFriends(@Param("uId") int uId);

    @Query("SELECT u FROM UserHistory u WHERE u.name LIKE :name")
    Iterable<User> searchUser(@Param("name") String name);
}
