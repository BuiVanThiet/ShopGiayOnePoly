function deleteAddressForCustomer(idAddress) {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn xóa địa chỉ này hay không này?',
        text: "Sau khi xác nhận, địa chỉ này sẽ bị xóa và không thể khôi phục.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        customClass: {
            popup: 'swal-popup'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/api-client/delete/address-customer/' + idAddress,
                type: 'GET',
                success: function (response) {
                    createToast(response.check, response.message);
                    listAddressForCustomer()
                },
                error: function (error) {
                   createToast("2","Xóa thất bại")
                }
            })
        }
    });
}

function listAddressForCustomer() {
    $.ajax({
        type: 'GET',
        url: '/api-client/list-address-for-customer',
        success: function (response) {
            var div = $('#info-address-for-customer');
            var noData = $('#noData-info-address-for-customer');
            div.empty();

            if (response.length === 0) {
                noData.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có địa chỉ nào!</p>
                    `);
                noData.show();
                div.hide();
            } else {
                noData.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                div.show(); // Hiển thị lại table nếu có dữ liệu

                response.forEach(function (listAddress, index) {
                    div.append(`
                    <div class="list-address-customer">
                        <div class="info-address-shipping d-flex align-items-center border rounded p-2 mb-2 position-relative">
                            <!-- Radio button -->
                            <input class="form-check-input me-3" type="radio" name="selectedAddress"/>
                            <!-- Thông tin địa chỉ -->
                            <div class="address-info flex-grow-1">
                                <p class="name-phoneNumber mb-1" >${listAddress.nameAndPhoneNumber}</p>
                                <p class="short-address mb-0 text-muted">${listAddress.shortAddress}</p>
                                <input type="hidden" class="id-address" value="${listAddress.id}"/>
                                <input type="hidden" class="original-address" value="${listAddress.originalAddress}"/>
                                <input type="hidden" id="fullAddressInput" value="${listAddress.fullAddress}" class="full-address"/>
                            </div>
                            <!-- Nút hành động -->
                            <div class="button-container d-flex gap-1">
                                <button class="btn-update-address btn btn-sm btn-outline-primary"
                                        type="button" data-bs-toggle="modal"
                                        data-bs-target="#updateAddressModal"
                                        onclick="getAddressDetails(this)">
                                    <i class="fas fa-sync-alt"></i>
                                </button>
                                <button class="btn-delete-address btn btn-sm btn-outline-danger"
                                        type="button" onclick="deleteAddressForCustomer(${listAddress.id})">
                                    <i class="fas fa-trash-alt"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                `);
                })
            }
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

$(document).ready(function () {
    listAddressForCustomer()
});