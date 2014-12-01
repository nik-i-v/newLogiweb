package ru.tsystems.javaschool.logiweb.lw;

import org.junit.Before;
import org.junit.Test;
import ru.tsystems.javaschool.logiweb.lw.server.entities.*;
import ru.tsystems.javaschool.logiweb.lw.service.driver.OrderServiceForDriversBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OrderServiceForDriverTest {
    private EntityManager entityManager;
    private Query query;


    @Before
    public void init() {
        entityManager = mock(EntityManager.class);
        query = mock(Query.class);
    }

    @Test
    public void getOrderForDriversTest() {
        String hql = "SELECT ds.orderId FROM DriverShift ds WHERE ds.drivers.license = :license";
        OrderServiceForDriversBean service = new OrderServiceForDriversBean();
        service.setEntityManager(entityManager);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("license", 12345678901L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new LinkedList());
        when(query.getSingleResult().toString()).thenReturn("1");
        when(entityManager.find(Order.class, 1)).thenReturn(new Order());

        hql = "SELECT DISTINCT ds FROM DriverShift ds WHERE ds.orderId = :number";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("number", 1)).thenReturn(query);
        when(query.getResultList()).thenReturn(new LinkedList());

        hql = "SELECT o.furaId FROM Order o WHERE o.id= :number";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("number", 1)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object());
        when(query.getSingleResult().toString()).thenReturn("1");
        when(entityManager.find(Fura.class, Integer.parseInt(query.getSingleResult().toString()))).thenReturn(new Fura());

        hql = "SELECT DISTINCT oi FROM OrderInfo oi WHERE oi.orderNumber = :number";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("number", 1)).thenReturn(query);
        when(entityManager.find(OrderStatus.class, 1)).thenReturn(new OrderStatus());
        when(query.getResultList()).thenReturn(new LinkedList());
        service.getOrderForDrivers(12345678901L);

    }

    @Test
    public void currentGoodsStatusIsNoTest() {
        String hql = "SELECT oi.name FROM OrderInfo oi WHERE oi.orderNumber = :number AND oi.status = :status";
        OrderServiceForDriversBean service = new OrderServiceForDriversBean();
        service.setEntityManager(entityManager);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("status", OrderInfo.Status.no)).thenReturn(query);
        service.currentGoodsStatusIsNo(1);
    }

    @Test
    public void changeDriverStatusForDriversTest() {
        String hql = "SELECT d.driversId FROM Drivers d WHERE d.license = :license";
        OrderServiceForDriversBean service = new OrderServiceForDriversBean();
        service.setEntityManager(entityManager);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("license", 93949484939L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object());
        when(query.getSingleResult().toString()).thenReturn("12");
        when(entityManager.find(DriverShift.class, 12)).thenReturn(new DriverShift());
        service.changeDriverStatusForDrivers(93949484939L, DriverStatus.shift);
    }

    @Test
    public void getStatusMenuForDriversTest() {
        OrderServiceForDriversBean service = new OrderServiceForDriversBean();
        DriverStatus driverStatus = service.getStatusMenuForDrivers(DriverStatus.atWeel.toString());
        assertEquals(driverStatus, DriverStatus.atWeel);
    }

    @Test
    public void getCurrentStatusForDriverTest() {
        String hql = "SELECT d.driverShift.status FROM Drivers d WHERE d.license = :license";
        OrderServiceForDriversBean service = new OrderServiceForDriversBean();
        service.setEntityManager(entityManager);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("license", 11111111111L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object());
        service.getCurrentStatusForDriver(11111111111L);
    }

    @Test
    public void isAnybodyAtWheel_success() {
        String hql = "SELECT ds.orderId FROM DriverShift ds WHERE ds.drivers.license = :license";
        OrderServiceForDriversBean service = new OrderServiceForDriversBean();
        service.setEntityManager(entityManager);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("license", 12345678901L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new LinkedList());
        when(query.getSingleResult().toString()).thenReturn("1");
        hql = "SELECT COUNT (ds.driverId) FROM  DriverShift ds WHERE ds.status = :status AND ds.orderId = :number";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("status", DriverStatus.atWeel)).thenReturn(query);
        when(query.setParameter("number", 1)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object());
        when(query.getSingleResult().toString()).thenReturn("0");
        service.isAnybodyAtWheel(12345678901L);

    }

    @Test(expected = IllegalArgumentException.class)
    public void isAnybodyAtWheel_no_orders() {
        String hql = "SELECT ds.orderId FROM DriverShift ds WHERE ds.drivers.license = :license";
        OrderServiceForDriversBean service = new OrderServiceForDriversBean();
        service.setEntityManager(entityManager);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("license", 12345678901L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new LinkedList());
        when(query.getSingleResult().toString()).thenReturn("");
        service.isAnybodyAtWheel(12345678901L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isAnybodyAtWheel_weel_already() {
        String hql = "SELECT ds.orderId FROM DriverShift ds WHERE ds.drivers.license = :license";
        OrderServiceForDriversBean service = new OrderServiceForDriversBean();
        service.setEntityManager(entityManager);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("license", 12345678901L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new LinkedList());
        when(query.getSingleResult().toString()).thenReturn("1");
        hql = "SELECT COUNT (ds.driverId) FROM  DriverShift ds WHERE ds.status = :status AND ds.orderId = :number";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("status", DriverStatus.atWeel)).thenReturn(query);
        when(query.setParameter("number", 1)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object());
        when(query.getSingleResult().toString()).thenReturn("1");
        service.isAnybodyAtWheel(12345678901L);
    }
}