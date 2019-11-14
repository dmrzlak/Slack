package com.slack.server.textfile;

import com.slack.server.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TextfileRepository extends CrudRepository<Textfile, Integer> {
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Textfile t WHERE t.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Textfile t WHERE t.name = :name")
    boolean existsById(@Param("name") int id);

    @Query("SELECT t FROM Textfile t WHERE t.name = :name")
    Textfile findByName(@Param("name") String name);

    @Query("SELECT t FROM Textfile t WHERE t.id = :id")
    Textfile findByID(@Param("id") int id);
}
