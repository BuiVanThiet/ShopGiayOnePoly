package com.example.shopgiayonepoly.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name_table")
    private String nameTable;
    @Column(name = "id_table")
    private Integer idTable;
    @Column(name = "attribute_name")
    private String attributeName;
    @Column(name = "new_value")
    private String newValue;
    @Column(name = "old_value")
    private String oldValue;
    @ManyToOne()
    @JoinColumn(name = "id_staff")
    private Staff staff;
    @Column(name = "at_time")
    private Date atTime;
    @Column(name = "note")
    private String note;
}
