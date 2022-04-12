/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import entity.CategoryEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.bind.annotation.JsonbTransient;
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
@Path("Category")
public class CategoryResource {

    CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal = lookupCategoryEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CategoryResource
     */
    public CategoryResource() {
    }

    @Path("retrieveAllCategories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories() {
        try {
            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();

            for (CategoryEntity categoryEntity : categoryEntities) {
                categoryEntity.getActivities().clear();
                categoryEntity.getSubCategories().clear();
                if (categoryEntity.getParentCategory() != null) {
                    categoryEntity.getParentCategory().getSubCategories().clear();
                }
                categoryEntity.setParentCategory(null);
            }
//            for (CategoryEntity categoryEntity : categoryEntities) {
//                for (ActivityEntity activity : categoryEntity.getActivities()) {
//                    System.out.println(activity);
//                    activity.setActivityOwner(null);
//                    activity.setParticipants(null);
//                    activity.setCategory(null);
//                    activity.setBooking(null);
//                    activity.setComments(null);
//                    activity.setGallery(null);
//                }
//                
//                for (CategoryEntity subCategory : categoryEntity.getSubCategories()) {
//                    subCategory.setSubCategories(null);
//                    subCategory.setParentCategory(null);
//                    subCategory.setActivities(null);
//                }
//                categoryEntity.getParentCategory().setSubCategories(null);
//                categoryEntity.getParentCategory().setParentCategory(null);
//                categoryEntity.getParentCategory().setActivities(null);
//            }
            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private CategoryEntitySessionBeanLocal lookupCategoryEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CategoryEntitySessionBeanLocal) c.lookup("java:global/joiNus/joiNus-ejb/CategoryEntitySessionBean!ejb.session.stateless.CategoryEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
