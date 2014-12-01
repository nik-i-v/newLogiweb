package ru.tsystems.javaschool.logiweb.lw.ui.rest;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverStatus;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;
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
import java.util.ArrayList;
import java.util.LinkedList;
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
        return "<xml><result> Your status now is " + orderServiceForDrivers.getCurrentStatusForDriver(driverLicense) + "</result></xml>";
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
              List<String> goods = orderServiceForDrivers.getGoodsList(driverLicense);
        if (goods.isEmpty()){
            goods.add("You have no goods now");
        }
        return goods;
    }

    /**
     * Return order for a driver.
     * @param driverLicense license number of a driver
     * @return the order for a driver
     */
    @GET
    @Path("{driverLicense}/order")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public List getOrderForDriver(@PathParam("driverLicense") Long driverLicense) {
        Order order =  orderServiceForDrivers.getOrderForDrivers(driverLicense);
        String furaNumber = order.getFura().getFuraNumber();
        Integer orderNumber = order.getId();
        List<OrderInfo> goods = order.getOrderStatus().getOrderInfo();
        List<DriverShift> driverShift = order.getDriverShift();
        StringBuffer drivers = new StringBuffer();
        for (DriverShift d: driverShift){
            drivers.append(d.getDrivers().getLicense() + ", ");
        }
        drivers.deleteCharAt(drivers.length()-2);
        StringBuffer goodsList = new StringBuffer();
        for (OrderInfo oi: goods){
            goodsList.append("Name: " + oi.getName() + " Status: " + oi.getStatus() + ", ");
        }
        goodsList.deleteCharAt(goodsList.length()-2);
        List result = new ArrayList();
        result.add(orderNumber);
        result.add(drivers);
        result.add(furaNumber);
        result.add(goodsList);
        return result;
    }

    /**
     * Changes the status of a goods.
     * @param name the name of a goods
     * @param driverLicense license number of a driver
     */
    @POST
    @Path("change_goods_status/{driverLicense}/{name}")
    @Consumes(MediaType.APPLICATION_XML)
    public String changeGoodsStatus(@PathParam("driverLicense") Long driverLicense, @PathParam("name") String name ) {
        orderServiceForDrivers.changeGoodsStatusForDrivers(name, driverLicense);
        return "<xml><result>" + "success" + "</result></xml>";
    }

    /**
     * Changes the status of a driver.
     * @param driverLicense the license driver of a driver
     * @param status a new status for a driver
     */
    @POST
    @Path("changeStatus/{driverLicense}/{status}")
    @Consumes(MediaType.APPLICATION_XML)
    public String changeDriverStatus(@PathParam("driverLicense") Long driverLicense, @PathParam("status") String status){
        if (status.equals("shift")) {
            changeStatus(driverLicense,DriverStatus.shift);
        } else {
            orderServiceForDrivers.isAnybodyAtWheel(driverLicense);
            changeStatus(driverLicense, DriverStatus.atWeel);
        }
        return "<xml><result>" + "success" + "</result></xml>";
    }

    private void changeStatus(Long driverLicense, DriverStatus status) {
        orderServiceForDrivers.changeDriverStatusForDrivers(driverLicense, status);

    }
}
