package com.slack.server.messages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Message m WHERE m.id = :id")
    boolean existsByID(@Param("id") Integer id);

    @Query("SELECT m FROM Message m WHERE m.id = :id")
    Message findByID(@Param("id") Integer id);

    @Query("Select m From Message m where m.wId = :wId AND m.cId = :cId")
    Iterable<Message> getChannelMessages(@Param("wId") int wId, @Param("cId") int cId);

    @Query("Select m From Message m where m.recipientID = :rId")
    Iterable<Message> getUsersMessages(@Param("rId") int rId);


    @Query("Select m From Message m where m.wId = :wId ORDER BY m.cID ASC, m.id ASC")
    Iterable<Message> getAllMessagesByWorkspace(@Param("wId") int wId);

    @Query("Select m from Message m where m.wId = wId AND m.cID = cId AND m.content LIKE concat('%', username, '%')")
    Iterable<Message> getAllMessageContainsUName(@Param("username") String username,
                                                 @Param("wId") int wId,
                                                 @Param("cId") int cId);
}
