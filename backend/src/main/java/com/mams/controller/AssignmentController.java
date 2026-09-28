package com.mams.controller;
import com.mams.dto.AssignmentRequest; import com.mams.entity.Assignment; import com.mams.service.AssignmentService;
import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/assignments")
public class AssignmentController {
 private final AssignmentService s; public AssignmentController(AssignmentService s){this.s=s;}
 @GetMapping @PreAuthorize("hasAnyRole('ADMIN','BASE_COMMANDER')") public List<Assignment> all(){return s.all();}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','BASE_COMMANDER')") public Assignment create(@Valid @RequestBody AssignmentRequest r){return s.create(r);}
}
