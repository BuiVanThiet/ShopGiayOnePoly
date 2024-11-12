window.onload = function() {
    const scrollPosition = sessionStorage.getItem('scrollPosition');
    if (scrollPosition) {
        window.scrollTo(0, scrollPosition);
    }
};

// Trước khi trang bị làm mới, lưu lại vị trí cuộn hiện tại
window.onbeforeunload = function() {
    sessionStorage.setItem('scrollPosition', window.scrollY);
};

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
                title: 'Biểu đồ thống kê sản phẩm và hóa đơn theo tháng trong năm nay',
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
                title: 'Biểu đồ thống kê sản phẩm và hóa đơn theo các năm',
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

// Hàm để tải dữ liệu sản phẩm và phân trang
async function fetchProductSales(page = 0) {
    try {
        // Lưu lại vị trí cuộn trước khi tải dữ liệu mới
        const currentScrollPosition = window.scrollY;

        const response = await fetch(`/api/productSales?page=${page}&size=3`);
        if (!response.ok) {
            console.error('Failed to fetch data');
            return;
        }

        const data = await response.json();
        console.log('Response Data:', data);

        const productTableBody = document.getElementById("productTableBody");
        const paginationContainer = document.getElementById("paginationContainer");

        productTableBody.innerHTML = '';
        paginationContainer.innerHTML = '';

        if (!data.content || data.content.length === 0) {
            productTableBody.insertAdjacentHTML('beforeend', `<tr><td colspan="5">No products found</td></tr>`);
            return;
        }

        // Kiểm tra `pageable` trước khi sử dụng
        const pageable = data.pageable || { pageNumber: 0, pageSize: 3 };

        // Thêm dữ liệu vào bảng
        data.content.forEach((item, index) => {
            const row = `
                <tr>
                    <td>${index + 1 + (pageable.pageNumber * pageable.pageSize)}</td>
                    <td><img id="product-image-${index}" src="${item.imageUrls[0]}" alt="Product Image" width="100" height="100"></td>
                    <td>
                        <span>${item.productName}</span><br>
                        <small>
                            Màu: ${item.colorName}<br>
                            Kích thước: ${item.sizeName}
                        </small>

                    </td>
                    <td>
                        ${item.originalPrice === item.promotionalPrice
                ? `${item.originalPrice}`
                : `<span style="text-decoration: line-through;">${item.originalPrice}</span>
                                <br>
                                <span style="color: red; font-size: 1.2em;">${item.promotionalPrice}</span>`}
                    </td>

                    <td>${item.totalQuantity}</td>
                </tr>
            `;
            productTableBody.insertAdjacentHTML('beforeend', row);

            // Hiển thị ảnh thay đổi mỗi 3 giây cho mỗi sản phẩm
            let currentImageIndex = 0;
            const imageElement = document.getElementById(`product-image-${index}`);
            const imageUrls = item.imageUrls;

            // Hàm thay đổi ảnh
            const changeImage = () => {
                imageElement.src = imageUrls[currentImageIndex];
                currentImageIndex = (currentImageIndex + 1) % imageUrls.length;  // Chuyển đến ảnh tiếp theo
            };

            // Thay đổi ảnh mỗi 10 giây (10000ms)
            setInterval(changeImage, 10000);

            // Hiển thị ảnh đầu tiên ngay lập tức
            changeImage();
        });

        // Hiển thị các nút phân trang nếu có nhiều hơn 1 trang
        if (data.totalPages > 1) {
            const prevPage = pageable.pageNumber > 0 ? pageable.pageNumber - 1 : data.totalPages - 1;
            paginationContainer.insertAdjacentHTML('beforeend', `
                <li class="page-item">
                    <a class="page-link" href="#" onclick="fetchProductSales(${prevPage})">Trước</a>
                </li>
            `);

            for (let i = 0; i < data.totalPages; i++) {
                paginationContainer.insertAdjacentHTML('beforeend', `
                    <li class="page-item ${pageable.pageNumber === i ? 'active' : ''}">
                        <a class="page-link" href="#" onclick="fetchProductSales(${i})">${i + 1}</a>
                    </li>
                `);
            }

            const nextPage = pageable.pageNumber < data.totalPages - 1 ? pageable.pageNumber + 1 : 0;
            paginationContainer.insertAdjacentHTML('beforeend', `
                <li class="page-item">
                    <a class="page-link" href="#" onclick="fetchProductSales(${nextPage})">Sau</a>
                </li>
            `);
        }

        // Phục hồi lại vị trí cuộn đã lưu trước khi tải lại trang
        window.scrollTo(0, currentScrollPosition);

    } catch (error) {
        console.error('Error:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => fetchProductSales());


// BIểu đồ tròn
google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(() => filterDataPie('month-pieChart')); // Mặc định là hiển thị tháng

// Hàm lấy dữ liệu cho hôm nay
function fetchDataToday() {
    return fetch('/api/statusBillsToday')
        .then(response => response.json())
        .then(data => {
            var chartData = [['Trạng thái', 'Hóa đơn']];
            data.forEach(item => {
                chartData.push([item.statusDescription, item.countStatus]);
            });
            return chartData;
        })
        .catch(error => console.error('Error fetching data for today:', error));
}

// Hàm lấy dữ liệu cho 7 ngày qua
function fetchDataLast7Days() {
    return fetch('/api/statusBillsLast7Days')
        .then(response => response.json())
        .then(data => {
            var chartData = [['Trạng thái', 'Hóa đơn']];
            data.forEach(item => {
                chartData.push([item.statusDescription, item.countStatus]);
            });
            return chartData;
        })
        .catch(error => console.error('Error fetching data for last 7 days:', error));
}

// Hàm lấy dữ liệu cho tháng
function fetchDataMonth() {
    return fetch('/api/statusBillsMonth')
        .then(response => response.json())
        .then(data => {
            var chartData = [['Trạng thái', 'Hóa đơn']];
            data.forEach(item => {
                chartData.push([item.statusDescription, item.countStatus]);
            });
            return chartData;
        })
        .catch(error => console.error('Error fetching data for month:', error));
}

// Hàm lấy dữ liệu cho năm
function fetchDataYear() {
    return fetch('/api/statusBillsYear')
        .then(response => response.json())
        .then(data => {
            var chartData = [['Trạng thái', 'Hóa đơn']];
            data.forEach(item => {
                chartData.push([item.statusDescription, item.countStatus]);
            });
            return chartData;
        })
        .catch(error => console.error('Error fetching data for year:', error));
}

// Hàm vẽ biểu đồ
function drawChart(chartData, title, period) {
    const pieChartDiv = document.getElementById('piechart');

    // Kiểm tra nếu không có dữ liệu ngoài tiêu đề
    if (chartData.length <= 1) {
        let message;
        switch (period) {
            case 'today-pieChart':
                message = "Hôm nay chưa có hóa đơn.";
                break;
            case 'last7days-pieChart':
                message = "7 ngày qua chưa có hóa đơn.";
                break;
            case 'month-pieChart':
                message = "Tháng này chưa có hóa đơn.";
                break;
            case 'year-pieChart':
                message = "Năm nay chưa có hóa đơn.";
                break;
            default:
                message = "Chưa có dữ liệu.";
                break;
        }

        // Hiển thị thông báo nếu không có dữ liệu
        pieChartDiv.innerHTML = `
            <div class="text-center mt-4">
                <h5>${title}</h5>
                <p>${message}</p>
            </div>`;
        return;
    }

    // Tạo bảng dữ liệu cho biểu đồ
    const data = google.visualization.arrayToDataTable(chartData);

    // Thiết lập các tùy chọn cho biểu đồ với title truyền vào
    const options = {
        title: title,
        is3D: true,
    };

    // Vẽ biểu đồ
    const chart = new google.visualization.PieChart(pieChartDiv);
    chart.draw(data, options);
}

// Hàm thay đổi dữ liệu khi ấn nút
function filterDataPie(period) {
    switch (period) {
        case 'today-pieChart':
            fetchDataToday().then(chartData => drawChart(chartData, 'Trạng thái đơn hàng hôm nay', 'today-pieChart'));
            break;
        case 'last7days-pieChart':
            fetchDataLast7Days().then(chartData => drawChart(chartData, 'Trạng thái đơn hàng 7 ngày qua', 'last7days-pieChart'));
            break;
        case 'month-pieChart':
            fetchDataMonth().then(chartData => drawChart(chartData, 'Trạng thái đơn hàng trong tháng này', 'month-pieChart'));
            break;
        case 'year-pieChart':
            fetchDataYear().then(chartData => drawChart(chartData, 'Trạng thái đơn hàng trong năm nay', 'year-pieChart'));
            break;
        default:
            fetchDataMonth().then(chartData => drawChart(chartData, 'Trạng thái đơn hàng trong tháng này', 'month-pieChart')); // Mặc định là tháng
            break;
    }
}

window.onload = function() {
    filterDataPie('month-pieChart'); // Hiển thị biểu đồ mặc định là tháng
};

