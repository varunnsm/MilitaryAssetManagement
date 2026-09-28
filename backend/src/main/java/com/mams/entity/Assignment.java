package com.mams.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="assignments")
public class Assignment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="base_id") private Base base;
    @ManyToOne(optional=false) @JoinColumn(name="equipment_type_id") private EquipmentType equipmentType;
    @Column(nullable=false) private String personnelName;
    @Column(nullable=false) private Integer quantity;
    @Column(nullable=false) private LocalDate assignedDate;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private AssignmentStatus status;

    public Long getId(){return id;}
    public Base getBase(){return base;} public void setBase(Base v){base=v;}
    public EquipmentType getEquipmentType(){return equipmentType;} public void setEquipmentType(EquipmentType v){equipmentType=v;}
    public String getPersonnelName(){return personnelName;} public void setPersonnelName(String v){personnelName=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public LocalDate getAssignedDate(){return assignedDate;} public void setAssignedDate(LocalDate v){assignedDate=v;}
    public AssignmentStatus getStatus(){return status;} public void setStatus(AssignmentStatus v){status=v;}
}
