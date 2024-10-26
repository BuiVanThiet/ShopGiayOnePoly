//
//
// document.addEventListener("DOMContentLoaded", function() {
//     // Lấy tất cả các nút màu
//     const colorButtons = document.querySelectorAll('.color-btn');
//
//     // Gán sự kiện click cho mỗi nút màu
//     colorButtons.forEach(function(button) {
//         button.addEventListener("click", function() {
//             const color = button.innerText;
//             setTemporaryColor(color);
//         });
//     });
// });
//
// function setTemporaryColor(color) {
//     // Đặt màu vào các phần tử temporary-color
//     document.querySelectorAll('.temporary-color').forEach(function(element) {
//         element.innerText = color;
//     });
//
//     // Cập nhật giá trị của selected-color
//     updateSelectedColor();
// }
//
// function updateSelectedColor() {
//     // Lấy giá trị màu từ temporary-color đầu tiên và cập nhật vào selected-color
//     const temporaryColor = document.querySelector('.temporary-color').innerText;
//     document.getElementById("selected-color").innerText = temporaryColor;
// }
//
// document.addEventListener("DOMContentLoaded", function() {
//     // Lấy tất cả các nút kích thước
//     const sizeButtons = document.querySelectorAll('.size-btn');
//
//     // Gán sự kiện click cho mỗi nút kích thước
//     sizeButtons.forEach(function(button) {
//         button.addEventListener("click", function() {
//             const size = button.innerText;
//             setTemporarySize(size);
//         });
//     });
// });
//
// function setTemporarySize(size) {
//     // Đặt kích thước vào các phần tử temporary-size
//     document.querySelectorAll('.temporary-size').forEach(function(element) {
//         element.innerText = size;
//     });
//
//     // Cập nhật giá trị của selected-size
//     updateSelectedSize();
// }
//
// function updateSelectedSize() {
//     // Lấy giá trị kích thước từ temporary-size đầu tiên và cập nhật vào selected-size
//     const temporarySize = document.querySelector('.temporary-size').innerText;
//     document.getElementById("selected-size").innerText = temporarySize;
// }