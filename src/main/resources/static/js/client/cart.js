function formatCurrency(amount) {
    // Định dạng số thành tiền tệ với ký hiệu ₫
    return amount.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}).replace('₫', '₫'); // thêm ký hiệu ₫
}

// Hàm tính toán tổng tiền
function calculateTotal() {
    // Lấy tất cả các phần tử chứa giá trị giá sản phẩm
    const priceCartItemElement = document.querySelectorAll('.price-item');
    const totalPriceElement = document.getElementById('spanTotalPriceCart');
    const priceCartItem = parseFloat(priceCartItemElement.textContent.replace(/\D/g, '') || '0');
    totalPriceElement.textContent = formatCurrency(totalPrice);
}

const observer = new MutationObserver(calculateTotal);

const priceCartItemElements = document.querySelectorAll('.price-item');
priceCartItemElements.forEach(element => {
    observer.observe(element, { childList: true, subtree: true });
});

calculateTotal();
