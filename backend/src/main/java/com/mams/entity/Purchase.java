package com.mams.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="purchases")
public class Purchase {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="base_id") private Base base;
    @ManyToOne(optional=false) @JoinColumn(name="equipment_type_id") private EquipmentType equipmentType;
    @Column(nullable=false) private Integer quantity;
    @Column(nullable=false) private LocalDate purchaseDate;
    private String referenceNumber;
    private LocalDateTime createdAt;

    @PrePersist void onCreate(){createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public Base getBase(){return base;} public void setBase(Base v){base=v;}
    public EquipmentType getEquipmentType(){return equipmentType;} public void setEquipmentType(EquipmentType v){equipmentType=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public LocalDate getPurchaseDate(){return purchaseDate;} public void setPurchaseDate(LocalDate v){purchaseDate=v;}
    public String getReferenceNumber(){return referenceNumber;} public void setReferenceNumber(String v){referenceNumber=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
