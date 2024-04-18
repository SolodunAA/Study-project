package diary.app.dao;

import diary.app.dto.AuditItem;

import java.util.ArrayList;
import java.util.List;

/**
 * class contains, add and show information about users actions
 */
public class InMemoryAuditDao implements AuditDao {
    /**
     * list which contains users actions
     */
    private final List<AuditItem> auditCache = new ArrayList<>();

    /**
     * add action in auditCache
     * @param auditItem action we want to add to auditCache
     */
    @Override
    public void addAuditItem(AuditItem auditItem) {
        auditCache.add(auditItem);
    }
    /**
     * @return audit record size
     */
    @Override
    public int AuditItemsSize() {
        return auditCache.size();
    }

    /**
     * @param limit number of records you want to see
     * @return List with "limit" user actions
     */

    @Override
    public List<AuditItem> getAuditItems(int limit) {
        int cacheSize = auditCache.size();
        return auditCache.subList(Math.max(cacheSize - limit, 0), cacheSize);
    }
}
