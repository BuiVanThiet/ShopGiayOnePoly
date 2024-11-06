function createPagination(elementId, totalPages, currentPage) {
    const element = document.getElementById(elementId);
    let liTag = '';
    let active;
    let beforePage = currentPage - 1;
    let afterPage = currentPage + 1;

    // Nút "Prev"
    if (currentPage > 1) {
        liTag += `<li class="btn prev" onclick="handlePageClick(${currentPage - 1}, '${elementId}', ${totalPages})"><span><i class="fas fa-angle-left"></i> Prev</span></li>`;
    }

    // Nút trang đầu
    if (currentPage > 2) {
        liTag += `<li class="first numb" onclick="handlePageClick(1, '${elementId}', ${totalPages})"><span>1</span></li>`;
        if (currentPage > 3) {
            liTag += `<li class="dots"><span>...</span></li>`;
        }
    }

    // Hiển thị các trang xung quanh trang hiện tại
    for (let plength = beforePage; plength <= afterPage; plength++) {
        if (plength > 0 && plength <= totalPages) {
            active = currentPage === plength ? "active" : "";
            liTag += `<li class="numb ${active}" onclick="handlePageClick(${plength}, '${elementId}', ${totalPages})"><span>${plength}</span></li>`;
        }
    }

    // Nút trang cuối
    if (currentPage < totalPages - 1) {
        if (currentPage < totalPages - 2) {
            liTag += `<li class="dots"><span>...</span></li>`;
        }
        liTag += `<li class="last numb" onclick="handlePageClick(${totalPages}, '${elementId}', ${totalPages})"><span>${totalPages}</span></li>`;
    }

    // Nút "Next"
    if (currentPage < totalPages) {
        liTag += `<li class="btn next" onclick="handlePageClick(${currentPage + 1}, '${elementId}', ${totalPages})"><span>Next <i class="fas fa-angle-right"></i></span></li>`;
    }

    element.innerHTML = liTag;
}

// Hàm xử lý nhấp chuột vào trang
function handlePageClick(pageNumber, elementId, totalPages) {
    console.log(`Page clicked: ${pageNumber}`); // Hiển thị số trang được ấn

    // Gọi hàm để ẩn lớp phủ tải khi nhấn nút
    hideLoadingOverlay(500);

    if (elementId === 'productPageMax') {
        loadProduct(pageNumber); // Gọi hàm loadProduct nếu điều kiện đúng
    } else if (elementId === 'billDetailPageMax') {
        loadBillDetail(pageNumber);
    }else if (elementId == 'voucherPageMax') {
        loadVoucherByBill(pageNumber);
    }else if (elementId == 'billManagePageMax') {
        getAllBilByStatus(pageNumber);
    }else if (elementId == 'billDetailPageMax-returnBill') {
        loadBillDetailFromReturnBill(pageNumber);
    }else if (elementId == 'billReturnPageMax-returnBill') {
        pageReturn = pageNumber;
        loadReturnBill(pageReturn);
    }else if (elementId == 'billExchangePageMax-exchangeBill') {
        pageExchange = pageNumber;
        loadExchangeBill(pageExchange);
    }

    createPagination(elementId, totalPages, pageNumber); // Cập nhật phân trang
}