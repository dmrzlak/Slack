package com.slack.server.appointmentXRef;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface AppointmentXRefRepository extends CrudRepository<AppointmentXRef, Integer> {

    @Transactional
    @Modifying
    @Query("delete from AppointmentXRef x where x.aId = :id")
    void removeAllById(@Param("id")int aId);

    @Transactional
    @Modifying
    @Query("delete from AppointmentXRef x where x.uId = :id")
    void removeByUserId(@Param("id")int uId);

    @Transactional
    @Modifying
    @Query("delete from AppointmentXRef x where x.aId = (Select a.Id from Apointment a where a.ownerId = :id)")
    void removeByOwnerId(@Param("id")int uId);

    @Query("SELECT CASE WHEN COUNT(x) > 0 THEN true ELSE false END FROM AppointmentXRef x " +
            "WHERE x.uId = :uId AND x.aId = :aId")
    boolean exists(@Param("uId") int uId, @Param("aId") int aId);

    @Query("SELECT x FROM AppointmentXRef x WHERE x.uId = :uId AND x.aId = :aId")
    AppointmentXRef find(@Param("uId") int uId, @Param("aId") int aId);


}
