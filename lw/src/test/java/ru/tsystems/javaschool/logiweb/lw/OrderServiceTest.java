package ru.tsystems.javaschool.logiweb.lw;

import org.junit.Before;
import org.junit.Test;
import ru.tsystems.javaschool.logiweb.lw.server.entities.*;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderServiceBean;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private EntityManager entityManager;
    private Query query;
    private Logger logger;


    @Before
    public void init() {
        entityManager = mock(EntityManager.class);
        query = mock(Query.class);
        logger = mock(Logger.class);
    }

    @Test
    public void getAllOrdersTest() {
        String hql = "SELECT o FROM Order o";
        List<Order> all = new LinkedList();
        all.add(new Order());
        OrderServiceBean service = new OrderServiceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(all);
        String hql1 = "SELECT DISTINCT ds FROM DriverShift ds WHERE ds.orderId = :number";
        when(entityManager.createQuery(hql1)).thenReturn(query);
        when(query.setParameter("status", DriverStatus.notShift)).thenReturn(query);
        when(query.getResultList()).thenReturn(new LinkedList());
        String hql2 = "SELECT DISTINCT oi FROM OrderInfo oi WHERE oi.orderNumber = :number";
        when(entityManager.createQuery(hql2)).thenReturn(query);
        when(query.setParameter("status", DriverStatus.notShift)).thenReturn(query);
        List<Order> result = service.getAllOrders();
        assertNotNull(result);
    }

    @Test
    public void changeOrderStatus() {
        String hql = "UPDATE OrderStatus os SET os.status = :status " +
                "WHERE os.orderId = :orderId";
        OrderServiceBean service = new OrderServiceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("status", OrderStatus.Status.confirmed)).thenReturn(query);
        when(query.setParameter("orderId", 1243)).thenReturn(query);
        service.changeOrderStatus(1243, OrderStatus.Status.confirmed);
    }

    @Test
    public void addFuraAndDriversToOrderTest() throws IncorrectDataException {
        String hql = "SELECT SUM(oi.weight) FROM OrderInfo oi WHERE oi.orderNumber = :number";
        OrderServiceBean service = new OrderServiceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("number", 1)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object());
        when(query.getSingleResult().toString()).thenReturn("123");
        hql = "SELECT f.capacity FROM  Fura f WHERE f.furaNumber = :number";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("number", "CF35241")).thenReturn(query);
        hql = "SELECT f.driverCount FROM Fura f WHERE f.furaNumber = :number";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("number", "CF35241")).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object());
        when(query.getSingleResult().toString()).thenReturn("1");
        hql = "UPDATE Fura f SET f.status = :status WHERE f.furaNumber = :furaId";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("status", Fura.Status.yes)).thenReturn(query);
        when(query.setParameter("number", "CF35241")).thenReturn(query);
        hql = "SELECT d.driversId FROM Drivers d WHERE d.license = :license";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("license", 13424756384L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object());
        when(query.getSingleResult().toString()).thenReturn("1");
        hql = "SELECT f.furasId FROM Fura f WHERE f.furaNumber = :fura";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("number", "CF35241")).thenReturn(query);
        hql = "UPDATE Order o SET  o.furaId = :fura WHERE o.id = :number";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("number", 1)).thenReturn(query);
        when(query.setParameter("fura", 1)).thenReturn(query);
        hql = "UPDATE OrderStatus os SET os.status = :status " +
                "WHERE os.orderId = :orderId";
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("status", OrderStatus.Status.confirmed)).thenReturn(query);
        when(query.setParameter("orderId", 1243)).thenReturn(query);
        when(entityManager.find(DriverShift.class, 1)).thenReturn(new DriverShift());
        List<Long> drivers = new LinkedList<>();
        drivers.add(83746535298L);
        service.addFuraAndDrivers(1, drivers, "CF35241");
    }
}
