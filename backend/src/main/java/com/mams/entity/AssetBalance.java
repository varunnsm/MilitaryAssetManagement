package com.mams.entity;

import jakarta.persistence.*;

@Entity
@Table(name="asset_balances", uniqueConstraints=@UniqueConstraint(columnNames={"base_id","equipment_type_id"}))
public class AssetBalance {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="base_id") private Base base;
    @ManyToOne(optional=false) @JoinColumn(name="equipment_type_id") private EquipmentType equipmentType;
    @Column(nullable=false) private Integer openingBalance;

    public Long getId(){return id;}
    public Base getBase(){return base;} public void setBase(Base v){base=v;}
    public EquipmentType getEquipmentType(){return equipmentType;} public void setEquipmentType(EquipmentType v){equipmentType=v;}
    public Integer getOpeningBalance(){return openingBalance;} public void setOpeningBalance(Integer v){openingBalance=v;}
}
