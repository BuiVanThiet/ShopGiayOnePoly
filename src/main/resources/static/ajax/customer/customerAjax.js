function loadTableCustomer(page) {
    $.ajax({
        type: "GET",
        url: "/customer-api/all-customer-status-dislike-0/"+page,
        success: function (response) {
            var table = $('#table-customer');
            table.empty();
            response.forEach(function (customer,index) {
                console.log(customer)
                table.append(`
                <tr>
                    <td>
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1728721025/${customer.nameImage}"
                             style="height: 50px" alt="Ảnh khách hàng" />
                    </td>
                    <td>${customer.fullName}</td>
                    <td>${customer.gender == 1 ? 'Nam' : 'Nữ'}</td>
                    <td>${customer.addRessDetail}</td>
                    <td>${customer.status == 1 ? 'Hoạt động' : 'Ngưng hoạt động'}</td>
                    <td>
                        <a href="/customer/edit/${customer.id}"
                           class="btn btn-sm btn-outline-primary btn-outline-primary-customer me-1">
                            <i class="fas fa-edit" title="Sửa"></i>
                        </a>
                        <a href="/customer/detail/${customer.id}"
                           class="btn btn-sm btn-outline-info btn-outline-info-customer me-1">
                            <i class="fas fa-info-circle" title="Chi tiết"></i>
                        </a>
                        <a href="/customer/delete/${customer.id}"
                           class="btn btn-sm btn-outline-danger btn-outline-danger-customer"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa khách hàng?')">
                            <i class="fas fa-trash" title="Xóa"></i>
                        </a>
                    </td>
                </tr>
                `);
            })
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function loadPageTableCustomer() {
    $.ajax({
        type: "GET",
        url: "/customer-api/max-page-customer-status-dislike-0",
        success: function (response) {
            console.log('so trang la ' + response)
            createPagination('customerPageMax-customer-index', response, 1); // Phân trang 1

        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function searchCustomer() {
    var keyWordSearch = $('#keyWordSearch').val().trim();
    console.log('du lieu search ' + keyWordSearch)
    $.ajax({
        type: "POST",
        url: "/customer-api/search",
        contentType: "application/json",
        data: JSON.stringify({
            key: keyWordSearch
        }),
        success: function (response) {
            loadTableCustomer(1)
            loadPageTableCustomer();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}


$(document).ready(function () {
    $('#formSearchCustomer').submit(function (event){
        event.preventDefault();
        searchCustomer();
    })
    loadTableCustomer(1)
    loadPageTableCustomer();
});