package diary.app.dao;

import diary.app.dto.AuditItem;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAuditDao implements AuditDao {

    private final List<AuditItem> auditCache = new ArrayList<>();

    @Override
    public void addAuditItem(AuditItem auditItem) {
        auditCache.add(auditItem);
    }

    @Override
    public List<AuditItem> getAuditItems(int limit) {
        int cacheSize = auditCache.size();
        return auditCache.subList(Math.max(cacheSize - limit, 0), cacheSize);
    }
}
