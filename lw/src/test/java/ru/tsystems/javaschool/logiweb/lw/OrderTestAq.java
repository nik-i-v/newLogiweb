package ru.tsystems.javaschool.logiweb.lw;

import org.jboss.arquillian.container.test.api.Deployment;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderServiceBean;
import ru.tsystems.javaschool.logiweb.lw.ui.admin.OrderAction;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import javax.ejb.EJB;

@RunWith(Arquillian.class)
public class OrderTestAq {

    @EJB
    private OrderService orderService;

//    @Deployment
//    public static JavaArchive createTestArchive() {
//        return ShrinkWrap.create(JavaArchive.class, "lw.war")
//                .addClasses(Order.class,
//                        OrderServiceBean.class,
//                        IncorrectDataException.class,
//                        OrderAction.class)
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//
//    }

    @Test
    public void testGetOrders() {
//        Assert.assertEquals("Hello, Earthling!",
//                greeter.createGreeting("Earthling"));
    }
}

