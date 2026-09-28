package com.mams.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="transfers")
public class Transfer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="from_base_id") private Base fromBase;
    @ManyToOne(optional=false) @JoinColumn(name="to_base_id") private Base toBase;
    @ManyToOne(optional=false) @JoinColumn(name="equipment_type_id") private EquipmentType equipmentType;
    @Column(nullable=false) private Integer quantity;
    @Column(nullable=false) private LocalDate transferDate;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TransferStatus status;
    private String remarks;
    private LocalDateTime createdAt;

    @PrePersist void onCreate(){createdAt=LocalDateTime.now(); if(status==null)status=TransferStatus.COMPLETED;}
    public Long getId(){return id;}
    public Base getFromBase(){return fromBase;} public void setFromBase(Base v){fromBase=v;}
    public Base getToBase(){return toBase;} public void setToBase(Base v){toBase=v;}
    public EquipmentType getEquipmentType(){return equipmentType;} public void setEquipmentType(EquipmentType v){equipmentType=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public LocalDate getTransferDate(){return transferDate;} public void setTransferDate(LocalDate v){transferDate=v;}
    public TransferStatus getStatus(){return status;} public void setStatus(TransferStatus v){status=v;}
    public String getRemarks(){return remarks;} public void setRemarks(String v){remarks=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
