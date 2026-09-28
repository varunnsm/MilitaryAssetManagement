package com.mams.entity;

import jakarta.persistence.*;

@Entity
@Table(name="equipment_types")
public class EquipmentType {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true) private String name;
    @Column(nullable=false) private String category;

    public EquipmentType() {}
    public EquipmentType(String name,String category){this.name=name;this.category=category;}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
}
