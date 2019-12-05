package com.slack.server.favorites;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends CrudRepository<Favorite, Integer> {

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Favorite f WHERE f.messageID = :mId AND f.userID = :uId")
    boolean exists(@Param("uId") Integer uId,@Param("mId") Integer mId);

    @Query("delete from Favorite f where f.messageID = :mId AND f.userID = :uId")
    void deleteByIds(@Param("uId") Integer uId, @Param("mId") Integer mId);

    @Query("SELECT m FROM Favorite m WHERE m.id = :id")
    Favorite findByID(@Param("id") Integer id);



}
