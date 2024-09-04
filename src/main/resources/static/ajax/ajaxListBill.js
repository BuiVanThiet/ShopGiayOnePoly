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
            success: function (response) {
                var tbody = $('#tableBillDetail');
                tbody.empty(); // Xóa các dòng cũ

                response.forEach(function (billDetail, index) {
                    var imagesHtml = '';

                    billDetail.productDetail.product.images.forEach(function (image, imgIndex) {
                        imagesHtml += `
                        <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}">
                            <img src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh">
                        </div>`;
                    });

                    tbody.append(`
                    <tr>
                        <th scope="row">${index + 1}</th>
                        <td>
                            <div id="carouselExampleAutoplaying${index}" class="carousel slide" data-bs-ride="carousel">
                                <div class="carousel-inner">
                                    ${imagesHtml}
                                </div>
                                <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleAutoplaying${index}" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleAutoplaying${index}" data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
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
            },
            error: function (xhr) {
                console.error("Lỗi khi hiển thị chi tiết hóa đơn: " + xhr.responseText);
            }
        });
    }

    loadBillNew();
    loadBillDetail();
});
