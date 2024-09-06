// Biến toàn cục để lưu đối tượng codeReader
let codeReader = null;
let idBill = null;
// Lấy IdBill từ session thông qua API
fetch('/bill-api/get-idbill')
    .then(response => response.json())
    .then(data => {
        idBill = data; // Lưu giá trị IdBill vào biến toàn cục
    })
    .catch(error => console.error('Error fetching IdBill:', error));
// Lắng nghe sự kiện click trên nút "Use Camera"
document.getElementById('startCamera').addEventListener('click', () => {
    // Tạo một đối tượng BrowserQRCodeReader từ thư viện ZXing
    codeReader = new ZXing.BrowserQRCodeReader();

    // Lấy danh sách các thiết bị đầu vào video
    codeReader.listVideoInputDevices().then(videoInputDevices => {
        if (videoInputDevices.length > 0) {
            // Chọn thiết bị video đầu tiên
            const firstDeviceId = videoInputDevices[0].deviceId;

            // Bắt đầu quét mã QR từ thiết bị video
            codeReader.decodeFromVideoDevice(firstDeviceId, 'video', (result, error) => {
                if (result) {
                    // Xử lý dữ liệu QR code nếu quét thành công
                    const qrData = JSON.parse(result.text);
                    console.log('QR code detected: ', qrData);

                    // Gửi dữ liệu QR code đến server
                    //   cần để ý thay đổi phần rest api này '/qr/scan'
                    fetch('/bill-api/addProductByQr', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(qrData)
                    })
                        .then(response => {
                            if (response.ok) {
                                // Chuyển hướng đến trang hiển thị sau khi gửi dữ liệu thành công
                                //cần để ý để thay đổi cả đường dẫn trang
                                window.location.href = '/bill/bill-detail/'+idBill;
                            } else {
                                console.error('Failed to send QR data.');
                            }
                        })
                        .catch(error => console.error('Error:', error));
                }
                if (error) {
                    console.error(error);
                }
            });
        } else {
            console.error('No video input devices found.');
        }
    }).catch(err => console.error(err));
});

// Dừng camera khi modal bị đóng
const modalElement = document.getElementById('camera-Modal');
modalElement.addEventListener('hidden.bs.modal', () => {
    if (codeReader) {
        // Dừng quét QR code và giải phóng tài nguyên
        codeReader.reset();
        codeReader = null;
    }
});