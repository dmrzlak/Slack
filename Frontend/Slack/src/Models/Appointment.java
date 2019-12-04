package Models;

import Controllers.DBSupport;

import java.util.Date;
import java.util.ArrayList;

/**
 * Model for the Workspace Table. Essentially this is what the table will contain
 * @Author Dylan Mrzlak
 */

public class Appointment {

    public Appointment(Integer id, String name, String description, Date time, Integer ownerId, String ownerName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.time = time;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    private Integer id;

    private String name;

    private String description;

    private Date time;

    private Integer ownerId;

    private String ownerName;

    private boolean accepted;
    @Override
    public String toString() {
        return "\tID: " + id + "\tNAME: " + name + "\tOWNER: " + ownerName + "\n\t\tDESC: " + description +
                "\n\t\tTIME:" + time.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public static DBSupport.HTTPResponse createAppointment(String name, String description, String timeString, int user) {
        DBSupport.HTTPResponse res = DBSupport.createAppointment(name, description, timeString, user);
        return res;
    }
    public static DBSupport.HTTPResponse sendInvite(int aId, ArrayList<Integer> userIds){
        DBSupport.HTTPResponse res = DBSupport.sendInvite(aId, userIds);
        return res;
    }

    public static DBSupport.HTTPResponse getAppointments(String user, boolean accepted, boolean pending){
        DBSupport.HTTPResponse res = DBSupport.getAppointments(user, accepted, pending);
        return res;
    }

    public static DBSupport.HTTPResponse deleteAppointment(String user, int aId){
        DBSupport.HTTPResponse res = DBSupport.deleteAppointment(user, aId);
        return res;
    }

    public static DBSupport.HTTPResponse respondAppointment(String user, int aId, boolean accept){
        DBSupport.HTTPResponse res = DBSupport.respondAppointment(user, aId, accept);
        return res;
    }


}
