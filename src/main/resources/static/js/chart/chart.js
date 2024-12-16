const customButtons = document.querySelectorAll('.custom-btn');
customButtons.forEach(button => {
    button.addEventListener('click', function() {
        customButtons.forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
    });
});


google.charts.load('current', {packages: ['corechart', 'bar']});
google.charts.setOnLoadCallback(() => {
    TodayStatistics();
});
window.onload = function() {
    filterDataPie('month-pieChart')
};
function initDefaultToday() {
    filterData('today');
}
window.onload = initDefaultToday;

function filterData(filterType) {
    if (filterType === 'month') {
        MonthlyStatistics();
        filterDataPie('month-pieChart');
        filterTopProducts('month');
        filterTopProductsExchange('month');
        filterTopProductsReturn('month');
    } else if (filterType === 'today') {
        TodayStatistics();
        filterDataPie('today-pieChart');
        filterTopProducts('today');
        filterTopProductsExchange('today')
        filterTopProductsReturn('today');
    } else if (filterType === 'last7days') {
        Last7DaysStatistics();
        filterDataPie('last7days-pieChart');
        filterTopProducts('last7days');
        filterTopProductsExchange('last7days')
        filterTopProductsReturn('last7days');
    } else if (filterType === 'year') {
        AnnualStatistics();
        filterDataPie('year-pieChart');
        filterTopProducts('year');
        filterTopProductsExchange('year')
        filterTopProductsReturn('year');
    }
    const today = new Date();
    let startDate, endDate;

    switch (filterType) {
        case 'today':
            startDate = new Date(today);
            endDate = new Date(today);
            title = "hôm nay";
            break;
        case 'last7days':
            startDate = new Date(today);
            startDate.setDate(today.getDate() - 7);
            endDate = new Date(today);
            title = "7 ngày qua";
            break;
        case 'month':
            startDate = new Date(today.getFullYear(), today.getMonth(), 1);
            endDate = new Date(today);
            title = "tháng này";
            break;
        case 'year':
            startDate = new Date(today.getFullYear(), 0, 1);
            endDate = new Date(today);
            title = "năm nay";
            break;
        default:
            console.error("Filter type không hợp lệ!");
            return;
    }

    document.getElementById('serviceFeeTitle').innerText = "Phí dịch vụ " + title;
    document.getElementById('exchangeFeeTitle').innerText = "Phí đổi hàng " + title;
    document.getElementById('returnFeeTitle').innerText = "Phí trả hàng " + title;

    const formattedStartDate = formatDateForAPI(startDate);
    const formattedEndDate = formatDateForAPI(endDate);

    console.log(`Lọc dữ liệu từ ${formattedStartDate} đến ${formattedEndDate}`);


    fetch(`/api/returnFee?startDate=${formattedStartDate}&endDate=${formattedEndDate}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Lỗi khi lấy dữ liệu từ API: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Dữ liệu từ API:", data);
            const returnFee = data || 0;
            console.log("Phí trả hàng:", returnFee);


            const formattedReturnFee = new Intl.NumberFormat('vi-VN', {
                style: 'decimal',
                minimumFractionDigits: 0,
            }).format(returnFee);


            const returnFeeElement = document.getElementById('returnFeeDisplay');
            if (returnFeeElement) {
                returnFeeElement.innerText = formattedReturnFee + ' VND';
            } else {
                console.error("Không tìm thấy phần tử với id 'returnFeeDisplay'");
            }
        })
        .catch(error => {
            console.error("Lỗi khi lấy dữ liệu:", error);
        });
    fetch(`/api/exchangeFee?startDate=${formattedStartDate}&endDate=${formattedEndDate}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Lỗi khi lấy dữ liệu từ API exchangeFee: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Dữ liệu từ API exchangeFee:", data);
            const exchangeFee = data || 0;
            console.log("Phí giao dịch:", exchangeFee);


            const formattedExchangeFee = new Intl.NumberFormat('vi-VN', {
                style: 'decimal',
                minimumFractionDigits: 0,
            }).format(exchangeFee);


            const exchangeFeeElement = document.getElementById('exchangeFeeDisplay');
            if (exchangeFeeElement) {
                exchangeFeeElement.innerText = formattedExchangeFee + ' VND';
            } else {
                console.error("Không tìm thấy phần tử với id 'exchangeFeeDisplay'");
            }
        })
        .catch(error => {
            console.error("Lỗi khi lấy dữ liệu từ exchangeFee:", error);
        });

    fetch(`/api/serviceFee?startDate=${formattedStartDate}&endDate=${formattedEndDate}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Lỗi khi lấy dữ liệu từ API serviceFee: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Dữ liệu từ API serviceFee:", data);
            const serviceFee = data || 0;
            console.log("Phí dịch vụ:", serviceFee);


            const formattedServiceFee = new Intl.NumberFormat('vi-VN', {
                style: 'decimal',
                minimumFractionDigits: 0,
            }).format(serviceFee);


            const serviceFeeElement = document.getElementById('serviceFeeDisplay');
            if (serviceFeeElement) {
                serviceFeeElement.innerText = formattedServiceFee + ' VND';
            } else {
                console.error("Không tìm thấy phần tử với id 'serviceFeeDisplay'");
            }
        })
        .catch(error => {
            console.error("Lỗi khi lấy dữ liệu từ serviceFee:", error);
        });
}

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

document.getElementById("timeFilterForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const today = new Date();


    if (startDate > today || endDate > today) {
        alert("Ngày không được lớn hơn ngày hiện tại.");
        return;
    }


    if (new Date(startDate) > new Date(endDate)) {
        alert("Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc.");
        return;
    }

    const title = (startDate === endDate) ? `Ngày ${startDate}` : `Từ ${startDate} đến ${endDate}`;

    document.getElementById("topProductTitle").textContent = `Top 3 sản phẩm bán chạy ${title}`;
    document.getElementById("topProductExchangeTitle").textContent = `Top 3 sản phẩm đã đổi ${title}`;
    document.getElementById("topProductReturnTitle").textContent = `Top 3 sản phẩm đã trả ${title}`;
    fetchProductSalesInDateRange(startDate, endDate);
    fetchProductSalesExchangeInDateRange(startDate, endDate);
    fetchProductSalesReturnInDateRange(startDate, endDate);
    fetchDataForCharts(startDate, endDate, title);
});

function formatDate(date) {
    return `${String(date.getDate()).padStart(2, '0')}-${String(date.getMonth() + 1).padStart(2, '0')}-${date.getFullYear()}`;
}

async function fetchProductSalesInDateRange(startDate, endDate) {
    try {

        const currentScrollPosition = window.scrollY;
        const response = await fetch(`/api/topProductSalesRenge?startDate=${startDate}&endDate=${endDate}`);

        if (!response.ok) return console.error('Lỗi tải dữ liệu');

        const products = await response.json();
        const productTableBody = document.getElementById("productTableBody");

        productTableBody.innerHTML = '';

        // Nếu không có sản phẩm
        if (!products.length) {
            productTableBody.innerHTML = '<tr><td colspan="5">Không có sản phẩm</td></tr>';
            return;
        }

        // Thêm dữ liệu vào bảng
        products.forEach((item, index) => {
            const row = `
                <tr>
                    <td>${index + 1}</td>
                    <td><img id="product-image-${index}" src="${item.imageUrls[0]}" width="100" height="100"></td>
                    <td>
                        <span>${item.productName}</span><br>
                        <small>Màu: ${item.colorName} <br>
                               Kích thước: ${item.sizeName}
                         </small>
                    </td>
                    <td>${item.originalPrice === item.promotionalPrice
                ? item.originalPrice
                : `<span style="text-decoration: line-through;">${item.originalPrice}</span><br><span style="color: red;">${item.promotionalPrice}</span>`}
                    </td>
                    <td>${item.totalQuantity}</td>
                </tr>
            `;
            productTableBody.insertAdjacentHTML('beforeend', row);

            // Thay đổi ảnh mỗi 10 giây
            let currentImageIndex = 0;
            const imageElement = document.getElementById(`product-image-${index}`);
            setInterval(() => {
                imageElement.src = item.imageUrls[currentImageIndex];
                currentImageIndex = (currentImageIndex + 1) % item.imageUrls.length;
            }, 10000);
        });

        window.scrollTo(0, currentScrollPosition);
    } catch (error) {
        console.error('Error:', error);
    }
}

async function fetchProductSalesExchangeInDateRange(startDate, endDate) {
    try {

        const currentScrollPosition = window.scrollY;
        const response = await fetch(`/api/topProductSalesExchangeRenge?startDate=${startDate}&endDate=${endDate}`);

        if (!response.ok) return console.error('Lỗi tải dữ liệu');

        const products = await response.json();
        const productExchangeTableBody = document.getElementById("productExchangeTableBody");

        productExchangeTableBody.innerHTML = '';
        console.log('dữ liệu exchange', JSON.stringify(products, null, 2));

        // Nếu không có sản phẩm
        if (!products.length) {
            productExchangeTableBody.innerHTML = '<tr><td colspan="5">Không có sản phẩm</td></tr>';
            return;
        }

        // Thêm dữ liệu vào bảng
        products.forEach((item, index) => {
            const row = `
                <tr>
                    <td>${index + 1}</td>
                    <td><img id="product-image-${index}" src="${item.imageUrls[0]}" width="100" height="100"></td>
                    <td>
                        <span>${item.productName}</span><br>
                        <small>Màu: ${item.colorName} <br>
                               Kích thước: ${item.sizeName}
                         </small>
                    </td>
                    <td>${item.originalPrice === item.promotionalPrice
                ? item.originalPrice
                : `<span style="text-decoration: line-through;">${item.originalPrice}</span><br><span style="color: red;">${item.promotionalPrice}</span>`}
                    </td>
                    <td>${item.totalQuantity}</td>
                </tr>
            `;
            productExchangeTableBody.insertAdjacentHTML('beforeend', row);

            // Thay đổi ảnh mỗi 10 giây
            let currentImageIndex = 0;
            const imageElement = document.getElementById(`product-image-${index}`);
            setInterval(() => {
                imageElement.src = item.imageUrls[currentImageIndex];
                currentImageIndex = (currentImageIndex + 1) % item.imageUrls.length;
            }, 10000);
        });

        window.scrollTo(0, currentScrollPosition);
    } catch (error) {
        console.error('Error:', error);
    }
}

async function fetchProductSalesReturnInDateRange(startDate, endDate) {
    try {

        const currentScrollPosition = window.scrollY;
        const response = await fetch(`/api/topProductSalesReturnRenge?startDate=${startDate}&endDate=${endDate}`);

        if (!response.ok) return console.error('Lỗi tải dữ liệu');

        const products = await response.json();
        const productReturnTableBody = document.getElementById("productReturnTableBody");

        productReturnTableBody.innerHTML = '';
        console.log('dữ liệu exchange', JSON.stringify(products, null, 2));

        if (!products.length) {
            productReturnTableBody.innerHTML = '<tr><td colspan="5">Không có sản phẩm</td></tr>';
            return;
        }

        products.forEach((item, index) => {
            const row = `
                <tr>
                    <td>${index + 1}</td>
                    <td><img id="product-image-${index}" src="${item.imageUrls[0]}" width="100" height="100"></td>
                    <td>
                        <span>${item.productName}</span><br>
                        <small>Màu: ${item.colorName} <br>
                               Kích thước: ${item.sizeName}
                         </small>
                    </td>
                    <td>${item.originalPrice === item.promotionalPrice
                ? item.originalPrice
                : `<span style="text-decoration: line-through;">${item.originalPrice}</span><br><span style="color: red;">${item.promotionalPrice}</span>`}
                    </td>
                    <td>${item.totalQuantity}</td>
                </tr>
            `;
            productReturnTableBody.insertAdjacentHTML('beforeend', row);

            let currentImageIndex = 0;
            const imageElement = document.getElementById(`product-image-${index}`);
            setInterval(() => {
                imageElement.src = item.imageUrls[currentImageIndex];
                currentImageIndex = (currentImageIndex + 1) % item.imageUrls.length;
            }, 10000);
        });

        window.scrollTo(0, currentScrollPosition);
    } catch (error) {
        console.error('Error:', error);
    }
}
function formatDateForAPI(date) {
    return new Date(date.getTime() - date.getTimezoneOffset() * 60000) // Bù trừ múi giờ
        .toISOString()
        .split('T')[0]; // Lấy phần ngày
}

function filterTopProducts(filterType) {
    const today = new Date();
    let startDate, endDate;
    let title = "Top 10 sản phẩm bán chạy"; // Tiêu đề mặc định

    switch (filterType) {
        case 'month': // Lọc tháng này
            startDate = new Date(today.getFullYear(), today.getMonth(), 1); // Ngày đầu tháng
            endDate = new Date(today); // Ngày hiện tại
            endDate.setHours(23, 59, 59, 999); // Cuối ngày
            title = `Top 3 sản phẩm bán chạy tháng này`;
            break;

        case 'last7days': // Lọc 7 ngày gần nhất
            startDate = new Date(today);
            startDate.setDate(today.getDate() - 7); // Lấy ngày cách đây 7 ngày
            endDate = new Date(today); // Ngày hiện tại
            endDate.setHours(23, 59, 59, 999); // Cuối ngày
            title = `Top 3 sản phẩm bán chạy trong 7 ngày qua`;
            break;

        case 'year': // Lọc năm hiện tại
            startDate = new Date(today.getFullYear(), 0, 1); // Ngày đầu năm
            endDate = new Date(today); // Ngày hiện tại
            endDate.setHours(23, 59, 59, 999); // Cuối ngày
            title = `Top 3 sản phẩm bán chạy năm ${today.getFullYear()}`;
            break;

        default: // Lọc hôm nay
            startDate = new Date(today);
            endDate = new Date(today);
            endDate.setHours(23, 59, 59, 999); // Cuối ngày
            title = `Top 3 sản phẩm bán chạy hôm nay`;
            break;
    }

    // Cập nhật tiêu đề hiển thị
    document.getElementById('topProductTitle').innerText = title;

    // Gọi API với startDate và endDate đã được định dạng
    fetchProductSalesInDateRange(
        formatDateForAPI(startDate),
        formatDateForAPI(endDate)
    );
}
function filterTopProductsExchange(filterType) {
    const today = new Date();
    let startDate, endDate;
    let title = "Top sản phẩm đã đổi"; // Tiêu đề mặc định

    switch (filterType) {
        case 'month': // Lọc tháng này
            startDate = new Date(today.getFullYear(), today.getMonth(), 1); // Ngày đầu tháng
            endDate = new Date(today); // Ngày hiện tại
            endDate.setHours(23, 59, 59, 999); // Cuối ngày
            title = `Top 3 sản phẩm đã đổi tháng này`;
            break;

        case 'last7days': // Lọc 7 ngày gần nhất
            startDate = new Date(today);
            startDate.setDate(today.getDate() - 7); // Lấy ngày cách đây 7 ngày
            endDate = new Date(today); // Ngày hiện tại
            endDate.setHours(23, 59, 59, 999); // Cuối ngày
            title = `Top 3 sản phẩm đã đổi trong 7 ngày qua`;
            break;

        case 'year': // Lọc năm hiện tại
            startDate = new Date(today.getFullYear(), 0, 1); // Ngày đầu năm
            endDate = new Date(today); // Ngày hiện tại
            endDate.setHours(23, 59, 59, 999); // Cuối ngày
            title = `Top 3 sản phẩm đã đổi năm ${today.getFullYear()}`;
            break;

        default: // Lọc hôm nay
            startDate = new Date(today);
            endDate = new Date(today);
            endDate.setHours(23, 59, 59, 999); // Cuối ngày
            title = `Top 3 sản phẩm đã đổi hôm nay`;
            break;
    }

    // Cập nhật tiêu đề hiển thị
    document.getElementById('topProductExchangeTitle').innerText = title;

    // Gọi API với startDate và endDate đã được định dạng
    fetchProductSalesExchangeInDateRange(
        formatDateForAPI(startDate),
        formatDateForAPI(endDate)
    );

    // Debug: log ra giá trị startDate và endDate
    console.log('Start Date:', formatDateForAPI(startDate));
    console.log('End Date:', formatDateForAPI(endDate));
}

function filterTopProductsReturn(filterType) {
    const today = new Date();
    let startDate, endDate;
    let title = "Top 10 sản phẩm bán chạy";

    switch (filterType) {
        case 'month':
            startDate = new Date(today.getFullYear(), today.getMonth(), 1);
            endDate = new Date(today);
            endDate.setHours(23, 59, 59, 999);
            title = `Top 3 sản phẩm đã trả tháng này`;
            break;

        case 'last7days':
            startDate = new Date(today);
            startDate.setDate(today.getDate() - 7);
            endDate = new Date(today);
            endDate.setHours(23, 59, 59, 999);
            title = `Top 3 sản phẩm đã trả trong 7 ngày qua`;
            break;

        case 'year':
            startDate = new Date(today.getFullYear(), 0, 1);
            endDate = new Date(today);
            endDate.setHours(23, 59, 59, 999);
            title = `Top 3 sản phẩm đã trả năm ${today.getFullYear()}`;
            break;

        default:
            startDate = new Date(today);
            endDate = new Date(today);
            endDate.setHours(23, 59, 59, 999);
            title = `Top 3 sản phẩm đã trả hôm nay`;
            break;
    }

    document.getElementById('topProductReturnTitle').innerText = title;

    fetchProductSalesReturnInDateRange(
        formatDateForAPI(startDate),
        formatDateForAPI(endDate)
    );
}

function fetchDataForCharts(startDate, endDate, title) {
    // Kiểm tra nếu ngày bắt đầu và ngày kết thúc giống nhau
    const formattedStart = formatDate(new Date(startDate));
    const formattedEnd = formatDate(new Date(endDate));
    const columnChartTitle = (startDate === endDate)
        ? `Biểu đồ thống kê sản phẩm và hóa đơn ngày ${formattedStart}`
        : `Biểu đồ thống kê sản phẩm và hóa đơn từ ${formattedStart} đến ${formattedEnd}`;

    const pieChartTitle = (startDate === endDate)
        ? `Trạng thái hóa đơn ngày ${formattedStart}`
        : `Trạng thái hóa đơn từ ${formattedStart} đến ${formattedEnd}`;

    // Fetch dữ liệu cho biểu đồ cột
    fetch(`/api/getStatisticsByDateRange`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ startDate, endDate }),
    })
        .then(response => response.json())
        .then(data => drawColumnChart(data, columnChartTitle))
        .catch(error => console.error('Lỗi dữ liệu biểu đồ cột:', error));

    // Fetch dữ liệu cho biểu đồ tròn
    fetch(`/api/statisticsByRange?startDate=${startDate}&endDate=${endDate}`)
        .then(response => response.json())
        .then(data => drawPieChart(data, pieChartTitle))
        .catch(error => console.error('Lỗi dữ liệu biểu đồ tròn:', error));
}


function drawColumnChart(data, title) {
    const googleData = new google.visualization.DataTable();
    googleData.addColumn('string', 'Ngày');
    googleData.addColumn('number', 'Sản phẩm');
    googleData.addColumn('number', 'Hóa đơn');
    data.forEach(item => googleData.addRow([item.month, item.productCount, item.invoiceCount]));

    const options = {
        title: title, // Tiêu đề biểu đồ cột
        hAxis: { title: 'Thời gian' },
        vAxis: { title: 'Số lượng' },
        seriesType: 'bars'
    };

    new google.visualization.ColumnChart(document.getElementById('chart_div')).draw(googleData, options);
}

function drawPieChart(data, title) {
    const googleData = google.visualization.arrayToDataTable([
        ['Trạng thái', 'Số lượng'],
        ...data.map(item => [item.statusDescription, item.countStatus])
    ]);

    const options = {
        title: title, // Tiêu đề biểu đồ tròn
        is3D: true,
        legend: { position: 'right' }
    };

    new google.visualization.PieChart(document.getElementById('piechart')).draw(googleData, options);
}

document.addEventListener("DOMContentLoaded", function () {
    function loadProductData() {
        fetch(`/api/productSales`)
            .then(response => response.json())
            .then(data => {
                displayProductTable(data);
            })
            .catch(error => {
                console.error('Lỗi khi tải dữ liệu sản phẩm:', error);
            });
    }

    function displayProductTable(data) {
        const productTableBody = document.getElementById('productTableBody');
        productTableBody.innerHTML = '';

        if (data.length === 0) {

            const noDataRow = `
                <tr>
                    <td colspan="5" style="text-align: center;">Không có sản phẩm</td>
                </tr>
            `;
            productTableBody.insertAdjacentHTML('beforeend', noDataRow);
            return;
        }

        data.forEach((item, index) => {
            const row = `
                <tr>
                    <td>${index + 1}</td>
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
        });
    }

    loadProductData();
});



google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(() => filterDataPie('month-pieChart'));


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


function drawChart(chartData, title, period) {
    const pieChartDiv = document.getElementById('piechart');


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

        pieChartDiv.innerHTML = `
            <div class="text-center mt-4">
                <h5>${title}</h5>
                <p>${message}</p>
            </div>`;
        return;
    }

    const data = google.visualization.arrayToDataTable(chartData);


    const options = {
        title: title,
        is3D: true,
    };

    const chart = new google.visualization.PieChart(pieChartDiv);
    chart.draw(data, options);
}

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

