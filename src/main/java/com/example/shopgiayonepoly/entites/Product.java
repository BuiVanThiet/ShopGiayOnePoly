package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends Base implements Serializable {
    private static final long serialVersionUID = 1L;
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
    @ManyToOne
    @JoinColumn(name = "id_sole")
    private Sole sole;
    @Column(name = "describe")
    private String describe;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "category_product",
            joinColumns = @JoinColumn(name = "id_product"), // Cột id_product phải liên kết với bảng Product
            inverseJoinColumns = @JoinColumn(name = "id_category") // Cột id_category phải liên kết với bảng Category
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Image> images;
}
