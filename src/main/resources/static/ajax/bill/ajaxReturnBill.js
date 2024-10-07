function loadBillDetailFromReturnBill(page) {
    $.ajax({
        type: "GET",
        url: "/return-bill/bill-detail/"+page,
        success: function(response) {
            var tbody = $('#tableBillDetail');
            var noDataContainer = $('#noDataContainer');
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

                response.forEach(function(billDetail, index) {
                    var imagesHtml = '';

                    billDetail.productDetail.product.images.forEach(function(image, imgIndex) {
                        imagesHtml += `
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" data-bs-interval="2000">
                                <img style="height: auto; width: 100px;" src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh">
                            </div>`;
                    });
                    var btnReturnProduct = '';
                    if(billDetail.quantity == 0){
                        btnReturnProduct =`
                        `;
                    }else {
                        btnReturnProduct =`
                        <button type="button"
                                   class="btn btn-outline-danger btn-return-product-detail"
                                   data-bs-toggle="modal"
                                   data-bs-target="#returnProductModal"
                                   data-name="${billDetail.productDetail.product.nameProduct}"
                                   data-id="${billDetail.productDetail.id}"
                                   data-quantity="${billDetail.quantity}"
                                   data-price-buy="${billDetail.price}"
                                   >
                                    Trả
                                </button>
                        `;
                    }
                    tbody.append(`
                        <tr>
                            <th scope="row" class="text-center align-middle">${index + 1}</th>
                            <td class="text-center align-middle">
                                <div class="carousel slide" data-bs-ride="carousel">
                                    <div class="carousel-inner carousel-inner-bill-custom">
                                        ${imagesHtml}
                                    </div>
                                </div>
                            </td>
                            <td class="" style="max-height: 100px; overflow-y: auto; width: 150px;">
                                <div class="fs-4">
                                    ${billDetail.productDetail.product.nameProduct}
                                </div>
                                <div class="fs-6">
                                    Tên màu: ${billDetail.productDetail.color.nameColor}
                                    <br>
                                    Tên size: ${billDetail.productDetail.size.nameSize}
                                </div>
                            </td>

                            <td class="text-center align-middle">
                                   ${billDetail.price.toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
                                ${billDetail.quantity}
                            </td>
                            <td class="text-center align-middle">
                                ${billDetail.totalAmount.toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
                                ${btnReturnProduct}
                            </td>
                        </tr>`);
                });

                $(document).on('click', '.btn-return-product-detail', function() {
                    var nameProduct = $(this).data('name');
                    var idProductDetail = $(this).data('id');
                    var quantityProduct = $(this).data('quantity');
                    var priceBuy = $(this).data('price-buy');

                    // Gán các giá trị vào modal
                    $('#nameProduct').val(nameProduct);
                    $('#idProductDetail').val(idProductDetail);
                    $('#quantityProduct').val(quantityProduct);
                    $('#priceBuyProduct').val(priceBuy);

                    // Cập nhật lại biến `quantityProductByBill` khi mở modal
                    quantityProductByBill = parseFloat(quantityProduct) || 0;

                    // Reset lại modal khi mở
                    $('#quantityReturnProduct').val('');
                    cardError.css('display', 'block');
                    textError.text('Mời nhập số lượng trả!');
                    btnReturn.attr('disabled', true);
                });

                // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
                $('.carousel').each(function() {
                    $(this).carousel(); // Khởi tạo carousel cho từng phần tử
                });
            }
        },
        error: function(xhr) {
            console.error("Lỗi khi hiển thị chi tiết hóa đơn: " + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadBillDetail, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function maxPageBillDetailByIdBill() {
    $.ajax({
        type: "GET",
        url:"/return-bill/max-page-bill-detail",
        success: function (response) {
            createPagination('billDetailPageMax-returnBill', response, 1);
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(maxPageBillDetailByIdBill, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function loadReturnBill() {
    $.ajax({
        type: "GET",
        url: "/return-bill/bill-return-detail",
        success: function (response) {
            var tbody = $('#tableReturnBill');
            var noDataContainer = $('#noDataReturnBill');
            tbody.empty();
            if(response.length === 0) {
                noDataContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có sản phẩm nào!</p>
                    `);

                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            }else {
                noDataContainer.hide();
                tbody.closest('table').show(); // Ẩn table nếu không có dữ liệu
                response.forEach(function(billReturn, index) {
                    var imagesHtml = '';

                    billReturn.productDetail.product.images.forEach(function(image, imgIndex) {
                        imagesHtml += `
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" data-bs-interval="2000">
                                <img style="height: auto; width: 100px;" src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh">
                            </div>`;
                    });
                    tbody.append(`
                        <tr>
                            <th scope="row" class="text-center align-middle">${index + 1}</th>
                            <td class="text-center align-middle">
                                <div class="carousel slide" data-bs-ride="carousel">
                                    <div class="carousel-inner carousel-inner-bill-custom">
                                        ${imagesHtml}
                                    </div>
                                </div>
                            </td>
                            <td class="" style="max-height: 100px; overflow-y: auto; width: 150px;">
                                <div class="fs-4">
                                    ${billReturn.productDetail.product.nameProduct}
                                </div>
                                <div class="fs-6">
                                    Tên màu: ${billReturn.productDetail.color.nameColor}
                                    <br>
                                    Tên size: ${billReturn.productDetail.size.nameSize}
                                </div>
                            </td>

                            <td class="text-center align-middle">
                                ${billReturn.priceBuy.toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
                                ${billReturn.quantityReturn}
                            </td>
                            <td class="text-center align-middle">
                                ${billReturn.totalReturn.toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
                                <button type="button"
                                   class="btn btn-outline-danger btn-return-product-detail"
                                   >
                                    Xóa
                                </button>
                            </td>
                        </tr>`);
                });
                // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
                $('.carousel').each(function() {
                    $(this).carousel(); // Khởi tạo carousel cho từng phần tử
                });
            }
        },
        error: function (xhr) {
            console.error('loi' + xhr.responseText);
        }
    })
}

function maxPageReturnBill() {
    $.ajax({
        type: "GET",
        url:"/return-bill/max-page-return-bill",
        success: function (response) {
            createPagination('billReturnPageMax-returnBill', response, 1);
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }

    })
}

function addProductReturn() {
    $.ajax({
        type: "POST",
        url:"/return-bill/add-product-in-return-bill",
        contentType: "application/json",
        data: JSON.stringify({
            idProductDetail: parseInt($('#idProductDetail').val()),  // Chuyển thành số nguyên
            quantityReturn: parseInt($('#quantityReturnProduct').val()),  // Chuyển thành số nguyên
            priceBuy: parseFloat($('#priceBuyProduct').val())  // Chuyển thành số thực
        }),
        success: function (response) {
            loadBillDetailFromReturnBill(1);
            maxPageBillDetailByIdBill();
            loadReturnBill();
            maxPageReturnBill();
            showToast(response.message,response.check);
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }

    })
}

function ressetListReturnBill() {
    $.ajax({
        type: "GET",
        url:"/return-bill/reset-return-bill-detail",
        success: function (response) {
            loadBillDetailFromReturnBill(1);
            maxPageBillDetailByIdBill();
            loadReturnBill();
            maxPageReturnBill();
            loadInfomationReturnBill();
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function loadInfomationReturnBill() {
    $.ajax({
        type: "GET",
        url: "/return-bill/infomation-return-bill",
        success: function (response) {
            $('#code-bill').text(response.bill.codeBill)
            var cutomer = '';
            if (response.bill.customer == null) {
                cutomer = 'Khách lẻ'
            }else {
                cutomer = response.bill.customer.fullName
            }
            $('#customer-buy-product').text(cutomer)

            var discout = '';
            if(response.bill.voucher == null) {
                discout = '0 VNĐ';
            }else {
                discout = (response.bill.totalAmount - (response.bill.acountMoney+response.bill.cash)).toLocaleString('en-US') + ' VNĐ'
            }
            $('#discount-voucher').text(discout)

        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        }
    })
}

$(document).ready(function () {
    $('#form-return-product').submit(function (event) {
        event.preventDefault();
        addProductReturn();
    })
    ressetListReturnBill();
});