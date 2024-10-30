//hien thi bill detail
var pageBillDetail = 1;
var selectSole;
var selectColor ;
var selectSize ;
var selectMaterial;
var selectManufacturer;
var selectOrigin;
var selectCategory;
function loadBillDetail(page)  {
    totalWeight = 0;
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
                    totalWeight += billDetail.productDetail.weight * billDetail.quantity;
                    console.log('can nang cua san pham la ' + totalWeight)

                    billDetail.productDetail.product.images.forEach(function(image, imgIndex) {
                        imagesHtml += `
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" data-bs-interval="30000">
                                <img style="height: auto; width: 100px;" src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh">
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
                    if(billDetail.bill.status == 2 || billDetail.bill.status == 3 || billDetail.bill.status == 4 ||  billDetail.bill.status == 6 ||  billDetail.bill.status == 7) {
                        btnDeleteProduct = `
                       `;
                    }else if (billDetail.bill.status == 1 || billDetail.bill.status == 0){
                        btnDeleteProduct = `
                                <td class="text-center align-middle">
                                    <button onclick="deleteBillDetail(${billDetail.id})" class="btn btn-outline-danger"><i class="bi bi-x-lg"></i> Xóa bỏ</button>
                                </td>
                        `;
                    }else if(billDetail.bill.status == 5) {
                        btnDeleteProduct = `
                                <button class="btn btn-outline-danger"
                                    data-bs-target="#returnQuantity"
                                    data-bs-toggle="modal"
                                    data-name=""
                                    data-id=""
                                    data-quantity=""
                                    data-price-sale=""
                                    data-price-root=""
                                    onclick="">
                                   <i class="bi bi-arrow-counterclockwise"></i>Trả hàng
                                </button>
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
                    // Kiểm tra nếu số lượng mua lớn hơn số lượng tồn kho
                    var rowClass = '';
                    if (billDetail.quantity > billDetail.productDetail.quantity && billDetail.bill.status == 1) {
                        rowClass = 'table-danger';  // Thêm class CSS để làm nổi bật dòng
                        checkQuantityOrder = false;
                    }else {
                        checkQuantityOrder = true;
                    }



                    tbody.append(`
                        <tr class="${rowClass}">
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
                                    ${btnDeleteProduct} 
                               
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
            $('#priceDiscount').val(response.discount);

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
        // POST must be contentType  data: JSON.stringify({ payMethod: payMethodUpLoad }),
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
    var url = '/bill-api/all-new';
    var idBill = $('#idBill').val();
    $.ajax({
        type: "GET",
        url: url,
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
        tbody.hide(); // Ẩn table nếu không có dữ liệu
    } else {
        quantityModal.show();
        noDataContainer.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
        tbody.show(); // Hiển thị lại table nếu có dữ liệu
        response.forEach(function(productDetail, index) {
            var imagesHtml = '';

            // productDetail.product.images.forEach(function(image, imgIndex) {
            //     imagesHtml += `
            //         <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" style="width: auto; height: 100px;">
            //             <img src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh" style="width: auto; height: 100px;">
            //         </div>`;
            // });
            // var priceSale ;
            // var priceRoot = productDetail.price;

            // if(productDetail.saleProduct == null) {
            //     priceSale =  productDetail.price;
            //     priceRoot = productDetail.price;
            // }else {
            //     if(productDetail.saleProduct.discountType == 1) {
            //         priceRoot =  productDetail.price;
            //         priceSale = productDetail.price - (productDetail.price * (productDetail.saleProduct.discountValue/100));
            //     }else {
            //         priceSale =  productDetail.price - productDetail.saleProduct.discountValue;
            //         priceRoot = productDetail.price;
            //     }
            // }

            var btn = '';
            // if (productDetail.status === 2 || productDetail.product.status === 2) {
            //     btn = `<span class="text-danger">Mặt hàng đã ngừng bán</span>`;
            // } else if (productDetail.quantity <= 0) {
            //     btn = `<span class="text-danger">Hết hàng</span>`;
            // } else {
            //     btn = `
            //             <button class="btn btn-outline-success"
            //                 data-bs-target="#exampleQuantity"
            //                 data-bs-toggle="modal"
            //                 data-name="${productDetail.product.nameProduct}"
            //                 data-id="${productDetail.id}"
            //                 data-quantity="${productDetail.quantity}"
            //                 data-price-sale="${priceSale}"
            //                 data-price-root="${priceRoot}"
            //                 >
            //                <i class="bi bi-cart-plus"></i> Mua
            //             </button>`;
            // }

            var quantityProduct = '';
            // if (productDetail.status === 2 || productDetail.product.status === 2) {
            //     quantityProduct = ``;
            // } else {
            //     quantityProduct = `${productDetail.quantity}`;
            // }

            var priceSaleAndRoot = '';

            // Kiểm tra xem có chương trình giảm giá không
            // if (productDetail.saleProduct != null) {
            //     // Lấy giá gốc
            //     var originalPrice = productDetail.price;
            //
            //     // Khởi tạo biến để lưu giá đã giảm
            //     var salePrice;
            //
            //     // Kiểm tra loại giảm giá
            //     if (productDetail.saleProduct.discountType === 1) {
            //         // Giảm theo phần trăm
            //         var discountAmount = originalPrice * (productDetail.saleProduct.discountValue / 100);
            //         salePrice = originalPrice - discountAmount;
            //     } else if (productDetail.saleProduct.discountType === 2) {
            //         // Giảm theo số tiền cố định
            //         salePrice = originalPrice - productDetail.saleProduct.discountValue;
            //     }
            //
            //     // Gán giá đã giảm và giá gốc vào biến priceSaleAndRoot
            //     priceSaleAndRoot = `
            //     <div>
            //         <span class="text-decoration-line-through">${originalPrice.toLocaleString('en-US')} VNĐ</span>
            //         <br>
            //         <span class="text-danger fs-5">${salePrice.toLocaleString('en-US')} VNĐ</span>
            //     </div>`;
            // } else {
            //     // Nếu không có chương trình giảm giá, chỉ hiển thị giá gốc
            //     priceSaleAndRoot = `
            //     <div>
            //         <span>${productDetail.price.toLocaleString('en-US')} VNĐ</span>
            //     </div>`;
            // }

            // tbody.append(`
            //             <tr>
            //     <th scope="row" class="text-center align-middle">${index + 1}</th>
            //     <td class="text-center align-middle">
            //         <div id="carouselExampleAutoplaying${index}" class="carousel slide" data-bs-ride="carousel">
            //             <div class="carousel-inner" style="width: auto; height: 100px;">
            //                 ${imagesHtml}
            //             </div>
            //         </div>
            //     </td>
            //     <td class="">
            //         <div class="fs-4">
            //             ${productDetail.product.nameProduct}
            //         </div>
            //         <div class="fs-6">
            //             Tên màu: ${productDetail.color.nameColor}
            //             <br>
            //             Tên size: ${productDetail.size.nameSize}
            //         </div>
            //     </td>
            //     <td class="text-center align-middle">
            //         ${quantityProduct}
            //     </td>
            //     <td class="text-center align-middle">
            //        ${priceSaleAndRoot}
            //     </td>
            //     <td class="text-center align-middle">
            //         ${btn}
            //     </td>
            // </tr>
            // `);

            // kiem tra giam gia
            if (productDetail[15] != 'Không giảm') {
                // Gán giá đã giảm và giá gốc vào biến priceSaleAndRoot
                priceSaleAndRoot = `
                <div>
                    <span class="text-decoration-line-through fs-6">${productDetail[9].toLocaleString('en-US')} VNĐ</span>
                    <br>
                    <span class="text-danger fs-5">${productDetail[12].toLocaleString('en-US')} VNĐ</span>
                </div>`;
            } else {
                // Nếu không có chương trình giảm giá, chỉ hiển thị giá gốc
                priceSaleAndRoot = `
                <div>
                    <span class="fs-5">${productDetail[9].toLocaleString('en-US')} VNĐ</span>
                </div>`;
            }

            //setUpBTN
            if (productDetail[13] === 2 || productDetail[14] === 2) {
                btn = `<span class="text-danger">Mặt hàng đã ngừng bán</span>`;
            } else if (productDetail[11] <= 0) {
                btn = `<span class="text-danger">Hết hàng</span>`;
            } else {
                btn = `
                            <button class="btn btn-outline-success btn-buy-product-detail"
                                data-bs-target="#exampleQuantity"
                                data-bs-toggle="modal"
                                data-name="${productDetail[2]}"
                                data-id="${productDetail[0]}"
                                data-quantity="${productDetail[11]}"
                                data-price-sale="${productDetail[9]}"
                                data-price-root="${productDetail[12]}"
                                onclick="resetHidenProductSale()">
                               <i class="bi bi-cart-plus"></i> Mua
                            </button>`;
            }

            //lấy ảnh sản phẩm
            var nameImage = productDetail[17].split(',');
            nameImage.forEach(function (imageProduct,indexImage) {
                imagesHtml += `
                      <div  data-bs-interval="30000" class="carousel-item ${indexImage === 0 ? 'active' : ''}">
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

            // tbody.append(`
            //             <tr>
            //     <th scope="row" class="text-center align-middle">${index + 1}</th>
            //     <td class="text-center align-middle" style="vertical-align: middle;">
            //         <div class="carousel-container-bill-custom ">
            //         ${saleBadge}
            //         <div id="carouselExampleAutoplaying${index}" class="carousel carousel-bill-custom slide" data-bs-ride="carousel">
            //             <div class="carousel-inner-bill-custom" style="height: auto; width: 250px;">
            //                 ${imagesHtml}
            //             </div>
            //         </div>
            //         </div>
            //     </td>
            //     <td style="vertical-align: top;">
            //         <div class="scrollable-content-bill-custom">
            //             <div class="fs-4">
            //                 ${productDetail[1]}
            //             </div>
            //             <div class="fs-6">
            //                 Màu: ${productDetail[2]}
            //                 <br>
            //                 Kích cỡ: ${productDetail[3]}
            //                 <br>
            //                 Hãng: ${productDetail[4]}
            //                 <br>
            //                 Chất liệu: ${productDetail[5]}
            //                 <br>
            //                 Nơi sản xuất: ${productDetail[6]}
            //                 <br>
            //                 Loại đế: ${productDetail[7]}
            //                 <br>
            //                 Danh mục: ${productDetail[15]}
            //             </div>
            //         </div>
            //     </td>
            //     <td class="text-center align-middle">
            //         ${productDetail[10]}
            //     </td>
            //     <td class="text-center align-middle">
            //        ${priceSaleAndRoot}
            //     </td>
            //     <td class="text-center align-middle">
            //         ${btn}
            //     </td>
            // </tr>
            // `);
            tbody.append(`
               <div class="col-3 mb-1">
                  <div class="card card-bill-custom" style="max-width: 800px; margin: auto; height: 500px;">
                    <!-- Mall label in the top-left corner -->
                    ${saleBadge}
                    <div style="height: 150px;" class="mb-2">
                      <div id="productCarousel1" class="carousel slide" data-bs-ride="carousel">
                        <div class="carousel-inner" style="height: 150px;"> <!-- Đặt chiều cao cố định cho carousel -->
                          ${imagesHtml}
                        </div>
                      </div>
                    </div>
                    <!-- Product details below image -->
                    <div class="card-body" style="max-height: 320px; overflow-y: auto;">
                      <h5 class="card-title">${productDetail[2]}</h5>
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
                      <h6>Số lượng trên hệ thống còn <br> ${productDetail[11]} đôi.</h6>
                      <h6>Giá: ${priceSaleAndRoot}</h6>
                    </div>
                    <div class="card-footer text-body-secondary text-center bg-white" style="height: 50px;">
                      ${btn}
                    </div>
                  </div>
                </div>

            `);
        });

        // Lắng nghe sự kiện khi mở modal
        $(document).on('click', '.btn-buy-product-detail', function() {
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
            nameProduct: $('#nameSearch').val() ? $('#nameSearch').val().trim() : '',  // Kiểm tra null hoặc undefined
            idColors: $('#colorSearch').val() ? $('#colorSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idSizes: $('#sizeSearch').val() ? $('#sizeSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idMaterials: $('#materialSearch').val() ? $('#materialSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idManufacturers: $('#manufacturerSearch').val() ? $('#manufacturerSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idOrigins: $('#originSearch').val() ? $('#originSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idSoles: $('#soleSearch').val() ? $('#soleSearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null,  // Xóa tất cả khoảng trắng và xử lý
            idCategories: $('#categorySearch').val() ? $('#categorySearch').val().trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number) : null  // Xóa tất cả khoảng trắng và xử lý
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
            loadVoucherByBill(1);
            maxPageVoucher();
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
                                    <p class="fs-5 text-muted mb-0">Số lượng: ${voucher.quantity}</p>
                                </div>
                                
                                <!-- Button to Select Voucher -->
<!--                                <a href="/bill/click-voucher-bill/" class="btn btn-outline-success btn-lg px-4">Chọn</a>-->
                                <button class="btn btn-outline-success btn-lg px-4" data-bs-dismiss="modal" aria-label="Close" onclick="getAddVoucherInBill(${voucher.id})">Chọn</button>
                            </div>
                        </div>
                    </div>
                `);
            });

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
    console.log(idVoucher)
    $.ajax({
        type: "POST",
        url: "/staff/bill/click-voucher-bill/"+idVoucher,
        success: function (response) {
            loadBillDetail(1);
            loadProduct(1)
            loadVoucherByBill(1);
            paymentInformation();
            maxPageVoucher();
            // loadBillStatusByBillId();
            var checkFormStatus = document.getElementById('checkFormStatus');
            if(checkFormStatus != null) {
                console.log('Phai vao duoc day')
                loadInformationBillByIdBill();
                loadCustomerShipInBill();
                $('#btn-Remove-voucher').show();
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
            maxPageVoucher();
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
            console.log(response + 'Day la so trang toi da cua voucher')
            createPagination('voucherPageMax', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

//ben trang quan ly hoa don
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
            maxPageVoucher();


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
    backToDefaultBuyProduct();
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
            maxPageVoucher();

            checkUpdateCustomer = true;

            totalShip(provinceTransport,districtTransport,wardTransport);


            showToast(response.message,response.check)

        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
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
    // $('#formSubmitFilterBill').submit(function (event) {
    //     event.preventDefault();
    //     searchBillManage();
    // })
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
    loadColorIntoSelect()
    loadSizeIntoSelect()
    loadMaterialIntoSelect()
    loadManufacturerIntoSelect()
    loadOriginIntoSelect()
    loadSoleIntoSelect()
    // pageNumber();
    // clickStatusBillManager(999);
    loadProduct(1)
    loadBillDetail(pageBillDetail);
    getMaxPageProduct();
    loadVoucherByBill(1);
    maxPageBillDetailByIdBill();
    maxPageVoucher();
    // getAllBilByStatus(1);
    // getMaxPageBillManage();
    console.log('can nang cua san pham la ' + totalWeight)
});
