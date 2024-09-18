// document.addEventListener("DOMContentLoaded", function() {
//     const element = document.querySelector(".pagination-custom ul");
//     let currentPage = 1; // Trang hiện tại
//
//     // Lấy số trang tối đa và tạo phân trang
//     fetchTotalPages(function(totalPages) {
//         updatePagination(totalPages, currentPage);
//     });
//
//     function updatePagination(totalPages, page) {
//         element.innerHTML = createPagination(totalPages, page);
//     }
//
//     function createPagination(totalPages, page) {
//         let liTag = '';
//         let active;
//         let beforePage = page - 1;
//         let afterPage = page + 1;
//
//         if (page > 1) { // hiện nút "Trước" nếu giá trị trang lớn hơn 1
//             liTag += `<li class="btn prev" onclick="navigateToPage(${page - 1})"><span><i class="fas fa-angle-left"></i> Prev</span></li>`;
//         }
//
//         if (page > 2) { // nếu giá trị trang lớn hơn 2 thì thêm trang 1 sau nút "Trước"
//             liTag += `<li class="first numb" onclick="navigateToPage(1)"><span>1</span></li>`;
//             if (page > 3) { // nếu giá trị trang lớn hơn 3 thì thêm dấu (...) sau trang đầu tiên
//                 liTag += `<li class="dots"><span>...</span></li>`;
//             }
//         }
//
//         // điều chỉnh số trang hiển thị trước và sau trang hiện tại
//         if (page === totalPages) {
//             beforePage = beforePage - 2;
//         } else if (page === totalPages - 1) {
//             beforePage = beforePage - 1;
//         }
//         if (page === 1) {
//             afterPage = afterPage + 2;
//         } else if (page === 2) {
//             afterPage = afterPage + 1;
//         }
//
//         // đảm bảo trước trang hiện tại không nhỏ hơn 1
//         beforePage = Math.max(beforePage, 1);
//         // đảm bảo sau trang hiện tại không lớn hơn tổng số trang
//         afterPage = Math.min(afterPage, totalPages);
//
//         for (let plength = beforePage; plength <= afterPage; plength++) {
//             if (plength > totalPages) { // nếu plength lớn hơn tổng số trang thì tiếp tục
//                 continue;
//             }
//             if (plength === 0) { // nếu plength bằng 0 thì cộng thêm 1 vào giá trị plength
//                 plength = 1;
//             }
//             if (page === plength) { // nếu trang bằng plength thì gán chuỗi "active" vào biến active
//                 active = "active";
//             } else { // nếu không thì để biến active trống
//                 active = "";
//             }
//             liTag += `<li class="numb ${active}" onclick="navigateToPage(${plength})"><span>${plength}</span></li>`;
//         }
//
//         if (page < totalPages - 1) { // nếu giá trị trang nhỏ hơn tổng số trang trừ 1 thì hiện trang cuối cùng
//             if (page < totalPages - 2) { // nếu giá trị trang nhỏ hơn tổng số trang trừ 2 thì thêm dấu (...) trước trang cuối cùng
//                 liTag += `<li class="dots"><span>...</span></li>`;
//             }
//             liTag += `<li class="last numb" onclick="navigateToPage(${totalPages})"><span>${totalPages}</span></li>`;
//         }
//
//         if (page < totalPages) { // hiện nút "Tiếp theo" nếu giá trị trang nhỏ hơn tổng số trang
//             liTag += `<li class="btn next" onclick="navigateToPage(${page + 1})"><span>Next <i class="fas fa-angle-right"></i></span></li>`;
//         }
//
//         return liTag; // trả về các thẻ li
//     }
//
//     window.navigateToPage = function(page) {
//         fetchTotalPages(function(totalPages) {
//             updatePagination(totalPages, page);
//         });
//     }
// });
