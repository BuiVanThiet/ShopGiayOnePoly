// Retrieve and parse quantity, fallback to 0 if not a valid number
var quantityProductByBill;
var cardError = $('#cardError');
var textError = $('#textError');
var btnReturn = $('#btnReturn');

$('#quantityReturnProduct').on('input', function () {
    var inputElement = $(this).val();
    if(inputElement.length < 1 || inputElement == '') {
        cardError.css('display', 'block');
        textError.text('Mời nhập số lượng trả!');
        btnReturn.attr('disabled', true);
    }else {
        if ($.isNumeric(inputElement)) {
            inputElement = parseFloat(inputElement); // Ensure the input is parsed as a float

            if (inputElement < 1) {
                cardError.css('display', 'block');
                textError.text('*Số lượng ít nhất phải là 1!');
                btnReturn.attr('disabled', true);
            } else if (inputElement > quantityProductByBill) {
                cardError.css('display', 'block');
                textError.text('*Số lượng không được quá số lượng mua!');
                btnReturn.attr('disabled', true);
            } else {
                cardError.css('display', 'none');
                textError.text('');
                btnReturn.attr('disabled', false);
            }
        } else {
            cardError.css('display', 'block');
            textError.text('*Vui lòng nhập số hợp lệ!');
            btnReturn.attr('disabled', true);
        }
    }
});
