package diary.app.dao.inmemory;

import diary.app.dao.inmemory.InMemoryAuditDao;
import diary.app.dto.AuditItem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InMemoryAuditDaoTests {
    @Test
    public void addAndGetAuditItemTest(){

        String user = "user";
        String action = "login";
        String userInput = "action";

        AuditItem auditItem = new AuditItem(user, action, userInput);
        InMemoryAuditDao inMemoryAuditDao = new InMemoryAuditDao();

        inMemoryAuditDao.addAuditItem(auditItem);

        assertTrue(inMemoryAuditDao.getAuditItems(1).contains(auditItem));
    }
    @Test
    public void getLimitAuditItemTest(){

        String user = "user";
        String action = "login";
        String userInput = "action";

        String action2 = "registration";
        String userInput2 = "add login";

        String action3 = "add new training";
        String userInput3 = "swimming";

        AuditItem auditItem = new AuditItem(user, action, userInput);
        AuditItem auditItem2 = new AuditItem(user, action2, userInput2);
        AuditItem auditItem3 = new AuditItem(user, action3, userInput3);

        InMemoryAuditDao inMemoryAuditDao = new InMemoryAuditDao();

        inMemoryAuditDao.addAuditItem(auditItem);
        inMemoryAuditDao.addAuditItem(auditItem2);
        inMemoryAuditDao.addAuditItem(auditItem3);

        assertEquals(2, inMemoryAuditDao.getAuditItems(2).size());
    }
}
