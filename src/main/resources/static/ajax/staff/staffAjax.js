function loadTableStaff(page) {
    $.ajax({
        type: "GET",
        url: "/staff-api/all-staff-status-dislike-0/" + page,
        success: function (response) {
            var table = $('#table-staff');
            table.empty();
            response.forEach(function (staff, index) {
                console.log(staff)
                table.append(`
                <tr>
                    <td>
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1728721025/${staff.nameImage}"
                             style="height: 50px" alt="Ảnh nhân viên" />
                    </td>
                    <td>${staff.codeStaff}</td>
                    <td>${staff.fullName}</td>
                    <td>${staff.address}</td>
                    <td>${staff.status == 1 ? 'Hoạt động' : 'Ngưng hoạt động'}</td>
                    <td>
                        <a href="/staff-manage/exchange-pass-word/${staff.id}"
                           class="btn btn-sm btn-outline-danger btn-outline-danger-staff me-1">
                            <i class="bi bi-pass" title="Đổi mật khẩu"></i>
                        </a>
                        <a href="/staff-manage/edit/${staff.id}"
                           class="btn btn-sm btn-outline-primary btn-outline-primary-staff me-1">
                            <i class="fas fa-edit" title="Sửa"></i>
                        </a>
                        <a href="/staff-manage/detail/${staff.id}"
                           class="btn btn-sm btn-outline-info btn-outline-info-staff me-1">
                            <i class="fas fa-info-circle" title="Chi tiết"></i>
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

function loadPageTableStaff() {
    $.ajax({
        type: "GET",
        url: "/staff-api/max-page-staff-status-dislike-0",
        success: function (response) {
            console.log('so trang la ' + response)
            createPagination('staffPageMax-staff-index', response, 1); // Phân trang 1

        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function searchStaff() {
    var keyWordSearch = $('#keyWordSearch').val().trim();
    console.log('du lieu search ' + keyWordSearch)
    $.ajax({
        type: "POST",
        url: "/staff-api/search",
        contentType: "application/json",
        data: JSON.stringify({
            key: keyWordSearch
        }),
        success: function (response) {
            loadTableStaff(1)
            loadPageTableStaff();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}


$(document).ready(function () {
    $('#formSearchStaff').submit(function (event) {
        event.preventDefault();
        searchStaff();
    })
    loadTableStaff(1)
    loadPageTableStaff();
});