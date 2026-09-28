package com.mams.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="expenditures")
public class Expenditure {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="base_id") private Base base;
    @ManyToOne(optional=false) @JoinColumn(name="equipment_type_id") private EquipmentType equipmentType;
    @Column(nullable=false) private Integer quantity;
    @Column(nullable=false) private LocalDate expenditureDate;
    private String reason;

    public Long getId(){return id;}
    public Base getBase(){return base;} public void setBase(Base v){base=v;}
    public EquipmentType getEquipmentType(){return equipmentType;} public void setEquipmentType(EquipmentType v){equipmentType=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public LocalDate getExpenditureDate(){return expenditureDate;} public void setExpenditureDate(LocalDate v){expenditureDate=v;}
    public String getReason(){return reason;} public void setReason(String v){reason=v;}
}
