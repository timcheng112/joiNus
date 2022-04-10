/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.FacilityEntitySessionBeanLocal;
import entity.FacilityEntity;
import entity.TimeSlotEntity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Facility")
public class FacilityResource {

    FacilityEntitySessionBeanLocal facilityEntitySessionBeanLocal = lookupFacilityEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FacilityResource
     */
    public FacilityResource() {
    }

    @Path("retrieveAllFacilities")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFacilities() {
        try {
            List<FacilityEntity> facilityEntities = facilityEntitySessionBeanLocal.retrieveAllFacilities();

            for (FacilityEntity fac : facilityEntities) {

                for (TimeSlotEntity time : fac.getTimeSlots()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(time.getTimeSlotTime());

                    Date newDate = cal.getTime();
                    String DATE_FORMAT1 = "yyyy-MM-dd HH:mm";
                    String MAIN_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
                    SimpleDateFormat formatter = new SimpleDateFormat(MAIN_DATE_FORMAT);
                    formatter = new SimpleDateFormat(DATE_FORMAT1);
                    System.out.println(formatter.format(newDate));

                    time.setFacility(null);
                    time.setBooking(null);
                }

                System.out.println("Facility Id: " + fac.getFacilityId() + ", Facility Name: " + fac.getFacilityName());
            }

            GenericEntity<List<FacilityEntity>> genericEntity = new GenericEntity<List<FacilityEntity>>(facilityEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private FacilityEntitySessionBeanLocal lookupFacilityEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (FacilityEntitySessionBeanLocal) c.lookup("java:global/joiNus/joiNus-ejb/FacilityEntitySessionBean!ejb.session.stateless.FacilityEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
