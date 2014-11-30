package ru.tsystems.javaschool.logiweb.lw;

import org.junit.Before;
import org.junit.Test;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.service.admin.UserServiceBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private EntityManager entityManager;
    private Query query;


    @Before
    public void init() {
        query = mock(Query.class);
        entityManager = mock(EntityManager.class);
    }

    @Test
    public void getAllUsersTest() {
        String hql = "SELECT u FROM Users u";
        List<Users> all = new LinkedList();
        all.add(new Users());
        UserServiceBean service = new UserServiceBean();
        service.setEntityManager(entityManager);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(all);
        List<Users> result = service.getUsers();
        assertNotNull(result);
    }

}
