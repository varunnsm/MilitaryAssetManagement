package com.mams.service;

import com.mams.entity.*;
import com.mams.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {
    private final PurchaseRepository purchases; private final TransferRepository transfers; private final AssignmentRepository assignments;
    private final ExpenditureRepository expenditures; private final AssetBalanceRepository balances;
    private final CurrentUserService current;

    public DashboardService(PurchaseRepository p,TransferRepository t,AssignmentRepository a,ExpenditureRepository e,AssetBalanceRepository b,CurrentUserService c){
        purchases=p;transfers=t;assignments=a;expenditures=e;balances=b;current=c;
    }

    public Map<String,Object> summary(LocalDate from, LocalDate to, Long baseId, Long equipmentTypeId){
        if(from==null) from=LocalDate.now().withDayOfMonth(1);
        if(to==null) to=LocalDate.now();
        User u=current.get();
        if(u.getRole()!=Role.ADMIN) baseId=u.getBase().getId();

        int opening=0,purchase=0,in=0,out=0,assigned=0,expended=0;
        List<AssetBalance> bs = baseId==null ? balances.findAll() : balances.findByBaseId(baseId);
        for(AssetBalance b:bs) if(equipmentTypeId==null || b.getEquipmentType().getId().equals(equipmentTypeId)) opening+=b.getOpeningBalance();

        List<Purchase> ps = baseId==null ? purchases.findByPurchaseDateBetween(from,to) : purchases.findByBaseIdAndPurchaseDateBetween(baseId,from,to);
        for(Purchase p:ps) if(equipmentTypeId==null || p.getEquipmentType().getId().equals(equipmentTypeId)) purchase+=p.getQuantity();

        List<Transfer> ts = transfers.findByTransferDateBetween(from,to);
        for(Transfer t:ts) if(equipmentTypeId==null || t.getEquipmentType().getId().equals(equipmentTypeId)){
            if(baseId==null || t.getToBase().getId().equals(baseId)) in+=t.getQuantity();
            if(baseId==null || t.getFromBase().getId().equals(baseId)) out+=t.getQuantity();
        }

        List<Expenditure> es = baseId==null ? expenditures.findByExpenditureDateBetween(from,to) : expenditures.findByBaseIdAndExpenditureDateBetween(baseId,from,to);
        for(Expenditure e:es) if(equipmentTypeId==null || e.getEquipmentType().getId().equals(equipmentTypeId)) expended+=e.getQuantity();

        if(baseId!=null){
            for(Assignment a:assignments.findByBaseIdOrderByAssignedDateDesc(baseId))
                if(!a.getAssignedDate().isAfter(to) && !a.getAssignedDate().isBefore(from) &&
                   (equipmentTypeId==null || a.getEquipmentType().getId().equals(equipmentTypeId))) assigned+=a.getQuantity();
        } else {
            for(Assignment a:assignments.findAll())
                if(!a.getAssignedDate().isAfter(to) && !a.getAssignedDate().isBefore(from) &&
                   (equipmentTypeId==null || a.getEquipmentType().getId().equals(equipmentTypeId))) assigned+=a.getQuantity();
        }

        int net=purchase+in-out;
        int closing=opening+net-expended;

        Map<String,Object> m=new LinkedHashMap<>();
        m.put("openingBalance",opening);m.put("purchases",purchase);m.put("transferIn",in);m.put("transferOut",out);
        m.put("netMovement",net);m.put("assigned",assigned);m.put("expended",expended);m.put("closingBalance",closing);
        m.put("from",from);m.put("to",to);m.put("baseId",baseId);m.put("equipmentTypeId",equipmentTypeId);
        return m;
    }
}
