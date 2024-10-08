package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category extends Base {
    @Column(name = "code_category")
    private String codeCategory;
    @Column(name = "name_category")
    private String nameCategory;
    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private Set<Product> products = new HashSet<>();

}
