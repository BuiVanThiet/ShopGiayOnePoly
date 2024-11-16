package com.example.shopgiayonepoly.dto.request.bill;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetailCheckMark2Request {
    private String nameProduct;             // Tên sản phẩm (tìm kiếm gần đúng)
    private Integer[] idColors;         // Lọc theo danh sách màu sắc
    private Integer[]  idSizes;          // Lọc theo danh sách kích thước
    private Integer[]  idMaterials;      // Lọc theo danh sách chất liệu
    private Integer[]  idManufacturers;  // Lọc theo danh sách nhà sản xuất
    private Integer[]  idOrigins;        // Lọc theo danh sách nơi xuất xứ
    private Integer[]  idSoles;          // Lọc theo danh sách đế giày (sole)
    private Integer[]  idCategories;     // Lọc theo danh sách danh mục
    private Integer statusCheckIdSale;

    public ProductDetailCheckMark2Request(String nameProduct, Integer[] idColors, Integer[] idSizes, Integer[] idMaterials, Integer[] idManufacturers, Integer[] idOrigins, Integer[] idSoles, Integer[] idCategories) {
        this.nameProduct = nameProduct;
        this.idColors = idColors;
        this.idSizes = idSizes;
        this.idMaterials = idMaterials;
        this.idManufacturers = idManufacturers;
        this.idOrigins = idOrigins;
        this.idSoles = idSoles;
        this.idCategories = idCategories;
    }
}

