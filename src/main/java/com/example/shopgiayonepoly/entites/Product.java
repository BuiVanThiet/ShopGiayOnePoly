package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends Base {
    @Column(name = "code_product")
    private String codeProduct;
    @Column(name = "name_product")
    private String nameProduct;
    @ManyToOne
    @JoinColumn(name = "id_material")
    private Material material;
    @ManyToOne
    @JoinColumn(name = "id_manufacturer")
    private Manufacturer manufacturer;
    @ManyToOne
    @JoinColumn(name = "id_origin")
    private Origin origin;
    @Column(name = "describe")
    private String describe;
    @ManyToMany
    @JoinTable(
            name ="category_product",
            joinColumns = @JoinColumn(name = "id_category"),
            inverseJoinColumns = @JoinColumn(name = "id_product")
    )
//    @JsonBackReference
    private List<Category> categories;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}
