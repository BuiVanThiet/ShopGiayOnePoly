function createPagination(elementId, totalPages, currentPage) {
    const element = document.getElementById(elementId);
    let liTag = '';
    let active;
    let beforePage = currentPage - 1;
    let afterPage = currentPage + 1;

    // Nút "Prev"
    if (currentPage > 1) {
        liTag += `<li class="btn prev" onclick="handlePageClick(${currentPage - 1}, '${elementId}', ${totalPages})"><span><i class="fas fa-angle-left"></i> </span></li>`;
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
        liTag += `<li class="btn next" onclick="handlePageClick(${currentPage + 1}, '${elementId}', ${totalPages})"><span> <i class="fas fa-angle-right"></i></span></li>`;
    }

    element.innerHTML = liTag;
}

// Hàm xử lý nhấp chuột vào trang
function handlePageClick(pageNumber, elementId, totalPages) {
    console.log(`Page clicked: ${pageNumber}`); // Hiển thị số trang được ấn

    // Gọi hàm để ẩn lớp phủ tải khi nhấn nút
    // hideLoadingOverlay(500);

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
    }else if (elementId == 'billClientPageMax-billStatus') {
        loadProductBuy(pageNumber);
    }


    if(elementId == 'customerPageMax-customer-index') {
        loadTableCustomer(pageNumber);
    }

    if(elementId == 'staffPageMax-staff-index') {
        loadTableStaff(pageNumber);
    }

    if(elementId == 'maxPageVoucher-manageVoucher') {
        loadVoucher(pageNumber);
    }

    if(elementId == 'maxPageSaleProduct-manageSaleProduct') {
        loadSaleProduct(pageNumber);
    }

    if(elementId == 'maxPageProduct-manageSaleProduct') {
        loadProduct(pageNumber);
    }

    if(elementId == 'shiftPageMax-manage') {
        loadShift(pageNumber)
    }

    if (elementId == 'maxPageTimekeeping-manage') {
        timekeepingList(pageNumber)
    }

    if(elementId  == 'maxPageCashierInventory-manage') {
        cashierInventoryList(pageNumber);
    }

    if(elementId == 'maxStaff-manageShift') {
        listStaff(pageNumber)
    }

    if(elementId == 'maxPageCashierInventoryByIdBill-manage' ) {
        cashierInventoryListByIdStaff(pageNumber)
    }

    if(elementId == 'maxPageInvoiceStatusByStaff-manage') {
        invoiceStatusByStaffByIdStaff(pageNumber)
    }

    if(elementId == 'maxPageTimKeepingByStaff-manage') {
        infoCheckInAndCheckOutByStaff(pageNumber);
    }

    if (elementId == 'billClientPageMax') {
        getAllBillClientByStatus(pageNumber)
    }

    if(elementId == 'transactionVNPayPageMax') {
        loadAllTransactionVNPay(pageNumber);
    }

    createPagination(elementId, totalPages, pageNumber); // Cập nhật phân trang
}