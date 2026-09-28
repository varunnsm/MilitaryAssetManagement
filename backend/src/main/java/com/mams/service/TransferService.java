package com.mams.service;

import com.mams.dto.TransferRequest;
import com.mams.entity.*;
import com.mams.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransferService {
    private final TransferRepository repo; private final BaseRepository bases; private final EquipmentTypeRepository types;
    private final CurrentUserService current; private final AuditService audit;
    public TransferService(TransferRepository r,BaseRepository b,EquipmentTypeRepository t,CurrentUserService c,AuditService a){
        repo=r;bases=b;types=t;current=c;audit=a;
    }
    public List<Transfer> all(){
        User u=current.get();
        if(u.getRole()==Role.ADMIN) return repo.findAll();
        return repo.findByFromBaseIdOrToBaseIdOrderByTransferDateDesc(u.getBase().getId(),u.getBase().getId());
    }
    public Transfer create(TransferRequest req){
        if(req.fromBaseId().equals(req.toBaseId())) throw new RuntimeException("Source and destination must differ");
        // Admin can move between any bases. Other roles must operate from their assigned base.
        if(!current.isAdmin() && !current.canAccessBase(req.fromBaseId()))
            throw new RuntimeException("Access denied: source base is outside your assigned base");
        Transfer t=new Transfer(); t.setFromBase(bases.findById(req.fromBaseId()).orElseThrow());
        t.setToBase(bases.findById(req.toBaseId()).orElseThrow());
        t.setEquipmentType(types.findById(req.equipmentTypeId()).orElseThrow()); t.setQuantity(req.quantity());
        t.setTransferDate(req.transferDate()); t.setRemarks(req.remarks()); t.setStatus(TransferStatus.COMPLETED);
        Transfer saved=repo.save(t);
        audit.log("CREATE_TRANSFER","TRANSFER",saved.getId(),"Transferred "+saved.getQuantity()+" "+saved.getEquipmentType().getName()+" from "+saved.getFromBase().getName()+" to "+saved.getToBase().getName());
        return saved;
    }
}
