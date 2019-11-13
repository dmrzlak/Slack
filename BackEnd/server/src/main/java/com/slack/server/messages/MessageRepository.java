package com.slack.server.messages;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
/**
 * Interface for the given db table. Springboot will make all of the CRUD functions for us
 * Anything past that that would require some kinda query, we need to put that SQL query here tied to a function
 */
public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Message m WHERE m.id = :id")
    boolean existsByID(@Param("id") Integer id);

    @Query("SELECT m FROM Message m WHERE m.id = :id")
    Message findByID(@Param("id") Integer id);

    @Query("Select m From Message m where m.wId = :wId AND m.cId = :cId")
    Iterable<Message> getChannelMessages(@Param("wId") int wId, @Param("cId") int cId);

    @Query("Select m From Message m where m.recipientID = :rId")
    Iterable<Message> getUsersMessages(@Param("rId") int rId);


    @Query("Select m From Message m where m.pinned = 1 and m.wId = :wId AND m.cId = :cId")
    Iterable<Message> getPinnedMessagesByChannel(@Param("wId") int wId, @Param("cId") int cId);

    @Query("Select m From Message m where m.wId = :wId ORDER BY m.cId ASC, m.id ASC")
    Iterable<Message> getAllMessagesByWorkspace(@Param("wId") int wId);

    @Query("Select m from Message m where m.wId = :wId AND m.cId = :cId AND m.content LIKE :query")
    Iterable<Message> getAllMessageContainsUName(@Param("query") String query,
                                                 @Param("wId") int wId,
                                                 @Param("cId") int cId);
}
