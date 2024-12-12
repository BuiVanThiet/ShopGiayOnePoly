package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.baseMethod.BaseProduct;
import com.example.shopgiayonepoly.dto.request.AttributeRequet;
import com.example.shopgiayonepoly.entites.*;
import com.example.shopgiayonepoly.service.CashierInventoryService;
import com.example.shopgiayonepoly.service.TimekeepingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-api")
public class ProductRestController extends BaseProduct {

    @Autowired
    TimekeepingService timekeepingService;
    @Autowired
    CashierInventoryService cashierInventoryService;
    @GetMapping("/search")
    @ResponseBody
    public List<Product> searchProducts(Integer idCategory, String searchTerm, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        return productService.findProducts(idCategory, searchTerm);
    }

    @GetMapping("/findProductDelete")
    @ResponseBody
    public List<Product> findProductDelete(Integer idCategory, String searchTerm, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        return productService.findProductDelete(idCategory, searchTerm);
    }

    @GetMapping("/detail/search")
    @ResponseBody
    public List<ProductDetail> searchProductDetail(String searchTerm, Integer idProduct, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        return productService.searchProductDetailsByKeyword(searchTerm, idProduct);
    }

    @GetMapping("/productList")
    @ResponseBody
    public List<Product> getProductList(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        return productService.findAll(); // Trả về danh sách sản phẩm
    }


    @GetMapping("/get-one/{id}")
    public ResponseEntity<Product> getOneByID(@PathVariable Integer id, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getImage/{productId}")
    public ResponseEntity<List<Image>> getImagesByProductId(@PathVariable Integer productId, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<Image> images = productService.findAllImagesByProductId(productId);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/getCategory/{productId}")
    public ResponseEntity<List<Integer>> getCategoriesByProductId(@PathVariable Integer productId, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        List<CategoryProduct> categories = productService.findAllCategoryByProductId(productId);
        List<Integer> idCategories = categories.stream()
                .map(CategoryProduct::getIdCategory)
                .collect(Collectors.toList());
        return ResponseEntity.ok(idCategories);
    }


    @GetMapping("/check-code/{codeProduct}")
    public ResponseEntity<Product> checkProductCode(@PathVariable String codeProduct, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        Optional<Product> product = productService.getOneProductByCodeProduct(codeProduct);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteProduct(@RequestBody Map<String, Object> payload, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        Map<String, Object> response = new HashMap<>();
        try {
            int id;
            int status;
            // Lấy id và status từ payload, kiểm tra kiểu dữ liệu
            if (payload.get("id") instanceof Integer) {
                id = (Integer) payload.get("id");
            } else {
                id = Integer.parseInt(payload.get("id").toString());
            }

            if (payload.get("status") instanceof Integer) {
                status = (Integer) payload.get("status");
            } else {
                status = Integer.parseInt(payload.get("status").toString());
            }

            productService.updateStatus(id, status);

            response.put("success", true);
            response.put("message", "Xóa thành công");
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            // Xử lý lỗi parse dữ liệu
            response.put("success", false);
            response.put("message", "Dữ liệu không hợp lệ: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Xử lý lỗi khác
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/attribute/quickly-add")
    public ResponseEntity<Map<String, String>> addAttribute(@RequestBody AttributeRequet request, HttpSession session) {
        Map<String, String> thongBao = new HashMap<>();
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if (staffLogin == null) {
            thongBao.put("message", "Nhân viên chưa đăng nhập");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        if (staffLogin.getStatus() != 1) {
            thongBao.put("message", "Nhân viên đang bị ngừng hoạt động!");
            thongBao.put("check", "3");
            return ResponseEntity.ok(thongBao);
        }
        Map<String,String> checkLoginAndLogout = checkLoginAndLogOutByStaff(staffLogin.getId());
        String messMap = checkLoginAndLogout.get("message");
        if(!messMap.trim().equals("")) {
            thongBao.put("message",messMap);
            thongBao.put("check","3");
            return ResponseEntity.ok(thongBao);
        }
        boolean checkCode = true;
        boolean checkName = true;
        switch (request.getType()) {
            case "category":
                Category category = new Category();
                category.setCodeCategory(request.getCode());
                category.setNameCategory(request.getName());
                for (Category listCategory : categoryService.findAll()) {
                    if (category.getCodeCategory().trim().toLowerCase().equals(listCategory.getCodeCategory().trim().toLowerCase())) {
                        checkCode = false;
                        break;
                    }
                }
                for (Category listCategory : categoryService.findAll()) {
                    if (category.getNameCategory().trim().toLowerCase().equals(listCategory.getNameCategory().trim().toLowerCase())) {
                        checkName = false;
                        break;
                    }
                }
                if (!checkCode || !checkName || category.getCodeCategory().isEmpty() || category.getNameCategory().isEmpty() || category.getCodeCategory().length() > 10 || category.getNameCategory().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    category.setStatus(1);
                    categoryService.save(category);
                    thongBao.put("message", "Thêm danh mục thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            case "color":
                Color color = new Color();
                color.setCodeColor(request.getCode());
                color.setNameColor(request.getName());

                for (Color listColor : colorService.findAll()) {
                    if (color.getCodeColor().trim().toLowerCase().equals(listColor.getCodeColor().trim().toLowerCase())) {
                        checkCode = false;
                    }
                }
                for (Color listColor : colorService.findAll()) {
                    if (color.getNameColor().trim().toLowerCase().equals(listColor.getNameColor().trim().toLowerCase())) {
                        checkName = false;
                    }
                }
                if (!checkCode || !checkName || color.getCodeColor().isEmpty() || color.getNameColor().isEmpty() || color.getCodeColor().length() > 10 || color.getNameColor().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    color.setStatus(1);
                    colorService.save(color);
                    thongBao.put("message", "Thêm màu sắc thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            case "size":
                Size size = new Size();
                size.setCodeSize(request.getCode());
                size.setNameSize(request.getName());
                for (Size listSize : sizeService.findAll()) {
                    if (size.getCodeSize().trim().toLowerCase().equals(listSize.getCodeSize().trim().toLowerCase())) {
                        checkCode = false;
                    }
                }
                for (Size listSize : sizeService.findAll()) {
                    if (size.getNameSize().trim().toLowerCase().equals(listSize.getNameSize().trim().toLowerCase())) {
                        checkName = false;
                    }
                }
                if (!checkCode || !checkName || size.getCodeSize().isEmpty() || size.getNameSize().isEmpty() || size.getCodeSize().length() > 10 || size.getNameSize().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    size.setStatus(1);
                    sizeService.save(size);
                    thongBao.put("message", "Thêm kích cỡ thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            case "material":
                Material material = new Material();
                material.setCodeMaterial(request.getCode());
                material.setNameMaterial(request.getName());
                for (Material listMaterial : materialService.findAll()) {
                    if (material.getCodeMaterial().trim().toLowerCase().equals(listMaterial.getCodeMaterial().trim().toLowerCase())) {
                        checkCode = false;
                    }
                }
                for (Material listMaterial : materialService.findAll()) {
                    if (material.getNameMaterial().trim().toLowerCase().equals(listMaterial.getNameMaterial().trim().toLowerCase())) {
                        checkName = false;
                    }
                }
                if (!checkCode || !checkName || material.getCodeMaterial().isEmpty() || material.getNameMaterial().isEmpty() || material.getCodeMaterial().length() > 10 || material.getNameMaterial().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    material.setStatus(1);
                    materialService.save(material);
                    thongBao.put("message", "Thêm chất liệu thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            case "manufacturer":
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setCodeManufacturer(request.getCode());
                manufacturer.setNameManufacturer(request.getName());
                for (Manufacturer listManufacturer : manufacturerService.findAll()) {
                    if (manufacturer.getCodeManufacturer().trim().toLowerCase().equals(listManufacturer.getCodeManufacturer().trim().toLowerCase())) {
                        checkCode = false;
                    }
                }
                for (Manufacturer listManufacturer : manufacturerService.findAll()) {
                    if (manufacturer.getNameManufacturer().trim().toLowerCase().equals(listManufacturer.getNameManufacturer().trim().toLowerCase())) {
                        checkName = false;
                    }
                }
                if (!checkCode || !checkName || manufacturer.getCodeManufacturer().isEmpty() || manufacturer.getNameManufacturer().isEmpty() || manufacturer.getCodeManufacturer().length() > 10 || manufacturer.getNameManufacturer().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    manufacturer.setStatus(1);
                    manufacturerService.save(manufacturer);
                    thongBao.put("message", "Thêm nhà sản xuất thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            case "origin":
                Origin origin = new Origin();
                origin.setCodeOrigin(request.getCode());
                origin.setNameOrigin(request.getName());
                for (Origin listOrigin : originService.findAll()) {
                    if (origin.getCodeOrigin().trim().toLowerCase().equals(listOrigin.getCodeOrigin().trim().toLowerCase())) {
                        checkCode = false;
                    }
                }
                for (Origin listOrigin : originService.findAll()) {
                    if (origin.getNameOrigin().trim().toLowerCase().equals(listOrigin.getNameOrigin().trim().toLowerCase())) {
                        checkName = false;
                    }
                }
                if (!checkCode || !checkName || origin.getCodeOrigin().isEmpty() || origin.getNameOrigin().isEmpty() || origin.getCodeOrigin().length() > 10 || origin.getNameOrigin().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    origin.setStatus(1);
                    originService.save(origin);
                    thongBao.put("message", "Thêm xuất xứ thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            case "sole":
                Sole sole = new Sole();
                sole.setCodeSole(request.getCode());
                sole.setNameSole(request.getName());
                for (Sole listSole : soleService.findAll()) {
                    if (sole.getCodeSole().trim().toLowerCase().equals(listSole.getCodeSole().trim().toLowerCase())) {
                        checkCode = false;
                    }
                }
                for (Sole listSole : soleService.findAll()) {
                    if (sole.getNameSole().trim().toLowerCase().equals(listSole.getNameSole().trim().toLowerCase())) {
                        checkName = false;
                    }
                }
                if (!checkCode || !checkName || sole.getCodeSole().isEmpty() || sole.getNameSole().isEmpty() || sole.getCodeSole().length() > 10 || sole.getNameSole().length() > 50) {
                    thongBao.put("message", "Dữ liệu không hợp lệ");
                    thongBao.put("check", "2");
                } else {
                    sole.setStatus(1);
                    soleService.save(sole);
                    thongBao.put("message", "Thêm đế giày thành công");
                    thongBao.put("check", "1");
                }
                return ResponseEntity.ok(thongBao);
            default:
                thongBao.put("message", "Lỗi khi thêm thuộc tính");
                thongBao.put("check", "3");
                return ResponseEntity.ok(thongBao);
        }
    }

    @GetMapping("/attribute/list")
    public ResponseEntity<?> getAttributeList(@RequestParam String type, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        switch (type) {
            case "category":
                List<Category> categories = categoryService.getCategoryNotStatus0();
                return ResponseEntity.ok(categories);
            case "color":
                List<Color> colors = colorService.getColorNotStatus0();
                return ResponseEntity.ok(colors);
            case "size":
                List<Size> sizes = sizeService.getSizeNotStatus0();
                return ResponseEntity.ok(sizes);
            case "material":
                List<Material> materials = materialService.getMaterialNotStatus0();
                return ResponseEntity.ok(materials);
            case "manufacturer":
                List<Manufacturer> manufacturers = manufacturerService.getManufacturerNotStatus0();
                return ResponseEntity.ok(manufacturers);
            case "origin":
                List<Origin> origins = originService.getOriginNotStatus0();
                return ResponseEntity.ok(origins);
            case "sole":
                List<Sole> soles = soleService.getSoleNotStatus0();
                return ResponseEntity.ok(soles);
            default:
                return ResponseEntity.badRequest().body("Loại thuộc tính không hợp lệ!");
        }
    }


    @GetMapping("/findAllCodeProduct")
    public List<String> findAllCodeProduct(HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        return productService.findAllCodeProduct();
    }

    @PostMapping("/restore")
    public void restoreProduct(Integer id, Integer status, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return ;
        }
        if(staffLogin.getStatus() != 1) {
            return ;
        }
        productService.updateStatus(id, status);
    }

    @PostMapping("/delete-multiple")
    public ResponseEntity<?> deleteMultipleProducts(@RequestBody List<Integer> ids, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("Danh sách sản phẩm không hợp lệ.");
        }

        try {
            for (Integer item : ids) {
                productService.updateStatus(item, 0);  // Cập nhật trạng thái xóa
            }
            return ResponseEntity.ok("Xóa sản phẩm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    @PostMapping("/restore-multiple")
    public ResponseEntity<?> restoreMultipleProducts(@RequestBody List<Integer> ids, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("Danh sách sản phẩm không hợp lệ.");
        }

        try {
            for (Integer item : ids) {
                productService.updateStatus(item, 1);  // Cập nhật trạng thái xóa
            }
            return ResponseEntity.ok("Khôi phục sản phẩm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    @GetMapping("/find-quantity")
    public Integer findAllQuantity(Integer id, HttpSession session) {
        Staff staffLogin = (Staff) session.getAttribute("staffLogin");
        if(staffLogin == null) {
            return null;
        }
        if(staffLogin.getStatus() != 1) {
            return null;
        }
        return productService.findQuantityByIDProduct(id);
    }

    @PostMapping("/update-product-detail-status/{id}")
    public ResponseEntity<Map<String, Object>> updateProductDetailStatus(@PathVariable("id") Integer id) {
        ProductDetail productDetail = productDetailRepository.findById(id).orElse(null);
        Map<String, Object> response = new HashMap<>();

        if (productDetail != null) {
            // Cập nhật trạng thái của sản phẩm chi tiết
            productDetail.setStatus(productDetail.getStatus() == 1 ? 2 : 1); // Chuyển trạng thái (1 <-> 2)
            productDetailRepository.save(productDetail);
            response.put("success", true);
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    protected Map<String,String> checkLoginAndLogOutByStaff(Integer idStaff) {
        Map<String,String> thongBao = new HashMap<>();
        String checkLogin = getCheckStaffAttendanceYetBill(idStaff,1);
        String checkLogOut = getCheckStaffAttendanceYetBill(idStaff,2);
        System.out.println(checkLogin);
        if(!checkLogin.equals("Có")) {
            thongBao.put("message","Mời bạn điểm danh trước khi làm việc!");
            return thongBao;
        }

        if(checkLogin.equals("Có") && checkLogOut.equals("Có")) {
            thongBao.put("message","Bạn đã điểm danh vào và ra rồi, không thể làm việc được nữa!");
            return thongBao;
        }
        thongBao.put("message","");
        return thongBao;
    }


    protected String getCheckStaffAttendanceYetBill(
//            @PathVariable("id") Integer idStaff,@PathVariable("type") Integer timekeepingTypeCheck
            Integer idStaff, Integer timekeepingTypeCheck
    ) {
        List<Object[]> checkLoginLogOut = this.timekeepingService.getCheckStaffAttendanceYet(idStaff, timekeepingTypeCheck);

        // Kiểm tra nếu danh sách không rỗng và có kết quả
        if (!checkLoginLogOut.isEmpty() && checkLoginLogOut.get(0).length > 0) {
            // Lấy giá trị đầu tiên từ kết quả
            return checkLoginLogOut.get(0)[0].toString();
        }
        // Trường hợp không có dữ liệu
        return "Không";
    }

}