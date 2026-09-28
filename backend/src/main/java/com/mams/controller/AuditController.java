package com.mams.controller;
import com.mams.entity.AuditLog; import com.mams.service.AuditService;
import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/audit-logs")
public class AuditController {
 private final AuditService s; public AuditController(AuditService s){this.s=s;}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<AuditLog> all(){return s.all();}
}
