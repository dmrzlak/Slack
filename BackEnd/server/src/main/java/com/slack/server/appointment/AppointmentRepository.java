package com.slack.server.appointment;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.ArrayList;

/**
 * Interface for the given db table. Springboot will make all of the CRUD functions for us
 * Anything past that that would require some kinda query, we need to put that SQL query here tied to a function
 */

public interface AppointmentRepository extends CrudRepository<Appointment, Integer>{

    @Query("Select a from Appointment a " +
            "where a.id in (Select x.aId from AppointmentXRef x where x.uId = :userId " +
            "and x.accepted = false )" +
            "ORDER BY a.time asc")
     ArrayList<Appointment> getPendingByUserId(@Param("userId") int userId);

    @Query("Select a from Appointment a " +
            "where a.id in (Select x.aId from AppointmentXRef x where x.uId = :userId " +
            "and x.accepted = true) ORDER BY a.time asc")
     ArrayList<Appointment> getAcceptedByUserId(@Param("userId") int userId);

    @Query("Select a from Appointment a " +
            "where a.id in (Select x.aId from AppointmentXRef x where x.uId = :userId) " +
            "ORDER BY a.time asc")
     ArrayList<Appointment> getAllByUserId(@Param("userId") int userId);

    @Query("Select a from Appointment a where a.id = :id")
    Appointment FindByID(@Param("id") int aId);


    @Query("Select a from Appointment a where a.time <= :yesterday")
    ArrayList<Appointment> getAllPastDue(@Param("yesterday") Date yesterday);

    @Transactional
    @Modifying
    @Query("delete from Appointment a where a.ownerId = :id")
    void removeByOwnerId(@Param("id")int uId);
}


