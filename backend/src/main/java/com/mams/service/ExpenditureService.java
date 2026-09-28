package com.mams.service;

import com.mams.dto.ExpenditureRequest;
import com.mams.entity.*;
import com.mams.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExpenditureService {
    private final ExpenditureRepository repo; private final BaseRepository bases; private final EquipmentTypeRepository types;
    private final CurrentUserService current; private final AuditService audit;
    public ExpenditureService(ExpenditureRepository r,BaseRepository b,EquipmentTypeRepository t,CurrentUserService c,AuditService a){
        repo=r;bases=b;types=t;current=c;audit=a;
    }
    public List<Expenditure> all(){
        User u=current.get();
        if(u.getRole()==Role.ADMIN) return repo.findAll();
        return repo.findByBaseIdAndExpenditureDateBetween(u.getBase().getId(),java.time.LocalDate.of(2000,1,1),java.time.LocalDate.of(2100,1,1));
    }
    public Expenditure create(ExpenditureRequest req){
        if(!current.canAccessBase(req.baseId())) throw new RuntimeException("Access denied for base");
        Expenditure e=new Expenditure();e.setBase(bases.findById(req.baseId()).orElseThrow());
        e.setEquipmentType(types.findById(req.equipmentTypeId()).orElseThrow());
        e.setQuantity(req.quantity());e.setExpenditureDate(req.expenditureDate());e.setReason(req.reason());
        Expenditure saved=repo.save(e);
        audit.log("CREATE_EXPENDITURE","EXPENDITURE",saved.getId(),"Expended "+saved.getQuantity()+" "+saved.getEquipmentType().getName()+" at "+saved.getBase().getName());
        return saved;
    }
}
