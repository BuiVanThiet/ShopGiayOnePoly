var pageExchange = 1;
function loadCategoryIntoSelect_exchangeBill() {
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
function loadColorIntoSelect_exchangeBill() {
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
function loadSizeIntoSelect_exchangeBill() {
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
function loadMaterialIntoSelect_exchangeBill() {
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
function loadManufacturerIntoSelect_exchangeBill() {
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
function loadOriginIntoSelect_exchangeBill() {
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
function loadSoleIntoSelect_exchangeBill() {
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

function filterProduct() {
    $.ajax({
        type: 'POST',
        url: '/return-exchange-bill-api/filter-product-deatail',  // Endpoint xử lý
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

function loadProduct(pageNumber) {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/product-detaill-sell/" + pageNumber,
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
            var btn = '';
            var quantityProduct = '';
            var priceSaleAndRoot = '';
            // kiem tra giam gia
            if (productDetail[15] != 'Không giảm') {
                // Gán giá đã giảm và giá gốc vào biến priceSaleAndRoot
                priceSaleAndRoot = `
                <div>
                    <span class="text-decoration-line-through fs-6">${Math.trunc(productDetail[9]).toLocaleString('en-US')} VNĐ</span>
                    <br>
                    <span class="text-danger fs-5">${Math.trunc(productDetail[12]).toLocaleString('en-US')} VNĐ</span>
                </div>`;
            } else {
                // Nếu không có chương trình giảm giá, chỉ hiển thị giá gốc
                priceSaleAndRoot = `
                <div>
                    <span class="fs-5">${Math.trunc(productDetail[9]).toLocaleString('en-US')} VNĐ</span>
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
                                data-price-sale="${productDetail[12]}"
                                data-price-root="${productDetail[9]}"
                                onclick="resetHidenProductSale()">
                               <i class="bi bi-cart-plus"></i> Mua
                            </button>`;
            }

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
            tbody.append(`
               <div class="col-3 mb-1">
                  <div class="card card-bill-custom" style="max-width: 800px; margin: auto; height: 500px;">
                    <!-- Mall label in the top-left corner -->
                    ${saleBadge}
                    <div class="mb-2 ">
                      <div id="productCarousel1" class="carousel slide d-flex justify-content-center align-items-center" data-bs-ride="carousel">
                        <div class="carousel-inner" style="height: auto; width: 150px;"> <!-- Đặt chiều cao cố định cho carousel -->
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


function getMaxPageProduct() {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/page-max-product",
        success: function(response) {
            createPagination('productPageMax', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi o tao page ' + xhr.responseText);
        },
    });
}

function getExchangeProduct() {
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
            loadProduct(1);
            getMaxPageProduct();
            createToast(response.check, response.message);
            // showToast(response.message,response.check)
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

//load ra exchange Bill
function loadExchangeBill(page) {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/bill-exchange-detail/"+page,
        success: function (response) {
            var tbody = $('#tableExchangeBill');
            var noDataContainer = $('#noDataExchangeBill');
            tbody.empty(); // Xóa các dòng cũ

            if(response.length == 0) {
                $('#form-input-money-discount').hide(); // Ẩn phần tử khi trang đã tải
                // Nếu không có dữ liệu, hiển thị ảnh
                noDataContainer.html(`
                <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                     alt="Lỗi ảnh" style="width: auto; height: 100px;">
                     <p class="text-center">Không có sản phẩm nào!</p>
                `);
                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            }else {
                $('#form-input-money-discount').show(); // Ẩn phần tử khi trang đã tải
                noDataContainer.hide();
                tbody.closest('table').show();
                response.forEach(function (exchange,index) {
                    var imagesHtml = '';
                    exchange.productDetail.product.images.forEach(function(image, imgIndex) {
                        imagesHtml += `
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" data-bs-interval="10000">
                                <img style="height: auto; width: 100px;" src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh">
                            </div>`;
                    });
                    var price = '';
                    if(exchange.priceAtTheTimeOfExchange == exchange.priceRootAtTime) {
                        price = `<div>
                                    <span>${Math.trunc(exchange.priceAtTheTimeOfExchange).toLocaleString('en-US')} VNĐ</span>
                                </div>`;
                    }else {
                        price = ` <div>
                                    <span class="text-decoration-line-through">${Math.trunc(exchange.priceRootAtTime).toLocaleString('en-US')} VNĐ</span>
                                    <br>
                                    <span class="text-danger fs-5">${Math.trunc(exchange.priceAtTheTimeOfExchange).toLocaleString('en-US')} VNĐ</span>
                                </div>`;
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
                                    ${exchange.productDetail.product.nameProduct}
                                </div>
                                <div class="fs-6">
                                    Tên màu: ${exchange.productDetail.color.nameColor}
                                    <br>
                                    Tên size: ${exchange.productDetail.size.nameSize}
                                </div>
                            </td>

                            <td class="text-center align-middle">
                                ${price}
                            </td>
                            <td class="text-center align-middle">
                                 <div class="pagination mb-3 custom-number-input" style="width: 130px;" data-id="${exchange.productDetail.id}">
                                    <button class="button btn-decrement-exchange">-</button>
                                    <div class="number" id="pageNumber"> ${exchange.quantityExchange}</div>
                                    <button class="button btn-increment-exchange">+</button>
                                 </div>
                            </td>
                            <td class="text-center align-middle">
                                ${Math.trunc(exchange.totalExchange).toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
                                <button type="button"
                                   class="btn btn-outline-danger btn-return-product-detail"
                                   onclick="removeProductExchange(${exchange.productDetail.id},${exchange.quantityExchange})"
                                   >
                                    Xóa
                                </button>
                            </td>
                        </tr>`);
                })
                // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
                $('.carousel').each(function() {
                    $(this).carousel(); // Khởi tạo carousel cho từng phần tử
                });
            }
        },
        error: function (xhr) {
            console.error('Loi ' + xhr.responseText)
        }
    })
}

function maxPageExchangeBill() {
    $.ajax({
        type: "GET",
        url:"/return-exchange-bill-api/max-page-exchange-bill",
        success: function (response) {
            createPagination('billExchangePageMax-exchangeBill', response, 1);
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }

    })
}

function ressetListExchangeBill() {
    $.ajax({
        type: "GET",
        url:"/return-exchange-bill-api/reset-return-bill-detail",
        success: function (response) {
            loadCategoryIntoSelect_exchangeBill();
            loadColorIntoSelect_exchangeBill();
            loadSizeIntoSelect_exchangeBill();
            loadMaterialIntoSelect_exchangeBill();
            loadManufacturerIntoSelect_exchangeBill();
            loadOriginIntoSelect_exchangeBill();
            loadSoleIntoSelect_exchangeBill();
            loadProduct(1);
            getMaxPageProduct();
            loadExchangeBill(pageExchange);
            maxPageExchangeBill();
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function getExchangeProduct() {
    let quantity = $("#quantity").val();
    let idProductDetail = $("#idProductDetail").val();
    let priceProductSale = $("#priceProductSale").val();
    let priceProductRoot = $("#priceProductRoot").val();
    $.ajax({
        url: "/return-exchange-bill-api/exchange",
        type: "POST",
        data: {
            quantityDetail: quantity, // Dữ liệu số lượng
            idProductDetail: idProductDetail, // ID sản phẩm
            priceProductSale: priceProductSale,
            priceProductRoot: priceProductRoot
        },
        success: function (response) {
            loadProduct(1);
            getMaxPageProduct();
            pageExchange = 1;
            loadExchangeBill(pageExchange);
            maxPageExchangeBill();
            createToast(response.check, response.message);
            // showToast(response.message,response.check)
            loadInfomationReturnBill();

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

//xoa san pham di
function removeProductExchange(id,quantity) {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/remove-exchange-product/"+id+"/"+quantity,
        success: function (response) {
            loadProduct(1);
            getMaxPageProduct();
            pageExchange = 1
            loadExchangeBill(pageExchange);
            maxPageExchangeBill();
            loadInfomationReturnBill();
            createToast(response.check, response.message);

            // showToast(response.message,response.check)
        },
        error: function (xhr) {
            console.error('loi '+xhr.responseText)
        }
    })
}

//nut tang giam soluong
function updateQuantityExchange(id, quantity,method) {
    $.ajax({
        type:"post",
        url:"/return-exchange-bill-api/increase-or-decrease-product-exchange",
        contentType: "application/json", // Thêm dòng này để chỉ định kiểu nội dung là JSON
        data: JSON.stringify({
            id: id,
            quantity: quantity,
            method: method
        }),
        success: function (response) {
            console.log('Cập nhật thành công: ' + response);
            loadProduct(1);
            getMaxPageProduct();
            loadExchangeBill(pageExchange);
            loadInfomationReturnBill();
            createToast(response.check, response.message);

            // showToast(response.message,response.check)
        },
        error: function (xhr) {
            console.error('Lỗi khi cập nhật: ' + xhr.responseText);
        }
    })
}

$(document).ready(function () {
    $('#formBuyProduct').submit(function (event) {
        event.preventDefault();
        getExchangeProduct();
    })
    $('#formFilterProduct').submit(function (event) {
        event.preventDefault();
        filterProduct();
    })
    $(document).on('click', '.btn-increment-exchange', function () {
        var $numberDiv = $(this).siblings('.number');
        var value = parseInt($numberDiv.text(), 10);
        $numberDiv.text(value + 1);
        // Cập nhật giá trị mới trên server
        updateQuantityExchange($(this).closest('.custom-number-input').data('id'), $numberDiv.text(),'cong');
    });

    $(document).on('click', '.btn-decrement-exchange', function () {
        var $numberDiv = $(this).siblings('.number');
        var value = parseInt($numberDiv.text(), 10);
        if (value > 0) {
            $numberDiv.text(value - 1);
            // Cập nhật giá trị mới trên server
            updateQuantityExchange($(this).closest('.custom-number-input').data('id'), $numberDiv.text(),'tru');
        }
    });
    ressetListExchangeBill();

});