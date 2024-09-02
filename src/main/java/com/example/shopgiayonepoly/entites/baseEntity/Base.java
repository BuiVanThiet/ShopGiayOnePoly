package com.example.shopgiayonepoly.entites.baseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@ToString
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "status")
    @NotBlank(message = "Mời bạn chọn trạng thái!")
    private String status;
    @PrePersist
    public void setDate(){
        this.createDate = new Date();
        this.updateDate = new Date();
    }
    @PreUpdate
    public void setDoiDate(){
        this.updateDate = new Date();
    }
}
