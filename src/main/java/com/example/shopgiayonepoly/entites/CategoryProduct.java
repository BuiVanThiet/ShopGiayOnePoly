package com.example.shopgiayonepoly.entites;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category_product")
@IdClass(CategoryProduct.CategoryProductId.class)
public class CategoryProduct {

    @Id
    @Column(name = "id_product")
    private int idProduct;

    @Id
    @Column(name = "id_category")
    private int idCategory;

    @ManyToOne
    @JoinColumn(name = "id_product", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_category", insertable = false, updatable = false)
    private Category category;

    // Khóa tổng hợp được định nghĩa trong class CategoryProduct
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryProductId implements Serializable {
        private int idProduct;
        private int idCategory;
    }
}
