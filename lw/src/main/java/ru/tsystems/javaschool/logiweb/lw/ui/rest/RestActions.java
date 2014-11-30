package ru.tsystems.javaschool.logiweb.lw.ui.rest;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverStatus;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.service.driver.OrderServiceForDrivers;
import sun.net.www.http.HttpClient;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This class permits to use driver services for drivers with using REST.
 *
 * @author Irina Nikulina
 */
@Path("/")
public class RestActions {

    @EJB
    private OrderServiceForDrivers orderServiceForDrivers;

    @Inject
    private FacesContext facesContext;

    /**
     * Returns the status of a driver.
     *
     * @param driverLicense license number of the driver
     * @return the status of a driver
     */
    @GET
    @Path("{driverLicense}/status")
    @javax.ws.rs.Produces({"application/xml"})
    public String getDriverStatus(@PathParam("driverLicense") Long driverLicense) {
        return "<xml><result>" + orderServiceForDrivers.getCurrentStatusForDriver(driverLicense) + "</result></xml>";
    }

    /**
     * Gets goods from a order for certain driver.
     *
     * @param driverLicense license number
     * @return the list of statuses for certain driver.
     */
    @GET
    @Path("{driverLicense}/goods_with_status_no")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public List<String> getGoodsStatus(@PathParam("driverLicense") Long driverLicense) {
              return orderServiceForDrivers.getGoodsList(driverLicense);
    }

    /**
     * Return order for a driver.
     * @param driverLicense license number of a driver
     * @return the order for a driver
     */
    @GET
    @Path("{driverLicense}/order")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public Order getOrderForDriver(@PathParam("driverLicense") Long driverLicense) {
        return orderServiceForDrivers.getOrderForDrivers(driverLicense);
    }

    /**
     * Changes the status of a goods.
     * @param name the name of a goods
     * @param driverLicense license number of a driver
     */
    @POST
    @Path("{driverLicense}/{name}")
    @Consumes("application/xml")
    public String changeGoodsStatus(@PathParam("name") String name, @PathParam("driverLicense") Long driverLicense) {
        orderServiceForDrivers.changeGoodsStatusForDrivers(name, driverLicense);
        return "<xml><result>" + "success" + "</result></xml>";
    }

    /**
     * Changes the status of a driver.
     * @param driverLicense the license driver of a driver
     * @param status a new status for a driver
     */
    @POST
    @Path("changeStatus/{driverLicense}")
    @Consumes("application/xml")
    public String changeDriverStatus(@PathParam("driverLicense") Long driverLicense, @PathParam("status") DriverStatus status){
        if (status.equals(DriverStatus.atWeel.toString())) {
            changeStatus(driverLicense,DriverStatus.shift);
        } else {
            orderServiceForDrivers.isAnybodyAtWheel(driverLicense);
            changeStatus(driverLicense, DriverStatus.atWeel);
        }
        return "<xml><result>" + "success" + "</result></xml>";
    }

    private void changeStatus(Long driverLicense, DriverStatus status) {
        try {
            orderServiceForDrivers.changeDriverStatusForDrivers(driverLicense, status);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Driver status has been changed", "Driver status change successful"));
//            currentStatus = orderServiceForDrivers.getCurrentStatusForDriver(driverLicense);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Changing unsuccessful"));
        }
    }
}
