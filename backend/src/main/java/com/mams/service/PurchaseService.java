package com.mams.service;

import com.mams.dto.PurchaseRequest;
import com.mams.entity.*;
import com.mams.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository repo; private final BaseRepository bases; private final EquipmentTypeRepository types;
    private final CurrentUserService current; private final AuditService audit;
    public PurchaseService(PurchaseRepository r,BaseRepository b,EquipmentTypeRepository t,CurrentUserService c,AuditService a){
        repo=r;bases=b;types=t;current=c;audit=a;
    }
    public List<Purchase> all(){
        User u=current.get();
        if(u.getRole()==Role.ADMIN) return repo.findAll();
        return repo.findByBaseIdAndPurchaseDateBetween(u.getBase().getId(),java.time.LocalDate.of(2000,1,1),java.time.LocalDate.of(2100,1,1));
    }
    public Purchase create(PurchaseRequest req){
        if(!current.canAccessBase(req.baseId())) throw new RuntimeException("Access denied for base");
        Purchase p=new Purchase(); p.setBase(bases.findById(req.baseId()).orElseThrow());
        p.setEquipmentType(types.findById(req.equipmentTypeId()).orElseThrow()); p.setQuantity(req.quantity());
        p.setPurchaseDate(req.purchaseDate()); p.setReferenceNumber(req.referenceNumber());
        Purchase saved=repo.save(p);
        audit.log("CREATE_PURCHASE","PURCHASE",saved.getId(),"Purchased "+saved.getQuantity()+" "+saved.getEquipmentType().getName()+" for "+saved.getBase().getName());
        return saved;
    }
}
