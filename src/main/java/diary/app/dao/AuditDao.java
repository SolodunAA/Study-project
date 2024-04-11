package diary.app.dao;

import diary.app.dto.AuditItem;

import java.util.List;

public interface AuditDao {
    void addAuditItem(AuditItem auditItem);

    List<AuditItem> getAuditItems(int limit);
}
