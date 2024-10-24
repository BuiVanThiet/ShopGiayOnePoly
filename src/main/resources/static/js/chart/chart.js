const buttons = document.querySelectorAll('.custom-btn');

// Thêm sự kiện click cho mỗi nút
buttons.forEach(button => {
    button.addEventListener('click', function() {
        // Xóa lớp 'active' khỏi tất cả các nút
        buttons.forEach(btn => btn.classList.remove('active'));
        // Thêm lớp 'active' vào nút được nhấn
        this.classList.add('active');
    });
});

window.onload = function() {
    var dps = [[], []];
    var chart = new CanvasJS.Chart("chartContainer", {
        exportEnabled: true,
        animationEnabled: true,
        theme: "white",
        backgroundColor: "#EEEEEE",
        title: {
            text: "Biểu đồ thống kê hóa đơn và sản phẩm",
            fontFamily: "Times New Roman",  // Thay đổi kiểu chữ tại đây
            fontSize: 24,         // Kích thước chữ
            fontWeight: "bold",   // Đậm
            color: "#333"
        },
        axisY: {
            title: "Số lượng",
            titleFontColor: "#4F81BC",
            lineColor: "#4F81BC",
            labelFontColor: "#4F81BC",
            tickColor: "#4F81BC",
            includeZero: true,
            prefix: "$"
        },
        axisY2: {
            title: "Clicks",
            titleFontColor: "#C0504E",
            lineColor: "#C0504E",
            labelFontColor: "#C0504E",
            tickColor: "#C0504E"
        },
        toolTip: {
            shared: true
        },
        legend: {
            cursor: "pointer",
            itemclick: toggleDataSeries
        },
        data: [{
            type: "column",
            name: "Cost Per Click",
            showInLegend: true,
            yValueFormatString: "$#,##0.00",
            dataPoints: dps[0]
        },
            {
                type: "column",
                name: "Click",
                axisYType: "secondary",
                showInLegend: true,
                dataPoints: dps[1]
            }]
    });

    var yValue;
    var label;

    /* Vòng lặp để thêm điểm dữ liệu từ dataPointsList */
    /* Chuyển mã từ Thymeleaf sang JS */
    /* Giả sử bạn đã thiết lập dataPointsList đúng cách */
    /* Dữ liệu này sẽ cần phải được chuyển từ Thymeleaf vào JS */

    /* Tương tự như trước, sử dụng th:block nếu cần */
    //...

    chart.render();

    function toggleDataSeries(e) {
        if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
            e.dataSeries.visible = false;
        } else {
            e.dataSeries.visible = true;
        }
        e.chart.render();
    }
}
