$(document).ready(function () {
    function loadBillNew() {
        $.ajax({
            type: "GET",
            url: "/bill-api/all-new",
            success: function (response) {
                var ul = $('#billBody');
                ul.empty();
                response.forEach(function (url) {
                    ul.append(
                        '<li class="nav-item">' +
                        '<a class="nav-link text-dark" href="' + '/bill/bill-detail/' + url.id + '">' + url.codeBill + '</a>' +
                        '</li>'
                    );
                });
            },
            error: function (xhr) {
                console.error("Lỗi hiển thị bill: " + xhr.responseText);
            }
        });
    }

    function loadBillDetail() {
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
                            <td>${billDetail.quantity}</td>
                            <td>${billDetail.totalAmount}</td>
                            <td>
                                <a href="#" class="btn btn-danger">Xóa bỏ</a>
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

    loadBillNew();
    loadBillDetail();
});
