package com.slack.server.appointment;


import com.slack.server.appointmentXRef.AppointmentXRef;
import com.slack.server.appointmentXRef.AppointmentXRefRepository;
import com.slack.server.messages.Message;
import com.slack.server.messages.MessageController;
import com.slack.server.messages.MessageRepository;
import com.slack.server.user.User;
import com.slack.server.user.UserRepository;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/appointment") // This means URL's start with /demo (after Application path)
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepo;
    @Autowired
    AppointmentXRefRepository appointmentXRefRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    MessageRepository messageRepo;

    /**
     * Create and put an Appointment into the table
     * @param name name of the appt
     * @param description desc of the appt
     * @param time time the appt occurs
     * @param user userId of the user making it
     * @return
     * @author Dylan Mrzlak
     */
    @GetMapping(path="/create")
    public @ResponseBody ResponseEntity createAppointment(@RequestParam String name, @RequestParam String description,
                                                          @RequestParam Date time, @RequestParam int user){
        //We want to save the owner's name for easy reporting later on, so we want to pull that specific user
        User u = userRepo.findByID(user);
        if(u == null) return new ResponseEntity("Owner not found", HttpStatus.NOT_FOUND);

        //Create the appointment to save it into the repo
        Appointment a = new Appointment();
        a.setName(name);
        a.setDescription(description);
        a.setTime(time);
        a.setOwnerId(user);
        a = appointmentRepo.save(a);
        a.setOwnerName(u.getName());

        //create a cross reference to signify that the user has the appointment as accepted
        AppointmentXRef x = new AppointmentXRef();
        x.setaId(a.getId());
        x.setuId(user);
        x.setAccepted(true);
        appointmentXRefRepo.save(x);

        return new ResponseEntity(a, HttpStatus.OK);
    }

    @GetMapping(path="/sendInvite")
    public @ResponseBody ResponseEntity sendInvites(@RequestParam int aId, @RequestParam String userIdList){
        String[] userIds = userIdList.split(",");
        ArrayList<String> successfulInvites = new ArrayList<>();
        Appointment a = appointmentRepo.FindByID(aId);
        if(a == null)
            return new ResponseEntity("Appointment not found", HttpStatus.NOT_FOUND);
        for (String idString : userIds) {
            int id = Integer.parseInt(idString);
            User u = userRepo.findByID(id);
            if(u != null){
                AppointmentXRef x = new AppointmentXRef();
                x.setaId(a.getId());
                x.setuId(u.getId());
                x.setAccepted(false);
                appointmentXRefRepo.save(x);

                Message m = new Message();
                m.setSenderId(a.getOwnerId());
                m.setRecipientID(u.getId());
                m.setContent(a.getOwnerName() + " has invited you to " + a.getName()+
                        ".\n\tTo respond enter the commmand: respond appointment - yes/no " + a.getId() +
                        "\n\tbased on your response (Yes will accept and No deny.");
                m.setwId(null);
                m.setcID(null);
                m.setPinned(false);
                messageRepo.save(m);

                successfulInvites.add(u.getName());
            }
        }
        return new ResponseEntity(successfulInvites, HttpStatus.OK);
    }

    @GetMapping(path="/get")
    public @ResponseBody ResponseEntity getAppointments(@RequestParam String user,
                                                        @RequestParam boolean accepted, @RequestParam boolean pending){
        //We want to remove any appointments that are past due for more than a day.
        removePastDue();
        //Get the user so we can query
        ArrayList<Appointment> list = new ArrayList<Appointment>();
        ArrayList<AppointmentToFrontend> listToRet = new ArrayList<>();
        User u = userRepo.findByName(user);
        if(u == null) return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        if(pending){
            list.addAll(appointmentRepo.getPendingByUserId(u.getId()));
        }
        if(accepted){
            list.addAll(appointmentRepo.getAcceptedByUserId(u.getId()));
        }
        for(Appointment appt: list){
            AppointmentToFrontend appt1 = new AppointmentToFrontend();
            boolean isAccepted = appointmentXRefRepo.find(u.getId(), appt.getId()).getAccepted();
            appt1.cloneFromAppt(appt);
            appt1.setAccepted(isAccepted);
        }
        return new ResponseEntity(listToRet, HttpStatus.OK);
    }

    @GetMapping(path="/delete")
    public @ResponseBody ResponseEntity removeAppointment(@RequestParam String user, @RequestParam int aId){
        User u = userRepo.findByName(user);
        if(u == null)
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);

        Appointment a = appointmentRepo.FindByID(aId);
        if(a == null)
            return new ResponseEntity("Appointment Not Found", HttpStatus.NOT_FOUND);

        if(!appointmentXRefRepo.exists(u.getId(), aId))
            new ResponseEntity("Cannot leave an appointment you are not it", HttpStatus.NOT_FOUND);

        if(a.getOwnerId() == u.getId()){
            //We want to remove all xRefs, since this is essentially the owner cancelling the appointment
            appointmentXRefRepo.removeAllById(aId);
            return new ResponseEntity("Successfully cancelled Appointment", HttpStatus.OK);
        }
        else{
            appointmentXRefRepo.removeByUserId(u.getId());
            return new ResponseEntity("Successfully left Appointment", HttpStatus.OK);
        }
    }

    @GetMapping(path="/respond")
    public @ResponseBody ResponseEntity respondToAppointment(@RequestParam String user, @RequestParam int aId,
                                                             @RequestParam boolean accept){
        User u = userRepo.findByName(user);
        if(u == null)
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);

        Appointment a = appointmentRepo.FindByID(aId);
        if(a == null)
            return new ResponseEntity("Appointment Not Found", HttpStatus.NOT_FOUND);
        AppointmentXRef x = appointmentXRefRepo.find(u.getId(), aId);
        if(x == null)
            new ResponseEntity("Cannot leave an appointment you are not it", HttpStatus.NOT_FOUND);
        if(accept){
            x.setAccepted(accept);
            return new ResponseEntity("Successfully Accepted Invite to " + a.getName(), HttpStatus.OK);
        }
        else {
            appointmentXRefRepo.delete(x);
            return new ResponseEntity("Successfully Denied Invite to " + a.getName(), HttpStatus.OK);
        }
    }

    private void removePastDue(){
        Date yesterday = new Date(System.currentTimeMillis() - 86400000);
        List<Appointment> pastDue = appointmentRepo.getAllPastDue(yesterday);
        for (Appointment a : pastDue) {
            appointmentXRefRepo.removeAllById(a.getId());
            appointmentRepo.delete(a);
        }
    }

    private class AppointmentToFrontend extends Appointment {
        private Integer id;

        private String name;

        private String description;

        private Date time;

        private Integer ownerId;

        private String ownerName;

        private boolean accepted;

        public void cloneFromAppt(Appointment appt) {
            id = appt.getId();
            name = appt.getName();
            description = appt.getDescription();
            time = appt.getTime();
            ownerId = appt.getOwnerId();
            ownerName = appt.getOwnerName();
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


    }
}
