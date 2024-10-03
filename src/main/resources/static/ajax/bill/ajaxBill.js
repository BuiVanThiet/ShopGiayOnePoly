//hien thi bill detail
var pageBillDetail = 1;
function loadBillDetail(page)  {
    $.ajax({
        type: "GET",
        url: "/bill-api/bill-detail-by-id-bill/"+page,
        success: function(response) {
            pageBillDetail = page;

            var tbody = $('#tableBillDetail');
            var noDataContainer = $('#noDataContainer');
            tbody.empty(); // Xóa các dòng cũ
            var paymentCard = $('#paymentInformationCard');
            if (response.length === 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                noDataContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có sản phẩm nào!</p>
                    `);

                paymentCard.hide();
                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            } else {
                paymentCard.show();
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

                    var priceSaleAndRoot = '';

                    if(billDetail.priceRoot == billDetail.price) {
                        priceSaleAndRoot = `
                                <div>
                                    <span>${billDetail.price.toLocaleString('en-US')} VNĐ</span>
                                </div>`;
                    }else {
                        priceSaleAndRoot = `
                                <div>
                                    <span class="text-decoration-line-through">${billDetail.priceRoot.toLocaleString('en-US')} VNĐ</span>
                                    <br>
                                    <span class="text-danger fs-5">${billDetail.price.toLocaleString('en-US')} VNĐ</span>
                                </div>`;
                    }
                    var btnDeleteProduct = '';
                    if(billDetail.bill.status > 1) {
                        btnDeleteProduct = `
                       `;
                    }else {
                        btnDeleteProduct = `
                        <button onclick="deleteBillDetail(${billDetail.id})" class="btn btn-outline-danger"><i class="bi bi-x-lg"></i> Xóa bỏ</button>
                        `;
                    }
                    var btnBuyProduct = '';
                    if(billDetail.bill.status > 1) {
                        btnBuyProduct =`
                                    <div class="number" id="pageNumber">${billDetail.quantity}</div>
                        `
                    }else {
                        btnBuyProduct =`
                            <button class="button btn-decrement">-</button>
                                    <div class="number" id="pageNumber">${billDetail.quantity}</div>
                                    <button class="button btn-increment">+</button>
                        `;
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
                            <td class="">
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
                                   ${priceSaleAndRoot}
                            </td>
                            <td class="text-center align-middle">
                                <div class="pagination mb-3 custom-number-input" style="width: 130px;" data-id="${billDetail.id}">
                                    ${btnBuyProduct}
                               </div>
                            </td>
                            <td class="text-center align-middle">
                                ${billDetail.totalAmount.toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
<!--                                <a href="/bill/deleteBillDetail/${billDetail.id}" class="btn btn-outline-danger" id="deleteproduct"><i class="bi bi-x-lg"></i> Xóa bỏ</a>-->
                                ${btnDeleteProduct}
                            </td>
                        </tr>`);
                });
                if(cashClient != null) {
                    cashClient.value='';
                    formErorrCash.style.display = 'block';
                    erorrCash.innerText = 'Mời nhập đủ giá!';
                }

                if(payMethodChecked === 1 || payMethodChecked === 3) {
                    btnCreateBill.disabled = true;
                }

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
        url:"/bill-api/max-page-billdetail",
        success: function (response) {
            createPagination('billDetailPageMax', response, 1); // Phân trang 1
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

//Thong tin bill
function paymentInformation() {
    $.ajax({
        type: 'GET',
        url: '/bill-api/payment-information',
        success: function(response) {
            console.log(response)

            // Cập nhật thông tin vào các phần tử HTML
            $('#subTotal').text(response.totalAmount.toLocaleString('en-US') + ' VNĐ');
            $('#discountAmount').text(response.discount.toLocaleString('en-US') + ' VNĐ');
            $('#totalAmount').text((response.finalAmount+shipPrice).toLocaleString('en-US') + ' VNĐ');

            totalBill = response.finalAmount-response.discount;

            $('#notePayment').text(response.note);
            if(payMethodChecked === 2 || payMethodChecked === 3) {
                $('#cashAccount').val(response.finalAmount);
            }
            var totalCash = document.getElementById('totalCash');
            if(totalCash != null) {
                totalCash.value = response.finalAmount;
            }
            if (response.voucherId) {
                $('#voucherName').text(response.nameVoucher);
                $('#textVoucher').val(response.nameVoucher);
                $('#discountContainer').show();
            } else {
                $('#discountContainer').hide();
                $('#textVoucher').val('Không có');
            }

        },
        error: function(error) {
            console.error('Lỗi khi lấy thông tin thanh toán:', error);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(paymentInformation, 5000);  // Gửi lại sau 5 giây
        // }
    });
}


//thong tin khach hang trong he thong
function loadClientsIntoSelect() {
    $.ajax({
        type: "GET",
        url: "/bill-api/client",
        success: function (response) {
            const selectElement = document.getElementById("clientSelect");
            if(selectElement != null) {
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
                if (typeof OneSelectTag === 'function') {
                    // Kiểm tra nếu MultiSelectTag đã được khởi tạo
                    if (!selectElement.classList.contains('multi-select-initialized')) {
                        new OneSelectTag(selectElement.id, {
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
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadClientsIntoSelect, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

var payMethodUpLoad = 0;

function uploadPayMethod() {
    $.ajax({
        type:'POST',
        url:'/bill-api/uploadPaymentMethod',
        contentType: "application/json",
        data: JSON.stringify({ payMethod: payMethodUpLoad }),
        success: function (respon) {
            // loadBillNew();
            // // loadBillDetail();
            // loadProduct();
            // loadClientsIntoSelect();
            // paymentInformation();
            var idBill = document.getElementById('idBill').value;
            payMethodUpLoad = 0;
            var newUrl = 'http://localhost:8080/staff/bill/bill-detail/' + idBill;

            // Chuyển hướng đến URL mới
            window.location.href = newUrl;
        },
        error: function (xhr) {
            console.error('loi ne ' + xhr.responseText)
        }

    })
}
function loadBillNew() {
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
                        '<a class="nav-link text-dark ' + isActive + '" href="' + '/staff/bill/bill-detail/' + url.id + '">' + url.codeBill + '</a>' +
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
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadBillNew, 5000);  // Gửi lại sau 5 giây
        // }
    });
}
//tai san pham len giao dien
function loadProduct(pageNumber) {
    $.ajax({
        type: "GET",
        url: "/bill-api/productDetail-sell/" + pageNumber,
        success: function(response) {
            updateProductTable(response);
        },
        error: function(xhr) {
            console.error("Lỗi khi hiển thị chi tiết hóa đơn: " + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadProduct, 5000);  // Gửi lại sau 5 giây
        // }
    });
}
//cap nhat thong tin san pham
function updateProductTable(response) {
    var tbody = $('#tableProductDetailSell');
    var noDataContainer = $('#noDataProductDetail');
    var quantityModal = $('#modalQuantityProduct');
    tbody.empty(); // Xóa các dòng cũ

    if (response.length === 0) {
        // Nếu không có dữ liệu, hiển thị ảnh
        noDataContainer.html(`
                <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                     alt="Lỗi ảnh" style="width: auto; height: 100px;">
                     <p class="text-center">Không có sản phẩm nào!</p>
            `);
        noDataContainer.show();
        quantityModal.hide();
        tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
    } else {
        quantityModal.show();
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
            var priceSale ;
            var priceRoot = productDetail.price;

            if(productDetail.saleProduct == null) {
                priceSale =  productDetail.price;
                priceRoot = productDetail.price;
            }else {
                if(productDetail.saleProduct.discountType == 1) {
                    priceRoot =  productDetail.price;
                    priceSale = productDetail.price - (productDetail.price * (productDetail.saleProduct.discountValue/100));
                }else {
                    priceSale =  productDetail.price - productDetail.saleProduct.discountValue;
                    priceRoot = productDetail.price;
                }
            }

            var btn = '';
            if (productDetail.status === 2 || productDetail.product.status === 2) {
                btn = `<span class="text-danger">Mặt hàng đã ngừng bán</span>`;
            } else if (productDetail.quantity <= 0) {
                btn = `<span class="text-danger">Hết hàng</span>`;
            } else {
                btn = `
                        <button class="btn btn-outline-success" 
                            data-bs-target="#exampleQuantity" 
                            data-bs-toggle="modal"
                            data-name="${productDetail.product.nameProduct}" 
                            data-id="${productDetail.id}" 
                            data-quantity="${productDetail.quantity}"
                            data-price-sale="${priceSale}"
                            data-price-root="${priceRoot}"
                            >
                           <i class="bi bi-cart-plus"></i> Mua
                        </button>`;
            }

            var quantityProduct = '';
            if (productDetail.status === 2 || productDetail.product.status === 2) {
                quantityProduct = ``;
            } else {
                quantityProduct = `${productDetail.quantity}`;
            }

            var priceSaleAndRoot = '';

            // Kiểm tra xem có chương trình giảm giá không
            if (productDetail.saleProduct != null) {
                // Lấy giá gốc
                var originalPrice = productDetail.price;

                // Khởi tạo biến để lưu giá đã giảm
                var salePrice;

                // Kiểm tra loại giảm giá
                if (productDetail.saleProduct.discountType === 1) {
                    // Giảm theo phần trăm
                    var discountAmount = originalPrice * (productDetail.saleProduct.discountValue / 100);
                    salePrice = originalPrice - discountAmount;
                } else if (productDetail.saleProduct.discountType === 2) {
                    // Giảm theo số tiền cố định
                    salePrice = originalPrice - productDetail.saleProduct.discountValue;
                }

                // Gán giá đã giảm và giá gốc vào biến priceSaleAndRoot
                priceSaleAndRoot = `
                <div>
                    <span class="text-decoration-line-through">${originalPrice.toLocaleString('en-US')} VNĐ</span>
                    <br>
                    <span class="text-danger fs-5">${salePrice.toLocaleString('en-US')} VNĐ</span>
                </div>`;
            } else {
                // Nếu không có chương trình giảm giá, chỉ hiển thị giá gốc
                priceSaleAndRoot = `
                <div>
                    <span>${productDetail.price.toLocaleString('en-US')} VNĐ</span>
                </div>`;
            }

            tbody.append(`
                        <tr>
                <th scope="row" class="text-center align-middle">${index + 1}</th>
                <td class="text-center align-middle">
                    <div id="carouselExampleAutoplaying${index}" class="carousel slide" data-bs-ride="carousel">
                        <div class="carousel-inner" style="width: auto; height: 100px;">
                            ${imagesHtml}
                        </div>
                    </div>
                </td>
                <td class="">
                    <div class="fs-4">
                        ${productDetail.product.nameProduct}
                    </div>
                    <div class="fs-6">
                        Tên màu: ${productDetail.color.nameColor}
                        <br>
                        Tên size: ${productDetail.size.nameSize}
                    </div>
                </td>
                <td class="text-center align-middle">
                    ${quantityProduct}
                </td>
                <td class="text-center align-middle">
                   ${priceSaleAndRoot}
                </td>
                <td class="text-center align-middle">
                    ${btn}
                </td>
            </tr>
            `);
        });

        // Lắng nghe sự kiện khi mở modal
        $(document).on('click', '.btn-outline-success', function() {
            var nameProduct = $(this).data('name');
            var idProductDetail = $(this).data('id');
            var quantityProduct = $(this).data('quantity');
            var priceSale = $(this).data('price-sale');
            var priceRoot = $(this).data('price-root');
            // Gán các giá trị vào các thẻ hidden trong modal
            $('#nameProduct').val(nameProduct);
            $('#idProductDetail').val(idProductDetail);
            $('#quantityProduct').val(quantityProduct);
            $('#priceProductSale').val(priceSale);
            $('#priceProductRoot').val(priceRoot);
        });

        // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
        $('.carousel').each(function() {
            $(this).carousel(); // Khởi tạo carousel cho từng phần tử
        });
    }
}

//bo loc san pham
function filterProduct() {
    $.ajax({
        type: 'POST',
        url: '/bill-api/filter-product-deatail',  // Endpoint xử lý
        contentType: 'application/json',
        data: JSON.stringify({
            nameProduct: $('#nameSearch').val().trim(),
            idColor: parseInt($('#colorSearch').val().trim()) || null, // Sử dụng || null để xử lý trường hợp không có giá trị
            idSize: parseInt($('#sizeSearch').val().trim()) || null,
            idMaterial: parseInt($('#materialSearch').val().trim()) || null,
            idManufacturer: parseInt($('#manufacturerSearch').val().trim()) || null,
            idOrigin: parseInt($('#originSearch').val().trim()) || null,
            idCategories: $('#categorySearch').val().trim().split(',').map(Number)  // Chuyển đổi giá trị của danh mục
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

            // Gọi hàm MultiSelectTag sau khi đã nạp dữ liệu
            new MultiSelectTag('categores', {
                rounded: true,
                shadow: true,
                placeholder: 'Search',
                tagColor: {
                    textColor: '#327b2c',
                    borderColor: '#92e681',
                    bgColor: '#eaffe6',
                },
                onChange: function (values) {
                    let selectedValues = values.map(item => item.value).join(',');
                    document.getElementById('categorySearch').value = selectedValues;
                    console.log("Selected values: ", selectedValues);
                }
            });
            clearAllSelections()
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

function loadColoIntoSelect() {

}

function resetFilterProductSale() {
    document.getElementById('nameSearch').value='';
    document.getElementById('colorSearch').selectedIndex = 0;
    document.getElementById('sizeSearch').selectedIndex = 0;
    document.getElementById('materialSearch').selectedIndex = 0;
    document.getElementById('manufacturerSearch').selectedIndex = 0;
    document.getElementById('originSearch').selectedIndex = 0;
    document.getElementById('categores').selectedIndex = 0;
    clearAllSelections();
    filterProduct();
}
// document.getElementById('resetFilter').addEventListener('click', function () {
//     document.getElementById('nameSearch').value='';
//     document.getElementById('colorSearch').selectedIndex = 0;
//     document.getElementById('sizeSearch').selectedIndex = 0;
//     document.getElementById('materialSearch').selectedIndex = 0;
//     document.getElementById('manufacturerSearch').selectedIndex = 0;
//     document.getElementById('originSearch').selectedIndex = 0;
//     document.getElementById('categores').selectedIndex = 0;
//     clearAllSelections();
//     filterProduct();
// });


function getMaxPageProduct() {
    $.ajax({
        type: "GET",
        url: "/bill-api/page-max-product",
        success: function(response) {
            createPagination('productPageMax', response, 1); // Phân trang 1

        },
        error: function (xhr) {
            console.error('loi o tao page ' + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(getMaxPageProduct, 5000);  // Gửi lại sau 5 giây
        // }
    });
}


// Lấy tất cả các phần tử span.page-link



function updateQuantity(id, quantity,method) {
    $.ajax({
        type: "POST",
        url: "/bill-api/updateBillDetail", // URL API của bạn
        contentType: "application/json", // Chuyển thành JSON
        data: JSON.stringify({
            id: id,
            quantity: quantity,
            method: method
        }),
        success: function (response) {
            console.log('Cập nhật thành công: ' + response);
            showToast(response.message,response.check)
            loadBillNew(); // Tải lại danh sách bill mới
            loadBillDetail(pageBillDetail); // Tải lại chi tiết bill
            paymentInformation();
            loadProduct(1);
            checkUpdateCustomer = true;

            totalShip(provinceTransport,districtTransport,wardTransport);

            // loadBillStatusByBillId();
            // loadInformationBillByIdBill();
            // loadCustomerShipInBill();

            // updateMoneyShipWait(shipMoneyBillWait);

            // pageNumber();
        },
        error: function (xhr) {
            console.error('Lỗi khi cập nhật: ' + xhr.responseText);
        }
    });
}

function loadVoucherByBill(page) {
    $.ajax({
       type: "GET",
       url: "/bill-api/voucher/"+page,
        success: function (response) {
            var load = $('#loadVoucher');
            load.empty();
            response.forEach(function (voucher,index) {
                load.append(`
                     <div class="card mb-3 shadow-sm border-0">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <!-- Voucher Info -->
                                <div>
                                    <p class="fs-6 fw-bold mb-1 text-danger">Mã Voucher: ${voucher.codeVoucher}</p>
                                    <p class="fs-5 text-muted mb-0">Khuyến mãi: ${voucher.nameVoucher}</p>
                                </div>
                                
                                <!-- Button to Select Voucher -->
<!--                                <a href="/bill/click-voucher-bill/${voucher.id}" class="btn btn-outline-success btn-lg px-4">Chọn</a>-->
                                <button class="btn btn-outline-success btn-lg px-4" onclick="getAddVoucherInBill(${voucher.id})">Chọn</button>
                            </div>
                        </div>
                    </div>
                `);
            });
            maxPageVoucher();

        },
        error: function (xhr) {
            console.error('loi ne phan voucher ' + xhr.responseText)
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadVoucherByBill, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function resetSearchVoucher() {
    document.getElementById('textVoucherSearch').value='';
    console.log('da rết voucher')
    searchVoucher();
}
function searchVoucher() {
    $.ajax({
        type: "POST",
        url: "/bill-api/voucher-search",
        contentType: "application/json",
        data: JSON.stringify({ keyword: $('#textVoucherSearch').val().trim() }),  // Gửi dữ liệu dạng JSON
        success: function(response) {
            loadVoucherByBill(1);
            maxPageVoucher();

        },
        error: function(xhr) {
            console.error('Loi tim voucher ' + xhr.responseText);
        }
    });
}

function getAddVoucherInBill(idVoucher) {
    $.ajax({
        type: "POST",
        url: "/staff/bill/click-voucher-bill/"+idVoucher,
        success: function (response) {
            loadBillDetail(1);
            loadProduct(1)
            loadVoucherByBill(1);
            paymentInformation();
                // loadBillStatusByBillId();
            var checkFormStatus = document.getElementById('checkFormStatus');
            if(checkFormStatus != null) {
                   console.log('Phai vao duoc day')
                   loadInformationBillByIdBill();
                   loadCustomerShipInBill();
                $('#btn-Remove-voucher').hide();
               }

            showToast(response.message,response.check)
        },
        error: function (xhr) {
            console.error('loi' + xhr.responseText);
        }
    })
}

function getRemoveVoucherInBill() {
    $.ajax({
        type: "POST",
        url: "/staff/bill/delete-voucher-bill",
        success: function (response) {
            loadBillDetail(1);
            loadProduct(1)
            loadVoucherByBill(1);
            paymentInformation();
            var checkFormStatus = document.getElementById('checkFormStatus');
            if(checkFormStatus != null) {
                // loadBillStatusByBillId();
                loadInformationBillByIdBill();
                loadCustomerShipInBill();
                $('#btn-Remove-voucher').hide();
            }
            showToast(response.message,response.check)
        },
        error: function (xhr) {
            console.error('loi' + xhr.responseText);
        }
    })
}

function getUpdateTypeBill(type) {
    $.ajax({
        type: "POST",
        url: "/bill-api/update-bill-type",
        contentType: "application/json",
        data: JSON.stringify({ typeBill: type }),
        success: function (response) {
            console.log("done type bill")
        },
        error: function (xhr) {
            console.error('loi update bill type ' + xhr.responseText);
        }
    })
}

function maxPageVoucher() {
    $.ajax({
        type: "GET",
        url:"/bill-api/max-page-voucher",
        success: function (response) {
            createPagination('voucherPageMax', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

//ben trang quan ly hoa don
function getAllBilByStatus(value) {
    $.ajax({
        type: "GET",
        url: "/bill-api/manage-bill/"+value,
        success: function (response) {
            var tableBillManage = $('#billManage');
            var nodataBillManage = $('#nodataBillManage');
            if (response.length === 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                nodataBillManage.html(`
                <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                     alt="Lỗi ảnh" style="width: auto; height: 100px;">
                     <p class="text-center">Không có hóa đơn nào!</p>
            `);
                nodataBillManage.show();
                tableBillManage.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            } else {
                nodataBillManage.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                tableBillManage.closest('table').show();
                tableBillManage.empty()
                response.forEach(function (bill,index) {

                    const formattedDateTime = formatDateTime(bill.createDate);

                    tableBillManage.append(`
                     <tr>
                            <th scope="row">${index+1}</th>
                            <td>${bill.codeBill}</td>
                            <td>${bill.customer == null ? 'Khách lẻ' : bill.customer.fullName}</td>
                            <td>${bill.customer == null ? 'Không có' : bill.customer.numberPhone}</td> 
                            <td>${bill.finalAmount.toLocaleString('en-US') + ' VNĐ'}</td>
                            <td>${bill.billType == 1 ? 'Tại quầy' : 'Giao hàng'}</td>
                            <td>${formattedDateTime}</td>
                            <td>
                                <a href="/staff/bill/bill-status-index/${bill.id}" class="btn btn-primary">Xem chi tiết</a>
                            </td>
                        </tr>
                    `);
                })
            }
        },
        error: function (xhr) {
            console.error('Hien thi loi phan chon bill ' + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(getAllBilByStatus, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function getMaxPageBillManage() {
    $.ajax({
        type: "GET",
        url: "/bill-api/manage-bill-max-page",
        success: function (response) {
            createPagination('billManagePageMax', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi max page bill manage' + xhr.responseText)
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(getMaxPageBillManage, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function clickStatusBillManager(status) {
    $.ajax({
        type: "GET",
        url: "/bill-api/status-bill-manage/"+status,
        success: function (response) {
            getMaxPageBillManage();
            getAllBilByStatus(1);
        },
        error: function (xhr) {
            console.error('loi chon trang thai ' + xhr.responseText);
        }
    })
}

function searchBillManage() {
    $.ajax({
        type: "POST",
        url: "/bill-api/bill-manage-search",
        contentType: "application/json",
        data: JSON.stringify({ keywordBill: $('#keywordBillManage').val().trim() }),  // Gửi dữ liệu dạng JSON
        success: function(response) {
            getMaxPageBillManage();
            getAllBilByStatus(1);
            // clickStatusBillManager(999);
        },
        error: function(xhr) {
            console.error('Loi tim voucher ' + xhr.responseText);
        }
    });
}
function resetFillterBillManage() {
    document.getElementById('keywordBillManage').value='';
    searchBillManage()
}
// document.getElementById('resetFilterBillManage').addEventListener('click', function () {
//     document.getElementById('keywordBillManage').value='';
//     searchBillManage()
// });

    function updateMethodPay(method) {
        if(method === 1) {
                payMethodUpLoad = 1;
                uploadPayMethod()
        }else if (method === 2) {
                payMethodUpLoad = 2;
                uploadPayMethod()
        }else {
                payMethodUpLoad = 3;
                uploadPayMethod()
        }

    }

function getBuyProduct() {
    let quantity = $("#quantity").val();
    let idProductDetail = $("#idProductDetail").val();
    let priceProductSale = $("#priceProductSale").val();
    let priceProductRoot = $("#priceProductRoot").val();
    $.ajax({
        url: "/staff/bill/buy-product-detail",
        type: "POST",
        data: {
            quantityDetail: quantity, // Dữ liệu số lượng
            idProductDetail: idProductDetail, // ID sản phẩm
            priceProductSale: priceProductSale,
            priceProductRoot: priceProductRoot
        },
        success: function (response) {
                loadBillNew();
                loadBillDetail(pageBillDetail);
                loadProduct(1)
                paymentInformation();
                getMaxPageProduct();
                loadVoucherByBill(1);
                maxPageBillDetailByIdBill();

                // loadBillStatusByBillId();
                // loadInformationBillByIdBill();
                // loadCustomerShipInBill();
            checkUpdateCustomer = true;

            totalShip(provinceTransport,districtTransport,wardTransport);

            // updateMoneyShipWait(shipMoneyBillWait);

            showToast(response.message,response.check)

            $('#quantity').val('');
            $('#quantityProduct').val('0');
            $('#idProductDetail').val('0');
            $('#priceProductSale').val('0');
            $('#priceProductRoot').val('0');
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        }
    })
}

function resetHidenProductSale() {
    $('#quantity').val('');
    $('#quantity').val('');
    $('#quantityProduct').val('0');
    $('#idProductDetail').val('0');
    $('#priceProductSale').val('0');
    $('#priceProductRoot').val('0');
}
function deleteBillDetail(id) {
    $.ajax({
        url: "/staff/bill/deleteBillDetail/"+id,
        type: "GET",
        success: function (response) {
            loadBillDetail(pageBillDetail);
            loadProduct(1)
            paymentInformation();
            getMaxPageProduct();
            loadVoucherByBill(1);
            maxPageBillDetailByIdBill();

            checkUpdateCustomer = true;

            totalShip(provinceTransport,districtTransport,wardTransport);


            showToast(response.message,response.check)

        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        }
    })
}

//xuat hoa don
function createBillPDF() {
    var idBill = parseInt($('#idBillCreatePDF').val());
    $.ajax({
        type: "GET",
        url: "/bill-api/bill-pdf/"+idBill,
        xhrFields: {
            responseType: 'blob'  // Nhận PDF dưới dạng blob
        },
        success: function (response) {
            // Tạo URL cho blob PDF
            const pdfUrl = URL.createObjectURL(response);

            // Mở tab mới và hiển thị PDF
            const newTab = window.open();
            newTab.document.write(`<iframe src="${pdfUrl}" width="100%" height="100%" style="border:none;"></iframe>`);

        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseType);
        }
    })
}

$(document).ready(function () {
    $('#formBuyProduct').submit(function (event) {
        event.preventDefault();
        getBuyProduct();
    })
    $('#formFilterProduct').submit(function (event) {
        event.preventDefault();
        filterProduct();
    })
    $('#formSearchVoucher').submit(function (event) {
        event.preventDefault();
        searchVoucher()
    })
    $('#formSubmitFilterBill').submit(function (event) {
        event.preventDefault();
        searchBillManage();
    })
    // Xử lý sự kiện tăng/giảm số lượng
    $(document).on('click', '.btn-increment', function () {
        var $numberDiv = $(this).siblings('.number');
        var value = parseInt($numberDiv.text(), 10);
        $numberDiv.text(value + 1);
        // Cập nhật giá trị mới trên server
        updateQuantity($(this).closest('.custom-number-input').data('id'), $numberDiv.text(),'cong');
    });

    $(document).on('click', '.btn-decrement', function () {
        var $numberDiv = $(this).siblings('.number');
        var value = parseInt($numberDiv.text(), 10);
        if (value > 0) {
            $numberDiv.text(value - 1);
            // Cập nhật giá trị mới trên server
            updateQuantity($(this).closest('.custom-number-input').data('id'), $numberDiv.text(),'tru');
        }
    });

    // Hàm cập nhật số lượng lên server
        // Gọi các hàm tải dữ liệu ban đầu
        loadBillNew();
        loadClientsIntoSelect();
        paymentInformation();
        loadCategoryIntoSelect()
        // pageNumber();
        clickStatusBillManager(999);

    loadProduct(1)
    loadBillDetail(pageBillDetail);
    getMaxPageProduct();
    loadVoucherByBill(1);
    maxPageBillDetailByIdBill();
    getAllBilByStatus(1);
    getMaxPageBillManage();
});
