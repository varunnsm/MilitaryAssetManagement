package com.mams.controller;
import com.mams.dto.TransferRequest; import com.mams.entity.Transfer; import com.mams.service.TransferService;
import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/transfers")
public class TransferController {
 private final TransferService s; public TransferController(TransferService s){this.s=s;}
 @GetMapping @PreAuthorize("hasAnyRole('ADMIN','BASE_COMMANDER','LOGISTICS_OFFICER')") public List<Transfer> all(){return s.all();}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','BASE_COMMANDER','LOGISTICS_OFFICER')") public Transfer create(@Valid @RequestBody TransferRequest r){return s.create(r);}
}
