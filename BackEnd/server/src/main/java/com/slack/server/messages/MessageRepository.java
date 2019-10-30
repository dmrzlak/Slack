package com.slack.server.messages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends CrudRepository<Message, Integer> {


    @Query("Select m From Message m where m.wId = :wId AND m.cId = :cId")
    Iterable<Message> getChannelMessages(@Param("wId") int wId, @Param("cId") int cId);

    @Query("Select m From Message m where m.recipientID = :rId")
    Iterable<Message> getUsersMessages(@Param("rId") int rId);
}
