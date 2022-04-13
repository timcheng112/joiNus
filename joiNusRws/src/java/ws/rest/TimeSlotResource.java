/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.TimeSlotEntitySessionBeanLocal;
import entity.TimeSlotEntity;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("TimeSlot")
public class TimeSlotResource {

    TimeSlotEntitySessionBeanLocal timeSlotEntitySessionBeanLocal = lookupTimeSlotEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TimeSlotResource
     */
    public TimeSlotResource() {
    }

    @Path("retrieveAllAvailTimeSlots")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAvailTimeSlots() {
        try {
            List<TimeSlotEntity> timeSlotEntities = timeSlotEntitySessionBeanLocal.retrieveAllAvailableTimeSlots();

            for (TimeSlotEntity timeSlot : timeSlotEntities) {
                System.out.println(timeSlot);
                timeSlot.getFacility().getTimeSlots().clear();
                if (timeSlot.getBooking() != null) {
                    timeSlot.getBooking().setTimeSlot(null);
                    timeSlot.getBooking().setActivity(null);
                }
            }

            GenericEntity<List<TimeSlotEntity>> genericEntity = new GenericEntity<List<TimeSlotEntity>>(timeSlotEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private TimeSlotEntitySessionBeanLocal lookupTimeSlotEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TimeSlotEntitySessionBeanLocal) c.lookup("java:global/joiNus/joiNus-ejb/TimeSlotEntitySessionBean!ejb.session.stateless.TimeSlotEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
