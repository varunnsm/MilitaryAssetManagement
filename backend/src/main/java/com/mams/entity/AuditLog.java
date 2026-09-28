package com.mams.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="audit_logs")
public class AuditLog {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="user_id") private User user;
    @Column(nullable=false) private String action;
    @Column(nullable=false) private String entityType;
    private Long entityId;
    @Column(length=1000) private String description;
    @Column(nullable=false) private LocalDateTime timestamp;
    private String ipAddress;

    @PrePersist void onCreate(){if(timestamp==null)timestamp=LocalDateTime.now();}
    public Long getId(){return id;}
    public User getUser(){return user;} public void setUser(User v){user=v;}
    public String getAction(){return action;} public void setAction(String v){action=v;}
    public String getEntityType(){return entityType;} public void setEntityType(String v){entityType=v;}
    public Long getEntityId(){return entityId;} public void setEntityId(Long v){entityId=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public LocalDateTime getTimestamp(){return timestamp;} public void setTimestamp(LocalDateTime v){timestamp=v;}
    public String getIpAddress(){return ipAddress;} public void setIpAddress(String v){ipAddress=v;}
}
