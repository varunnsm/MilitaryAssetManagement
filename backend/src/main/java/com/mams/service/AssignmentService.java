package com.mams.service;

import com.mams.dto.AssignmentRequest;
import com.mams.entity.*;
import com.mams.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssignmentService {
    private final AssignmentRepository repo; private final BaseRepository bases; private final EquipmentTypeRepository types;
    private final CurrentUserService current; private final AuditService audit;
    public AssignmentService(AssignmentRepository r,BaseRepository b,EquipmentTypeRepository t,CurrentUserService c,AuditService a){
        repo=r;bases=b;types=t;current=c;audit=a;
    }
    public List<Assignment> all(){
        User u=current.get();
        if(u.getRole()==Role.ADMIN) return repo.findAll();
        return repo.findByBaseIdOrderByAssignedDateDesc(u.getBase().getId());
    }
    public Assignment create(AssignmentRequest req){
        if(!current.canAccessBase(req.baseId())) throw new RuntimeException("Access denied for base");
        Assignment a=new Assignment();a.setBase(bases.findById(req.baseId()).orElseThrow());
        a.setEquipmentType(types.findById(req.equipmentTypeId()).orElseThrow());
        a.setPersonnelName(req.personnelName());a.setQuantity(req.quantity());a.setAssignedDate(req.assignedDate());a.setStatus(AssignmentStatus.ACTIVE);
        Assignment saved=repo.save(a);
        audit.log("CREATE_ASSIGNMENT","ASSIGNMENT",saved.getId(),"Assigned "+saved.getQuantity()+" "+saved.getEquipmentType().getName()+" to "+saved.getPersonnelName());
        return saved;
    }
}
