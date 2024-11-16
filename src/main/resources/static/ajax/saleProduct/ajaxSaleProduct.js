var selectSole;
var selectColor ;
var selectSize ;
var selectMaterial;
var selectManufacturer;
var selectOrigin;
var selectCategory;
var checkProduct = 1;
function loadSaleProduct(page) {
    $.ajax({
        type: "GET",
        url: "/api-sale-product/list/"+page,
        success: function (response) {
            console.log(response)
            var tableSaleProduct = $('#tableSaleProduct');
            var noDataSaleProductContainer = $('#noDataSaleProductContainer');
            tableSaleProduct.empty();
            if(response.length === 0) {
                noDataSaleProductContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có đợt giảm giá nào!</p>
                    `);
                tableSaleProduct.closest('table').hide();
                noDataSaleProductContainer.show()
            }else {
                tableSaleProduct.closest('table').show();
                noDataSaleProductContainer.hide()
                response.forEach(function (saleProduct,index) {
                    const start = formatDate(saleProduct[5]);
                    const end = formatDate(saleProduct[6]);
                    var action = '';
                    var endDate = new Date(end);
                    var today = new Date();
                    today.setHours(0, 0, 0, 0);
                    if(saleProduct[7] === 1) {
                        action = `
                            <td>
                                <a href="/sale-product/delete/${saleProduct[0]}"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Bạn có muốn xóa phiếu giảm giá này không?')">Xóa</a>
                            </td>
                            <td>
                                <a href="/sale-product/edit/${saleProduct[0]}"
                                   class="btn btn-warning btn-sm">Sửa</a>
                            </td>
                            <td>
                                <button type="button" class="btn btn-sm btn-outline-success " data-bs-toggle="modal" data-bs-target="#listProductModal" onclick="resetFilterProductSale()">Xem</button>
                            </td>
                    `;
                    }else if (saleProduct[7] === 2 || saleProduct[7] === 0) {
                        action = `
                            <td>
                                <a href="/sale-product/restore/${saleProduct[0]}"
                                   class="btn btn-success btn-sm"
                                   onclick="return confirm('Bạn có muốn khôi phục phiếu giảm giá này không?')">Khôi
                                    phục</a>
                            </td>
                    `;
                    }else if (saleProduct[7] === 3){
                    //     action = `
                    //          <td>
                    //             <a href="/voucher/delete/${saleProduct[0]}"
                    //                class="btn btn-danger btn-sm"
                    //                onclick="return confirm('Bạn có muốn xóa phiếu giảm giá này không?')">Xóa</a>
                    //         </td>
                    //         <td>
                    //             <a href="/voucher/extend/${saleProduct[0]}"
                    //                class="btn btn-success btn-sm"
                    //                onclick="return confirm('Bạn có muốn gia hạn phiếu giảm giá này không?')">Gia
                    //                 hạn</a>
                    //         </td>
                    // `;
                    }
                    tableSaleProduct.append(`
                    <tr>
                        <td>${saleProduct[1]}</td>
                        <td>${saleProduct[2]}</td>
                        <td>${saleProduct[3]}</td>
                        <td>${saleProduct[4]}</td>
                        <td>${start}</td>
                        <td>${end}</td>
                        ${action}
                    </tr>
                `);
                })
            }

        },
        error: function (xhr) {

            console.error('loi ' + xhr.responseText)
        }
    })
}

function formatDate(inputDate) {
    // Chuyển đổi chuỗi thành đối tượng Date
    const date = new Date(inputDate);

    // Sử dụng Intl.DateTimeFormat để định dạng ngày theo yêu cầu
    const formattedDate = new Intl.DateTimeFormat('vi-VN', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
    }).format(date);

    // Trả về giá trị đã định dạng
    return formattedDate;
}

function maxPageSaleProduct() {
    $.ajax({
        type: "GET",
        url: "/api-sale-product/max-page-sale-product",
        success: function (response) {
            createPagination('maxPageSaleProduct-manageSaleProduct',response,1)
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function searchSaleProduct() {
    var discountTypeValue = $('#searchDiscountTypeInput').val().trim();
    var discountTypeCheck = discountTypeValue === '' ? null : parseInt(discountTypeValue);

    var nameCheck = $('#inputSearchSaleProduct').val().trim();

    var statusCheckValue = $('#searchStatusSaleProduct').val().trim();
    var statusCheck = statusCheckValue === '' ? null : parseInt(statusCheckValue); // Tránh NaN nếu rỗng

    $.ajax({
        type: "POST",
        url: "/api-sale-product/search-sale-product",
        contentType: "application/json",
        data: JSON.stringify({
            discountTypeCheck: discountTypeCheck, // Gửi giá trị đã kiểm tra
            nameCheck: nameCheck, // Gửi tên tìm kiếm
            statusCheck: statusCheck // Gửi trạng thái
        }),
        success: function (response) {
            console.log(response)
            loadSaleProduct(1);
            maxPageSaleProduct();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}
function startLoadAjaxSaleProduct() {
    $.ajax({
        type: "Get",
        url: "/api-sale-product/reset-filter-sale-product",
        success: function (response) {
            loadSaleProduct(1);
            maxPageSaleProduct();
            loadCategoryIntoSelect()
            loadColorIntoSelect()
            loadSizeIntoSelect()
            loadMaterialIntoSelect()
            loadManufacturerIntoSelect()
            loadOriginIntoSelect()
            loadSoleIntoSelect()
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}
function getSelectedStatus() {
    var selectedValue = $("input[name='statusSaleProduct']:checked").val();
    console.log(selectedValue); // In giá trị được chọn (1 hoặc 2)
    return selectedValue;
}
function addSaleProductNew() {
    var selectedStatus = getSelectedStatus();
    $.ajax({
        type: "POST",
        url: "/api-sale-product/add-new-sale-product",
        contentType: "application/json",
        data: JSON.stringify({
            codeSale: $('#codeSaleProduct').val().trim(),
            nameSale: $('#nameSaleProduct').val().trim(),
            discountType: parseInt($('#discountType').val().trim()),
            discountValue: $('#value').val().trim(),
            startDate: $('#startDate').val().trim(),
            endDate: $('#endDate').val().trim(),
            status: selectedStatus
        }),
        success: function (response) {
            createToast(response.check, response.message);
            loadSaleProduct(1);
            maxPageSaleProduct();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function resetFormSearch() {
    $('#searchDiscountTypeInput').val('');
    $('#searchStatusVoucher').val('1'); // Nếu trạng thái mặc định là "Hoạt động"

    // Reset trường input search về rỗng
    $('#inputSearchVoucher').val('');
    searchSaleProduct();
}
// hien thi san pham

function loadProduct(pageNumber) {
    $.ajax({
        type: "GET",
        url: "/api-sale-product/all-product/" + pageNumber,
        success: function(response) {
            console.log(response)
            updateProductTable(response);
        },
        error: function(xhr) {
            console.error("Lỗi khi hiển thị chi tiết hóa đơn: " + xhr.responseText);
        },
    });
}

function updateProductTable(response) {
    var tbody = $('#tableProduct');
    var noDataContainer = $('#noDataProductContainer');
    tbody.empty(); // Xóa các dòng cũ

    if (response.length === 0) {
        // Nếu không có dữ liệu, hiển thị ảnh
        noDataContainer.html(`
                <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                     alt="Lỗi ảnh" style="width: auto; height: 100px;">
                     <p class="text-center">Không có sản phẩm nào!</p>
            `);
        noDataContainer.show();
        tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
    } else {
        noDataContainer.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
        tbody.closest('table').show(); // Hiển thị lại table nếu có dữ liệu
        response.forEach(function(productDetail, index) {
            var imagesHtml = '';

            //lấy ảnh sản phẩm
            var nameImage = productDetail[17].split(',');
            nameImage.forEach(function (imageProduct,indexImage) {
                imagesHtml += `
                      <div  data-bs-interval="10000" class="carousel-item ${indexImage === 0 ? 'active' : ''}">
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1724519685/${imageProduct.trim()}" class="d-block w-100" alt="Product Image 1">
                      </div>
                     `;
            })

            var saleBadge = '';
            if(productDetail[15] != 'Không giảm') {
                saleBadge = `<div class="bill-label-sale">${productDetail[15]}</div>`;
            }else {
                saleBadge = '';
            }

            var priceSale = '';
            if (productDetail[15] != 'Không giảm') {
                // Gán giá đã giảm và giá gốc vào biến priceSaleAndRoot
                priceSale = `
                    <span class="text-danger fs-5">${Math.trunc(productDetail[12]).toLocaleString('en-US')} VNĐ</span>
                `;
            } else {
                // Nếu không có chương trình giảm giá, chỉ hiển thị giá gốc
                priceSale = `
                    Không có!
                `;
            }

            tbody.append(`
                <tr>
                    <td>
                        ${productDetail[18]}
                    </td>
                    <td>
                        <div id="productCarousel1" class="carousel slide d-flex justify-content-center align-items-center" data-bs-ride="carousel">
                            <div class="carousel-inner" style="height: auto; width: 150px;"> <!-- Đặt chiều cao cố định cho carousel -->
                              ${imagesHtml}
                            </div>
                        </div>
                    </td>
                    <td style="font-size: 13px">
                      <h5 class="card-title fs-6">${productDetail[2]}</h5>
                      <p class="card-text">
                        Màu: <span class="text-primary-emphasis">${productDetail[3]}</span>
                        <br>
                        Kích cỡ: <span class="text-primary-emphasis">${productDetail[4]}</span>
                        <br>
                        Hãng: <span class="text-primary-emphasis">${productDetail[5]}</span>
                        <br>
                        Chất liệu: <span class="text-primary-emphasis">${productDetail[6]}</span>
                        <br>
                        Nơi sản xuất: <span class="text-primary-emphasis">${productDetail[7]}</span>
                        <br>
                        Loại đế: <span class="text-primary-emphasis">${productDetail[8]}</span>
                        <br>
                        Danh mục: <span class="text-primary-emphasis">${productDetail[16]}</span>
                      </p>
                    </td>
                    <td class="">
                        ${Math.trunc(productDetail[9]).toLocaleString('en-US')} VNĐ
                    </td>
                    <td>
                        ${priceSale}
                    </td>
                    <td>
                        <input type="checkbox" value="${productDetail[0]}" name="selectedProducts" id="product_apply${productDetail[0]}" />
                    </td>
                </tr>
              

            `);
            restoreCheckboxState();
        });

        // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
        $('.carousel').each(function() {
            $(this).carousel(); // Khởi tạo carousel cho từng phần tử
        });
    }
}

function filterProduct(statusCheckIdSale) {
    $.ajax({
        type: 'POST',
        url: '/api-sale-product/filter-product-deatail',  // Endpoint xử lý
        contentType: 'application/json',
        data: JSON.stringify({
            nameProduct: $('#nameSearch').val() ? $('#nameSearch').val().trim() : '',  // Kiểm tra null hoặc undefined
            idColors: $('#colorSearch').val() ? $('#colorSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idSizes: $('#sizeSearch').val() ? $('#sizeSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idMaterials: $('#materialSearch').val() ? $('#materialSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idManufacturers: $('#manufacturerSearch').val() ? $('#manufacturerSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idOrigins: $('#originSearch').val() ? $('#originSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idSoles: $('#soleSearch').val() ? $('#soleSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idCategories: $('#categorySearch').val() ? $('#categorySearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            statusCheckIdSale: statusCheckIdSale
        }),
        success: function (response) {
            loadProduct(1); // Gọi hàm để tải sản phẩm với trang đầu tiên
            getMaxPageProduct(); // Gọi hàm để lấy số trang tối đa
            console.log('Dữ liệu truyền về là:', response);
        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}

function loadCategoryIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/categoryAll",
        success: function (response) {
            const selectElement = $('#categores');
            selectElement.empty(); // Xóa các option cũ nếu có

            // Giả định response là một mảng các đối tượng client
            response.forEach(cate => {
                const option = $('<option>', {
                    value: cate.id, // giá trị của option
                    text: cate.nameCategory // tên hiển thị trong option
                });
                selectElement.append(option);
            });

            selectCategory = new MultiSelectTag('categores', {
                rounded: true,
                shadow: false,
                placeholder: 'Search',
                tagColor: {
                    textColor: '#327b2c',
                    borderColor: '#92e681',
                    bgColor: '#eaffe6',
                },
                onChange: function(values) {
                    let selectedValues = values.map(item => item.value).join(',');
                    document.getElementById('categorySearch').value = selectedValues;
                    console.log('category search: '+selectedValues)
                }
            });
            selectCategory.clearAll();
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadCategoryIntoSelect, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function loadColorIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/filter-color",
        success: function (response) {
            const selectElement = $('#colors');
            selectElement.empty(); // Xóa các option cũ nếu có

            // Giả định response là một mảng các đối tượng client
            response.forEach(color => {
                const option = $('<option>', {
                    value: color.id, // giá trị của option
                    text: color.nameColor // tên hiển thị trong option
                });
                selectElement.append(option);
            });

            // Gọi hàm MultiSelectTag sau khi đã nạp dữ liệu
            selectColor = new MultiSelectTag('colors', {
                rounded: true,
                shadow: false,
                placeholder: 'Search',
                tagColor: {
                    textColor: '#327b2c',
                    borderColor: '#92e681',
                    bgColor: '#eaffe6',
                },
                onChange: function(values) {
                    let selectedValues = values.map(item => item.value).join(',');
                    document.getElementById('colorSearch').value = selectedValues;
                    console.log('color search: '+selectedValues)
                }
            });
            selectColor.clearAll();
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadCategoryIntoSelect, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function loadSizeIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/filter-size",
        success: function (response) {
            const selectElement = $('#sizes');
            selectElement.empty(); // Xóa các option cũ nếu có

            // Giả định response là một mảng các đối tượng client
            response.forEach(size => {
                const option = $('<option>', {
                    value: size.id, // giá trị của option
                    text: size.nameSize // tên hiển thị trong option
                });
                selectElement.append(option);
            });

            selectSize = new MultiSelectTag('sizes', {
                rounded: true,
                shadow: false,
                placeholder: 'Search',
                tagColor: {
                    textColor: '#327b2c',
                    borderColor: '#92e681',
                    bgColor: '#eaffe6',
                },
                onChange: function(values) {
                    let selectedValues = values.map(item => item.value).join(',');
                    document.getElementById('sizeSearch').value = selectedValues;
                    console.log('size search: '+selectedValues)
                }
            });
            selectSize.clearAll();
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadCategoryIntoSelect, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function loadMaterialIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/filter-material",
        success: function (response) {
            const selectElement = $('#materials');
            selectElement.empty(); // Xóa các option cũ nếu có

            // Giả định response là một mảng các đối tượng client
            response.forEach(material => {
                const option = $('<option>', {
                    value: material.id, // giá trị của option
                    text: material.nameMaterial // tên hiển thị trong option
                });
                selectElement.append(option);
            });

            selectMaterial = new MultiSelectTag('materials', {
                rounded: true,
                shadow: false,
                placeholder: 'Search',
                tagColor: {
                    textColor: '#327b2c',
                    borderColor: '#92e681',
                    bgColor: '#eaffe6',
                },
                onChange: function(values) {
                    let selectedValues = values.map(item => item.value).join(',');
                    document.getElementById('materialSearch').value = selectedValues;
                    console.log('material search: '+selectedValues)
                }
            });
            selectMaterial.clearAll();
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadCategoryIntoSelect, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function loadManufacturerIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/filter-manufacturer",
        success: function (response) {
            const selectElement = $('#manufacturers');
            selectElement.empty(); // Xóa các option cũ nếu có

            // Giả định response là một mảng các đối tượng client
            response.forEach(manufacturer => {
                const option = $('<option>', {
                    value: manufacturer.id, // giá trị của option
                    text: manufacturer.nameManufacturer // tên hiển thị trong option
                });
                selectElement.append(option);
            });

            selectManufacturer = new MultiSelectTag('manufacturers', {
                rounded: true,
                shadow: false,
                placeholder: 'Search',
                tagColor: {
                    textColor: '#327b2c',
                    borderColor: '#92e681',
                    bgColor: '#eaffe6',
                },
                onChange: function(values) {
                    let selectedValues = values.map(item => item.value).join(',');
                    document.getElementById('manufacturerSearch').value = selectedValues;
                    console.log('manufacturer search: '+selectedValues)
                }
            });
            selectManufacturer.clearAll();
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadCategoryIntoSelect, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function loadOriginIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/filter-origin",
        success: function (response) {
            const selectElement = $('#origins');
            selectElement.empty(); // Xóa các option cũ nếu có

            // Giả định response là một mảng các đối tượng client
            response.forEach(origin => {
                const option = $('<option>', {
                    value: origin.id, // giá trị của option
                    text: origin.nameOrigin // tên hiển thị trong option
                });
                selectElement.append(option);
            });

            selectOrigin = new MultiSelectTag('origins', {
                rounded: true,
                shadow: false,
                placeholder: 'Search',
                tagColor: {
                    textColor: '#327b2c',
                    borderColor: '#92e681',
                    bgColor: '#eaffe6',
                },
                onChange: function(values) {
                    let selectedValues = values.map(item => item.value).join(',');
                    document.getElementById('originSearch').value = selectedValues;
                    console.log('origin search: '+selectedValues)
                }
            });
            selectOrigin.clearAll();
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadCategoryIntoSelect, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function loadSoleIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/filter-sole",
        success: function (response) {
            const selectElement = $('#soles');
            selectElement.empty(); // Xóa các option cũ nếu có

            // Giả định response là một mảng các đối tượng client
            response.forEach(sole => {
                const option = $('<option>', {
                    value: sole.id, // giá trị của option
                    text: sole.nameSole // tên hiển thị trong option
                });
                selectElement.append(option);
            });

            // Gọi hàm MultiSelectTag sau khi đã nạp dữ liệu
            selectSole = new MultiSelectTag('soles', {
                rounded: true,
                shadow: false,
                placeholder: 'Search',
                tagColor: {
                    textColor: '#327b2c',
                    borderColor: '#92e681',
                    bgColor: '#eaffe6',
                },
                onChange: function(values) {
                    let selectedValues = values.map(item => item.value).join(',');
                    document.getElementById('soleSearch').value = selectedValues;
                    console.log('sole search: '+selectedValues)
                }
            });
            selectSole.clearAll();
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        }
    });
}

function resetFilterProductSale() {
    document.getElementById('nameSearch').value='';
    document.getElementById('soleSearch').value='';
    selectSole.clearAll();
    document.getElementById('colorSearch').value='';
    selectColor.clearAll();
    document.getElementById('sizeSearch').value='';
    selectSize.clearAll();
    document.getElementById('materialSearch').value='';
    selectMaterial.clearAll();
    document.getElementById('manufacturerSearch').value='';
    selectManufacturer.clearAll();
    document.getElementById('originSearch').value='';
    selectOrigin.clearAll();
    document.getElementById('categorySearch').value='';
    selectCategory.clearAll();
    filterProduct(checkProduct);
}

// let selectedProductIds = JSON.parse(localStorage.getItem('selectedProductIds')) || [];
let selectedProductIds = [];
// Lắng nghe sự kiện thay đổi trạng thái của checkbox
$(document).on('change', 'input[name="selectedProducts"]', function() {
    let productId = $(this).val(); // Lấy giá trị ID từ checkbox

    if ($(this).is(':checked')) {
        // Nếu checkbox được chọn, thêm ID vào mảng nếu chưa có
        if (!selectedProductIds.includes(productId)) {
            selectedProductIds.push(productId);
        }
    } else {
        // Nếu checkbox bị bỏ chọn, xóa ID khỏi mảng
        selectedProductIds = selectedProductIds.filter(id => id !== productId);
    }

    // Lưu mảng selectedProductIds vào localStorage
    localStorage.setItem('selectedProductIds', JSON.stringify(selectedProductIds));

    console.log('Selected Product IDs:', selectedProductIds);
});

// Khôi phục trạng thái checkbox khi bảng được cập nhật hoặc trang được tải lại
function restoreCheckboxState() {
    $('input[name="selectedProducts"]').each(function() {
        let productId = $(this).val(); // Lấy giá trị ID của checkbox
        if (selectedProductIds.includes(productId)) {
            $(this).prop('checked', true); // Đánh dấu checkbox là đã chọn
        } else {
            $(this).prop('checked', false); // Bỏ chọn nếu không có trong mảng
        }
    });
}


function getMaxPageProduct() {
    $.ajax({
        type: "GET",
        url: "/api-sale-product/page-max-product",
        success: function(response) {
            createPagination('maxPageProduct-manageSaleProduct', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi o tao page ' + xhr.responseText);
        }
    });
}

$(document).ready(function () {
    $('#searchForm').submit(function (event) {
        event.preventDefault();
        searchSaleProduct()
    })
    $('#formFilterProduct').submit(function (event) {
        event.preventDefault();
        filterProduct(checkProduct);
    })
    startLoadAjaxSaleProduct();

});