var pageReturn = 1;
function loadBillDetailFromReturnBill(page) {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/bill-detail/"+page,
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
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" data-bs-interval="10000">
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
                                <div class="carousel slide d-flex justify-content-center align-items-center" data-bs-ride="carousel">
                                    <div style="width: 150px;" class="carousel-inner carousel-inner-bill-custom">
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
                                   ${Math.trunc(billDetail.price).toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
                                ${billDetail.quantity}
                            </td>
                            <td class="text-center align-middle">
                                ${Math.trunc(billDetail.totalAmount).toLocaleString('en-US') + ' VNĐ'}
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
        url:"/return-exchange-bill-api/max-page-bill-detail",
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

function loadReturnBill(page) {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/bill-return-detail/"+page,
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
                document.getElementById('form_Reason').style.display = 'none';
            }else {
                document.getElementById('form_Reason').style.display = 'block';
                noDataContainer.hide();
                tbody.closest('table').show(); // Ẩn table nếu không có dữ liệu
                response.forEach(function(billReturn, index) {
                    var imagesHtml = '';

                    billReturn.productDetail.product.images.forEach(function(image, imgIndex) {
                        imagesHtml += `
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" data-bs-interval="10000">
                                <img style="height: auto; width: 100px;" src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh">
                            </div>`;
                    });
                    tbody.append(`
                        <tr>
                            <th scope="row" class="text-center align-middle">${index + 1}</th>
                            <td class="text-center align-middle">
                                <div class="carousel slide d-flex justify-content-center align-items-center" data-bs-ride="carousel">
                                    <div style="width: 150px;" class="carousel-inner carousel-inner-bill-custom">
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
                                <span class="d-inline-block" tabindex="0" 
                                data-bs-toggle="popover" 
                                data-bs-trigger="hover focus" 
                                data-bs-content="${billReturn.priceDiscount.toLocaleString('en-US') + ' VNĐ'}">
                                    ${Math.trunc(billReturn.priceBuy).toLocaleString('en-US') + ' VNĐ'}
                                </span>
                            </td>
                            <td class="text-center align-middle">
                                 <div class="pagination mb-3 custom-number-input" style="width: 130px;" data-id="${billReturn.productDetail.id}">
                                        <button class="button btn-decrement">-</button>
                                    <div class="number" id="pageNumber"> ${billReturn.quantityReturn}</div>
                                    <button class="button btn-increment">+</button>
                                 </div>
                            </td>
                            <td class="text-center align-middle">
                                ${Math.trunc(billReturn.totalReturn).toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
                                <button type="button"
                                   class="btn btn-outline-danger btn-return-product-detail"
                                   onclick="remoBillReturn(${billReturn.productDetail.id},${billReturn.quantityReturn})"
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
                // Khởi tạo lại tất cả các popover sau khi cập nhật DOM
                $(function () {
                    $('[data-bs-toggle="popover"]').popover();
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
        url:"/return-exchange-bill-api/max-page-return-bill",
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
        url:"/return-exchange-bill-api/add-product-in-return-bill",
        contentType: "application/json",
        data: JSON.stringify({
            idProductDetail: parseInt($('#idProductDetail').val()),  // Chuyển thành số nguyên
            quantityReturn: parseInt($('#quantityReturnProduct').val()),  // Chuyển thành số nguyên
            priceBuy: parseFloat($('#priceBuyProduct').val())  // Chuyển thành số thực
        }),
        success: function (response) {
            loadBillDetailFromReturnBill(1);
            maxPageBillDetailByIdBill();
            pageReturn = 1;
            loadReturnBill(pageReturn);
            maxPageReturnBill();
            loadInfomationReturnBill()
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
        url:"/return-exchange-bill-api/reset-return-bill-detail",
        success: function (response) {
            loadBillDetailFromReturnBill(1);
            maxPageBillDetailByIdBill();
            loadReturnBill(pageReturn);
            maxPageReturnBill();
            loadInfomationReturnBill();
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function loadInfomationReturnBill() {
    $('#span-exchangeAndReturnFee').remove();
    $('#span-discountedAmount').remove();
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/infomation-return-bill",
        success: function (response) {
            $('#code-bill').text(response.codeBill)
            $('#customer-buy-product').text(response.nameCustomer)
            $('#discount-voucher').text(Math.trunc(response.discount).toLocaleString('en-US') + ' VNĐ')
            $('#divide-equally-product').text(Math.trunc(response.discountRatioPercentage)+ ' %')
            $('#total-return').text(Math.trunc(response.totalReturn).toLocaleString('en-US') + ' VNĐ')
            $('#total-exchange').text(Math.trunc(response.totalExchange).toLocaleString('en-US') + ' VNĐ')
            var totalReturnCustomer = 0;
            if(((response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount)-response.totalExchange) <= 0) {
                totalReturnCustomer = 0;
            }else {
                totalReturnCustomer = (response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount)-response.totalExchange;
            }
            $('#total-return-customer').text(Math.trunc(totalReturnCustomer).toLocaleString('en-US') + ' VNĐ')
            var totalExchangeCustomer = 0;
            if((response.totalExchange-(response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount)) <= 0) {
                totalExchangeCustomer = 0;
            }else {
                totalExchangeCustomer = response.totalExchange-(response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount);
            }
            $('#total-exchange-customer').text(Math.trunc(totalExchangeCustomer).toLocaleString('en-US') + ' VNĐ')
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        }
    })
}

function remoBillReturn(idProductDetail,quantity) {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/remove-product-in-return-bill/"+idProductDetail+"/"+quantity,
        success: function (response) {
            loadBillDetailFromReturnBill(1);
            maxPageBillDetailByIdBill();
            pageReturn = 1;
            loadReturnBill(pageReturn);
            maxPageReturnBill();
            loadInfomationReturnBill()
            loadProduct(1);
            getMaxPageProduct();
            loadExchangeBill(pageExchange);
            maxPageExchangeBill();
            showToast(response.message,response.check);
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function increaseOrDecreaseProductReturn(idProductReturn,quantity,method) {
    console.log(idProductReturn)
    console.log(quantity)
    console.log(method)
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/increase-or-decrease-product-return/"+parseInt(idProductReturn)+"/"+parseInt(quantity)+"/"+method, // URL
        success: function (response) {
            loadBillDetailFromReturnBill(1);
            maxPageBillDetailByIdBill();
            loadReturnBill(pageReturn);
            // maxPageReturnBill();
            loadInfomationReturnBill()
            showToast(response.message,response.check);
        },
        error: function (xhr) {
            console.error('Lỗi khi cập nhật: ' + xhr.responseText);
        }
    });
}

function createReturnBill() {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/create-return-bill/"+$('#node-return').val(),
        success: function (response) {
            // Kiểm tra nếu phản hồi có URL chuyển hướng
            if (response.redirectUrl) {
                // Chuyển hướng đến URL được chỉ định trong phản hồi
                window.location.href = response.redirectUrl;
            } else {
                // Xử lý nếu không có chuyển hướng (ví dụ hiển thị thông báo)
                alert("Không có URL chuyển hướng");
            }
        },
        error: function (xhr) {
            console.error('Lỗi khi cập nhật: ' + xhr.responseText);
        }
    });
}

function exchangeAndReturnFee(input) {
    let value = input.value.replace(/,/g, '').replace(/[^0-9]/g, '');
    input.value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    if(value === '') {
        value = 0;
    }
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/exchangeAndReturnFee/"+value,
        success: function (response) {
            loadInfomationReturnBill();
        },
        error: function (xhr) {
            console.error('Lỗi khi cập nhật: ' + xhr.responseText);
        }
    });
}

function discountedAmount(input) {
    let value = input.value.replace(/,/g, '').replace(/[^0-9]/g, '');
    input.value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    if(value === '') {
        value = 0;
    }
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/discountedAmount/"+value,
        success: function (response) {
            loadInfomationReturnBill();
        },
        error: function (xhr) {
            console.error('Lỗi khi cập nhật: ' + xhr.responseText);
        }
    });
}


$(document).ready(function () {
    // Xử lý sự kiện tăng/giảm số lượng
    $(document).on('click', '.btn-increment', function () {
        var $numberDiv = $(this).siblings('.number');
        var value = parseInt($numberDiv.text(), 10);
        $numberDiv.text(value + 1);
        // Cập nhật giá trị mới trên server
        increaseOrDecreaseProductReturn($(this).closest('.custom-number-input').data('id'),1,'cong')
    });

    $(document).on('click', '.btn-decrement', function () {
        var $numberDiv = $(this).siblings('.number');
        var value = parseInt($numberDiv.text(), 10);
        if (value > 0) {
            $numberDiv.text(value - 1);
            // Cập nhật giá trị mới trên server
            increaseOrDecreaseProductReturn($(this).closest('.custom-number-input').data('id'),1,'tru')
        }
    });

    $('#form-return-product').submit(function (event) {
        event.preventDefault();
        addProductReturn();
    })

    ressetListReturnBill();
});