document.addEventListener('input', function(event) {
    // Kiểm tra xem sự kiện xảy ra trên trường nhập liệu số lượng hay không
    if (event.target.matches('input[id$="quantity"]')) {
        // Lấy ID của sản phẩm từ thuộc tính ID của input
        var input = event.target;
        var productId = input.id.replace('quantity', '');

        // Lấy giá trị input số lượng người dùng nhập
        var inputQuantity = input.value.trim();

        // Lấy số lượng sản phẩm có sẵn
        var availableQuantity = document.getElementById('quantityProduct').value;

        // Lấy phần tử hiển thị lỗi
        var errorElement = document.getElementById('erorrQuantity');
        var errorCardCheck = document.getElementById('errorCard');
        var btnBuyQuantity = document.getElementById( 'btnBuy');

        // Kiểm tra nếu không nhập gì
        if (inputQuantity === '') {
            errorCardCheck.style.display = 'block';
            errorElement.textContent = '*Mời nhập số lượng mua!';
            btnBuyQuantity.disabled = true;
        } else if (!isInteger(inputQuantity) || parseInt(inputQuantity) <= 0) {
            // Kiểm tra số lượng nhập có phải là số nguyên và hợp lệ không
            errorCardCheck.style.display = 'block';
            errorElement.textContent = '*Số lượng phải là một số nguyên dương!';
            btnBuyQuantity.disabled = true;
        } else if (parseInt(inputQuantity) > parseInt(availableQuantity)) {
            // Kiểm tra số lượng mua vượt quá số lượng sản phẩm có sẵn
            errorCardCheck.style.display = 'block';
            errorElement.textContent = '*Số lượng mua vượt quá số lượng sản phẩm có sẵn!';
            btnBuyQuantity.disabled = true;
        }

        else {
            // Xóa lỗi nếu không có
            errorCardCheck.style.display = 'none';
            errorElement.textContent = '';
            btnBuyQuantity.disabled = false;
        }
    }
});

// Check xem có phải số nguyên không
function isInteger(value) {
    var numberValue = parseInt(value, 10);
    return !isNaN(numberValue) && numberValue.toString() === value.toString();
}
function backToDefaultBuyProduct() {
    document.getElementById('quantity').value = '';
    document.getElementById('errorCard').style.display = 'block';
    document.getElementById('erorrQuantity').textContent = '*Mời nhập số lượng mua!';
    document.getElementById('btnBuy').disabled = true;
}
// document.getElementById('comeBackProduct').addEventListener('click', function () {
//     backToDefaultBuyProduct();
// })

