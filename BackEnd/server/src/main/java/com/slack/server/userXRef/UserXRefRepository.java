package com.slack.server.userXRef;

import com.slack.server.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserXRefRepository extends CrudRepository<UserXRef, Integer> {
    @Query("SELECT CASE WHEN COUNT(x) > 0 THEN true ELSE false END FROM UserXRef x WHERE x.fId = :fId AND x.uId = :uId")
    boolean exists(@Param("fId") int fId, @Param("uId") int uId);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserXRef x WHERE x.fId = :fId AND x.uId = :uId")
    void delete(@Param("fId") int fId, @Param("uId") int uId);
}
