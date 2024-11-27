
function validateAndFormatCells() {
    const errorText = document.getElementById('errorTextCreateProductDetail');
    const numericColumns = {
        3: { name: "Giá bán", min: 1000, max: Infinity }, // Giá bán: >= 1000
        4: { name: "Giá nhập", min: 1000, max: Infinity }, // Giá nhập: >= 1000
        5: { name: "Số lượng", min: 1, max: Infinity },    // Số lượng: > 0
        6: { name: "Trọng lượng", min: 1, max: 5000 }      // Trọng lượng: <= 5000
    };

    let hasError = false; // Biến đánh dấu nếu có lỗi

    document.querySelectorAll('#productDetailTable tbody tr').forEach(row => {
        Object.keys(numericColumns).forEach(colIndex => {
            const cell = row.cells[colIndex];
            const rawValue = cell.textContent.trim(); // Giá trị chưa xử lý
            const value = rawValue.replace(/\./g, ''); // Loại bỏ dấu chấm để kiểm tra
            const columnRules = numericColumns[colIndex];

            // Lưu trạng thái con trỏ trước khi sửa đổi
            const selection = window.getSelection();
            const range = selection.rangeCount > 0 ? selection.getRangeAt(0) : null;
            const cursorOffset = range ? range.startOffset : null;

            // Kiểm tra điều kiện hợp lệ
            if (isNaN(value) || value === "" || Number(value) < columnRules.min || Number(value) > columnRules.max) {
                // Không hợp lệ
                cell.style.backgroundColor = '#d3d3d3';
                hasError = true;
            } else {
                // Hợp lệ
                cell.style.backgroundColor = '';
                if (colIndex !== '5' && colIndex !== '6') { // Không định dạng cột "Số lượng" và "Trọng lượng"
                    const formattedValue = formatNumber(Number(value));
                    if (rawValue !== formattedValue) {
                        // Cập nhật chỉ khi cần định dạng
                        cell.textContent = formattedValue;
                    }
                }
            }

            // Khôi phục vị trí con trỏ
            if (cursorOffset !== null && cell.contains(selection.anchorNode)) {
                const newRange = document.createRange();
                const textNode = cell.childNodes[0];
                const newCursorPosition = Math.min(cursorOffset, textNode.length); // Đảm bảo vị trí không vượt quá độ dài nội dung
                newRange.setStart(textNode, newCursorPosition);
                newRange.collapse(true);
                selection.removeAllRanges();
                selection.addRange(newRange);
            }
        });
    });

    // Hiển thị hoặc ẩn thông báo lỗi
    if (hasError) {
        errorText.style.visibility = 'visible';
        errorText.innerText = "Giá bán, Giá nhập > 1.000. Trọng lượng <= 5.000";
        return false;
    } else {
        errorText.style.visibility = 'hidden';
        return true;
    }
}

function addValidationListeners() {
    const numericColumns = [3, 4, 5, 6]; // Các cột cần kiểm tra

    document.querySelectorAll('#productDetailTable tbody tr').forEach(row => {
        numericColumns.forEach(colIndex => {
            const cell = row.cells[colIndex];
            if (cell && cell.contentEditable === "true") {
                cell.addEventListener('input', validateAndFormatCells);
            }
        });
    });
}
function formatNumber(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}