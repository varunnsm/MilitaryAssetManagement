package com.mams.controller;
import com.mams.dto.PurchaseRequest; import com.mams.entity.Purchase; import com.mams.service.PurchaseService;
import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/purchases")
public class PurchaseController {
 private final PurchaseService s; public PurchaseController(PurchaseService s){this.s=s;}
 @GetMapping @PreAuthorize("hasAnyRole('ADMIN','BASE_COMMANDER','LOGISTICS_OFFICER')") public List<Purchase> all(){return s.all();}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','BASE_COMMANDER','LOGISTICS_OFFICER')") public Purchase create(@Valid @RequestBody PurchaseRequest r){return s.create(r);}
}
