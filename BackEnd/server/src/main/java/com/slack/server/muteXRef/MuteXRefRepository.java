package com.slack.server.muteXRef;

import com.slack.server.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface MuteXRefRepository extends CrudRepository<MuteXRef, Integer> {
    @Query("SELECT CASE WHEN COUNT(x) > 0 THEN true ELSE false END FROM MuteXRef x WHERE x.uId = :uId AND x.mId = :mId")
    boolean exists(@Param("uId") int uId, @Param("mId") int mId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MuteXRef x WHERE x.uId = :uId AND x.mId = :mId")
    void delete(@Param("uId") int uId, @Param("mId") int mId);
}
