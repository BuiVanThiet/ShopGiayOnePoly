const buttons = document.querySelectorAll('.custom-btn');
buttons.forEach(button => {
    button.addEventListener('click', function() {
        buttons.forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
    });
});

// Khởi tạo Google Charts
google.charts.load('current', {packages: ['corechart', 'bar']});
google.charts.setOnLoadCallback(() => {
    Last7DaysStatistics(); // Gọi hàm để vẽ biểu đồ cho 7 ngày gần đây
});

// Hàm lọc dữ liệu và hiển thị biểu đồ
function filterData(filterType) {
    if (filterType === 'month') {
        MonthlyStatistics(); // Gọi hàm để vẽ biểu đồ
    } else if(filterType === 'today'){
        TodayStatistics();
    } else if(filterType === 'last7days'){
        Last7DaysStatistics();
    } else if(filterType === 'year'){
        AnnualStatistics();
    }
}

// Hàm vẽ biểu đồ
function MonthlyStatistics() {
    fetch('/api/MonthlyStatistics') // Gọi API để lấy dữ liệu
        .then(response => response.json())
        .then(data => {
            var googleData = new google.visualization.DataTable();
            googleData.addColumn('string', 'Tháng');
            googleData.addColumn('number', 'Sản phẩm');
            googleData.addColumn('number', 'Hóa đơn');
            // Thêm dữ liệu vào googleData từ dữ liệu nhận được
            data.forEach(item => {
                googleData.addRow([item.month, item.productCount, item.invoiceCount]);
            });

            var options = {
                title: 'Biểu đồ thống kê sản phẩm và hóa đơn theo tháng',
                titleTextStyle: {
                    alignment: 'center'
                },
                vAxis: {
                    title: 'Số lượng'
                },
                hAxis: {
                    title: 'Tháng'
                },
                seriesType: 'bars',
                series: {
                    0: { type: 'bars', label: 'Sản phẩm' },
                    1: { type: 'bars', label: 'Hóa đơn' }
                },
            };

            var chart = new google.visualization.ColumnChart(
                document.getElementById('chart_div')
            );
            chart.draw(googleData, options);
        })
        .catch(error => console.error('Lỗi khi lấy dữ liệu:', error));
}

function TodayStatistics() {
    fetch('/api/TodayStatistics') // Gọi API để lấy dữ liệu
        .then(response => response.json())
        .then(data => {
            var googleData = new google.visualization.DataTable();
            googleData.addColumn('string', 'Tháng');
            googleData.addColumn('number', 'Sản phẩm');
            googleData.addColumn('number', 'Hóa đơn');

            // Thêm dữ liệu vào googleData từ dữ liệu nhận được
            data.forEach(item => {
                googleData.addRow([item.month, item.productCount, item.invoiceCount]);
            });

            var options = {
                title: 'Biểu đồ thống kê sản phẩm và hóa đơn hôm nay',
                titleTextStyle: {
                    alignment: 'center'
                },
                vAxis: {
                    title: 'Số lượng'
                },
                hAxis: {
                    title: 'Hôm nay'
                },
                seriesType: 'bars',
                series: {
                    0: { type: 'bars', label: 'Sản phẩm' },
                    1: { type: 'bars', label: 'Hóa đơn' }
                },
            };

            var chart = new google.visualization.ColumnChart(
                document.getElementById('chart_div')
            );

            chart.draw(googleData, options);
        })
        .catch(error => console.error('Lỗi khi lấy dữ liệu:', error));
}

function Last7DaysStatistics() {
    fetch('/api/Last7DaysStatistics') // Gọi API để lấy dữ liệu
        .then(response => response.json())
        .then(data => {
            var googleData = new google.visualization.DataTable();
            googleData.addColumn('string', 'Tháng');
            googleData.addColumn('number', 'Sản phẩm');
            googleData.addColumn('number', 'Hóa đơn');

            // Thêm dữ liệu vào googleData từ dữ liệu nhận được
            data.forEach(item => {
                googleData.addRow([item.month, item.productCount, item.invoiceCount]);
            });

            var options = {
                title: 'Biểu đồ thống kê sản phẩm và hóa đơn 7 ngày gần đây',
                titleTextStyle: {
                    alignment: 'center'
                },
                vAxis: {
                    title: 'Số lượng'
                },
                hAxis: {
                    title: '7 ngày gần đây'
                },
                seriesType: 'bars',
                series: {
                    0: { type: 'bars', label: 'Sản phẩm' },
                    1: { type: 'bars', label: 'Hóa đơn' }
                },
            };

            var chart = new google.visualization.ColumnChart(
                document.getElementById('chart_div')
            );

            chart.draw(googleData, options);
        })
        .catch(error => console.error('Lỗi khi lấy dữ liệu:', error));
}

function AnnualStatistics() {
    fetch('/api/AnnualStatistics') // Gọi API để lấy dữ liệu
        .then(response => response.json())
        .then(data => {
            var googleData = new google.visualization.DataTable();
            googleData.addColumn('string', 'Tháng');
            googleData.addColumn('number', 'Sản phẩm');
            googleData.addColumn('number', 'Hóa đơn');
            // Thêm dữ liệu vào googleData từ dữ liệu nhận được
            data.forEach(item => {
                googleData.addRow([item.month, item.productCount, item.invoiceCount]);
            });

            var options = {
                title: 'Biểu đồ thống kê sản phẩm và hóa đơn theo năm',
                titleTextStyle: {
                    alignment: 'center'
                },
                vAxis: {
                    title: 'Số lượng'
                },
                hAxis: {
                    title: 'Năm'
                },
                seriesType: 'bars',
                series: {
                    0: { type: 'bars', label: 'Sản phẩm' },
                    1: { type: 'bars', label: 'Hóa đơn' }
                },
            };

            var chart = new google.visualization.ColumnChart(
                document.getElementById('chart_div')
            );
            chart.draw(googleData, options);
        })
        .catch(error => console.error('Lỗi khi lấy dữ liệu:', error));
}

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function fetchData() {
    return fetch('/api/statusBills')
        .then(response => response.json())
        .then(data => {
            console.log("Dữ liệu từ API:", data);  // Kiểm tra dữ liệu trả về từ API
            var chartData = [['Trạng thái', 'Hóa đơn']];
            data.forEach(item => {
                console.log("Item:", item.statusDescription);  // Kiểm tra từng giá trị statusDescription
                chartData.push([item.statusDescription, item.countStatus]);
            });
            return chartData;
        })
        .catch(error => console.error('Error fetching data:', error));
}

// Draw the chart
function drawChart() {
    fetchData().then(chartData => {
        console.log("Dữ liệu chuẩn bị vẽ biểu đồ:", chartData);  // Kiểm tra dữ liệu trước khi vẽ

        // Tạo bảng dữ liệu cho biểu đồ
        var data = google.visualization.arrayToDataTable(chartData);

        // Thiết lập các tùy chọn cho biểu đồ
        var options = {
            title: 'Trạng thái đơn hàng tháng này',
            is3D: true,
            // Các tùy chọn khác nếu cần
        };

        // Vẽ biểu đồ
        var chart = new google.visualization.PieChart(document.getElementById('piechart'));
        chart.draw(data, options);
    });
}


// Hàm để tải dữ liệu sản phẩm và phân trang
function loadProductData(page = 0) {
    // Lưu lại vị trí cuộn hiện tại
    const scrollPosition = window.scrollY;

    fetch(`/api/productSales?page=${page}&size=3`)
        .then(response => response.json())
        .then(data => {
            const productTableBody = document.getElementById("productTableBody");
            const paginationContainer = document.getElementById("paginationContainer");

            // Xóa nội dung cũ
            productTableBody.innerHTML = '';
            paginationContainer.innerHTML = '';

            // Thêm dữ liệu vào bảng
            data.content.forEach((item, index) => {
                const row = `
                        <tr>
                            <td>${index + 1 + (data.pageable.pageNumber * data.pageable.pageSize)}</td>
                            <td><img src="${item.imageUrls[0]}" alt="Product Image" width="100" height="100"></td>
                            <td>
                                <span style="font-size: 20px; font-weight: normal;">${item.productName}</span><br>
                                <small>
                                    Màu sắc: ${item.colorName}<br>
                                    Kích thước: ${item.sizeName}
                                </small>
                            </td>
                            <td>
                                ${item.originalPrice === item.promotionalPrice
                    ? `<span>${item.originalPrice}</span>`
                    : `<span style="text-decoration: line-through; color: black;">${item.originalPrice}</span><br>
                                   <span style="color: red;">${item.promotionalPrice}</span>`}
                            </td>
                            <td>${item.totalQuantity}</td>
                        </tr>
                    `;
                productTableBody.insertAdjacentHTML('beforeend', row);
            });

            // Hiển thị các nút phân trang
            if (data.totalPages > 1) {
                // Nút Trước
                let prevPage = data.pageable.pageNumber - 1;
                if (prevPage < 0) {
                    prevPage = data.totalPages - 1; // Nếu ở trang đầu, chuyển về trang cuối cùng
                }

                paginationContainer.insertAdjacentHTML('beforeend', `
                        <li class="page-item ${data.pageable.pageNumber === 0 }">
                            <a class="page-link" href="#" onclick="loadProductData(${prevPage})">Trước</a>
                        </li>
                    `);

                // Số trang
                for (let i = 0; i < data.totalPages; i++) {
                    paginationContainer.insertAdjacentHTML('beforeend', `
                            <li class="page-item ${data.pageable.pageNumber === i ? 'active' : ''}">
                                <a class="page-link" href="#" onclick="loadProductData(${i})">${i + 1}</a>
                            </li>
                        `);
                }

                // Nút Sau
                let nextPage = data.pageable.pageNumber + 1;
                if (nextPage >= data.totalPages) {
                    nextPage = 0; // Nếu ở trang cuối, chuyển về trang đầu tiên
                }

                paginationContainer.insertAdjacentHTML('beforeend', `
                        <li class="page-item ${data.pageable.pageNumber === data.totalPages - 1 }">
                            <a class="page-link" href="#" onclick="loadProductData(${nextPage})">Sau</a>
                        </li>
                    `);
            }

            // Sau khi tải xong dữ liệu, cuộn về vị trí đã lưu
            window.scrollTo(0, scrollPosition);
        })
        .catch(error => console.error('Error:', error));
}

// Gọi hàm loadProductData khi trang được tải
document.addEventListener('DOMContentLoaded', function () {
    loadProductData();
});

