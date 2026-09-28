package com.mams.controller;
import com.mams.dto.ExpenditureRequest; import com.mams.entity.Expenditure; import com.mams.service.ExpenditureService;
import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/expenditures")
public class ExpenditureController {
 private final ExpenditureService s; public ExpenditureController(ExpenditureService s){this.s=s;}
 @GetMapping @PreAuthorize("hasAnyRole('ADMIN','BASE_COMMANDER')") public List<Expenditure> all(){return s.all();}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','BASE_COMMANDER')") public Expenditure create(@Valid @RequestBody ExpenditureRequest r){return s.create(r);}
}
