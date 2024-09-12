function updatePaymentInformation() {
    $.ajax({
        type: 'GET',
        url: '/bill-api/payment-information',
        success: function(response) {
            console.log(response)
            // Cập nhật thông tin vào các phần tử HTML
            $('#subTotal').text(response.totalAmount + ' VNĐ');
            $('#discountAmount').text(response.discount + ' VNĐ');
            $('#totalAmount').text(response.finalAmount + ' VNĐ');

            if (response.voucherId) {
                $('#voucherName').text(response.nameVoucher);
                $('#textVoucher').text(response.nameVoucher);
                $('#discountContainer').show();
            } else {
                $('#discountContainer').hide();
            }
        },
        error: function(error) {
            console.error('Lỗi khi lấy thông tin thanh toán:', error);
        }
    });
}



function loadClientsIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/client",
        success: function (response) {
            const selectElement = document.getElementById("clientSelect");

            // Xóa tất cả tùy chọn hiện tại
            selectElement.innerHTML = "";

            // Tạo các thẻ <option> mới từ dữ liệu nhận được
            response.forEach(item => {
                const option = document.createElement("option");
                option.value = item.id; // Thay đổi theo cấu trúc dữ liệu nhận được
                option.textContent = item.fullName + ' - ' + item.numberPhone; // Thay đổi theo cấu trúc dữ liệu nhận được
                selectElement.appendChild(option);
            });

            // Khởi tạo MultiSelectTag sau khi dữ liệu đã được thêm vào
            if (typeof MultiSelectTag === 'function') {
                // Kiểm tra nếu MultiSelectTag đã được khởi tạo
                if (!selectElement.classList.contains('multi-select-initialized')) {
                    new MultiSelectTag(selectElement.id, {
                        rounded: true,
                        shadow: true,
                        placeholder: 'Search',
                        tagColor: {
                            textColor: '#327b2c',
                            borderColor: '#92e681',
                            bgColor: '#eaffe6',
                        },
                        onChange: function (values) {
                            console.log(`${selectElement.id} selected values:`, values);  // Log ra ID của dropdown và các giá trị đã chọn
                        }
                    });
                    selectElement.classList.add('multi-select-initialized');
                }
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        }
    });
}



function loadProduct() {
    $.ajax({
        type: "GET",
        url: "/bill-api/productDetail-sell",
        success: function(response) {
            console.log("Dữ liệu trả về:", response); // Kiểm tra dữ liệu trả về
            var tbody = $('#tableProductDetailSell');
            var noDataContainer = $('#noDataProductDetail');
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

                    productDetail.product.images.forEach(function(image, imgIndex) {
                        imagesHtml += `
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" style="width: auto; height: 100px;">
                                <img src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh" style="width: auto; height: 100px;">
                            </div>`;
                    });
                    var btn = '';
                    if (productDetail.status === 0 || productDetail.product.status === 0) {
                        btn = `<span class="text-danger">Mặt hàng đã ngừng bán</span>`;
                    } else {
                        btn = `<a href="#" class="btn btn-success">Mua</a>`;
                    }
                    var quantityProduct = '';
                    if (productDetail.status === 0 || productDetail.product.status === 0) {
                        quantityProduct = ``;
                    } else {
                        quantityProduct = `${productDetail.quantity}`;
                    }
                    tbody.append(`
                        <tr>
                            <th scope="row">${index + 1}</th>
                            <td>
                                <div id="carouselExampleAutoplaying${index}" class="carousel slide" data-bs-ride="carousel">
                                    <div class="carousel-inner" style="width: auto; height: 100px;">
                                        ${imagesHtml}
                                    </div>
                                </div>
                            </td>
                            <td>
                                ${productDetail.product.nameProduct}
                                <div class="fs-6">
                                Tên màu: ${productDetail.color.nameColor}
                                <br>
                                Tên size: ${productDetail.size.nameSize}
                                </div>

                            </td>
                            <td>
                                ${quantityProduct}
                            </td>
                            <td>${productDetail.price}</td>
                            <td>
                                ${btn}
                            </td>
                        </tr>`);
                });

                // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
                $('.carousel').each(function() {
                    $(this).carousel(); // Khởi tạo carousel cho từng phần tử
                });
            }
        },
        error: function(xhr) {
            console.error("Lỗi khi hiển thị chi tiết hóa đơn: " + xhr.responseText);
        }
    });
}


$(document).ready(function () {
    window.loadBillNew = function () {
        var idBill = $('#idBill').val();
        $.ajax({
            type: "GET",
            url: "/bill-api/all-new",
            success: function (response) {
                var ul = $('#billBody');
                var noDataBill = $('#noDataBill');
                ul.empty();

                if (response && response.length > 0) {
                    noDataBill.hide()
                    // Có dữ liệu, hiển thị danh sách bill
                    response.forEach(function (url) {
                        var isActive = (url.id == idBill) ? 'active' : ''; // So sánh với idBill
                        ul.append(
                            '<li class="nav-item">' +
                            '<a class="nav-link text-dark ' + isActive + '" href="' + '/bill/bill-detail/' + url.id + '">' + url.codeBill + '</a>' +
                            '</li>'
                        );
                    });
                }else {
                    // Không có dữ liệu, hiển thị thông báo và hình ảnh
                    noDataBill.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có hóa đơn nào!</p>
                    `);
                    noDataBill.show()
                }
            },
            error: function (xhr) {
                console.error("Lỗi hiển thị bill: " + xhr.responseText);
            }
        });
    }

    window.loadBillDetail = function () {
        $.ajax({
            type: "GET",
            url: "/bill-api/bill-detail-by-id-bill",
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
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" style="width: auto; height: 100px;">
                                <img src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh" style="width: auto; height: 100px;">
                            </div>`;
                        });

                        tbody.append(`
                        <tr>
                            <th scope="row">${index + 1}</th>
                            <td>
                                <div id="carouselExampleAutoplaying${index}" class="carousel slide" data-bs-ride="carousel">
                                    <div class="carousel-inner" style="width: auto; height: 100px;">
                                        ${imagesHtml}
                                    </div>
                                </div>
                            </td>
                            <td>${billDetail.productDetail.product.nameProduct}</td>
                            <td>${billDetail.price}</td>
                            <td>
                                <div class="pagination mb-3 custom-number-input" style="width: 130px;" data-id="${billDetail.id}">
                                    <button class="button btn-decrement">-</button>
                                    <div class="number" id="pageNumber">${billDetail.quantity}</div>
                                    <button class="button btn-increment">+</button>
                               </div>
                            </td>
                            <td>${billDetail.totalAmount}</td>
                            <td>
                                <a href="/bill-api/deleteBillDetail/${billDetail.id}" class="btn btn-danger">Xóa bỏ</a>
                            </td>
                        </tr>`);
                    });

                    // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
                    $('.carousel').each(function() {
                        $(this).carousel(); // Khởi tạo carousel cho từng phần tử
                    });
                }
            },
            error: function(xhr) {
                console.error("Lỗi khi hiển thị chi tiết hóa đơn: " + xhr.responseText);
            }
        });
    }

    // window.pageNumber = function () {
    //     $.ajax({
    //         type: "GET",
    //         url: "/bill-api/page",
    //         success: function (response) {
    //             var ul = $('#pageNumber');
    //             ul.empty();
    //             for (var i = 1; i <= response; i++) {
    //                 ul.append(`
    //                 <li class="page-item"><a class="page-link" href="/bill-api/bill-detail-page/${i}">${i}</a></li>
    //             `);
    //             }
    //         },
    //         error: function (xhr) {
    //             console.error("Lỗi hiển thị trang: " + xhr.responseText);
    //         }
    //     });
    // }




    // Xử lý sự kiện tăng/giảm số lượng
    $(document).on('click', '.btn-increment', function () {
        var $numberDiv = $(this).siblings('.number');
        var value = parseInt($numberDiv.text(), 10);
        $numberDiv.text(value + 1);

        // Cập nhật giá trị mới trên server
        updateQuantity($(this).closest('.custom-number-input').data('id'), $numberDiv.text());
    });

    $(document).on('click', '.btn-decrement', function () {
        var $numberDiv = $(this).siblings('.number');
        var value = parseInt($numberDiv.text(), 10);
        if (value > 0) {
            $numberDiv.text(value - 1);

            // Cập nhật giá trị mới trên server
            updateQuantity($(this).closest('.custom-number-input').data('id'), $numberDiv.text());
        }
    });

    // Hàm cập nhật số lượng lên server
    function updateQuantity(id, quantity) {
        $.ajax({
            type: "POST",
            url: "/bill-api/updateBillDetail", // URL API của bạn
            contentType: "application/json", // Chuyển thành JSON
            data: JSON.stringify({
                id: id,
                quantity: quantity
            }),
            success: function (response) {
                console.log('Cập nhật thành công: ' + response);
                showToast(response.message,response.check)
                loadBillNew(); // Tải lại danh sách bill mới
                loadBillDetail(); // Tải lại chi tiết bill
                loadProduct();
                updatePaymentInformation();
                // pageNumber();
            },
            error: function (xhr) {
                console.error('Lỗi khi cập nhật: ' + xhr.responseText);
            }
        });
    }

    // Gọi các hàm tải dữ liệu ban đầu
    loadBillNew();
    loadBillDetail();
    loadProduct();
    loadClientsIntoSelect();
    updatePaymentInformation();
    // pageNumber();

});
