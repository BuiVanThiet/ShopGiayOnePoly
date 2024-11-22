package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "timekeeping")
@ToString
public class Timekeeping extends Base {
    @ManyToOne
    @JoinColumn(name = "id_staff")
    private Staff staff;
    @Column(name = "time_start_root")
    private LocalTime timeStartRoot;
    @Column(name = "time_end_root")
    private LocalTime timeEndRoot;
    @Column(name = "note")
    private String note;
}
