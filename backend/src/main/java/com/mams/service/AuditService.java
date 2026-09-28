package com.mams.service;

import com.mams.entity.*;
import com.mams.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditService {
    private final AuditLogRepository repo;
    private final CurrentUserService current;
    public AuditService(AuditLogRepository repo,CurrentUserService current){this.repo=repo;this.current=current;}
    public void log(String action,String entityType,Long entityId,String description){
        AuditLog a=new AuditLog();
        a.setUser(current.get()); a.setAction(action); a.setEntityType(entityType);
        a.setEntityId(entityId); a.setDescription(description);
        repo.save(a);
    }
    public List<AuditLog> all(){return repo.findTop200ByOrderByTimestampDesc();}
}
