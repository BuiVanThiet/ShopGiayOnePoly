package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.config.BackgroundEventHandler;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class PdfTemplateService {
    //cai nay dung de tao hoa don
public ByteArrayOutputStream fillPdfTemplate(Object[] bill,List<Object[]> productList) throws Exception {
    // Tạo ByteArrayOutputStream để lưu PDF mới
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    // Tạo PdfWriter và PdfDocument
    PdfWriter writer = new PdfWriter(byteArrayOutputStream);
    PdfDocument pdfDoc = new PdfDocument(writer);
    Document document = new Document(pdfDoc);
    // Thêm trang mới trước khi thêm nội dung
    pdfDoc.addNewPage();
    // Thêm trang đầu tiên vào PDF
    Color textColor = ColorConstants.BLACK;
    // Thêm font hỗ trợ Unicode cho văn bản tiếp theo
    InputStream fontStream = getClass().getClassLoader().getResourceAsStream("static/fornText/Roboto-Regular.ttf");

    PdfFont font = PdfFontFactory.createFont(IOUtils.toByteArray(fontStream), PdfEncodings.IDENTITY_H);
    // Đường dẫn đến file logo
    InputStream logoStream = getClass().getClassLoader().getResourceAsStream("static/img/logoBill.png");
    ImageData imageData = ImageDataFactory.create(IOUtils.toByteArray(logoStream));
    Image logo = new Image(imageData);
    logo.setWidth(160).setHeight(160); // Điều chỉnh kích thước logo cho phù hợp
    logo.setFixedPosition(20, 700); // Đặt logo phía trên bên trái
// Thêm màu nền cho trang bằng mã màu #f8f4ec
    PdfPage page = pdfDoc.getFirstPage();
    Rectangle pageSize = page.getPageSize();
    PdfCanvas pdfCanvas = new PdfCanvas(page);
    pdfCanvas.setFillColor(new DeviceRgb(248, 244, 236));  // Màu #f8f4ec
    pdfCanvas.rectangle(pageSize.getLeft(), pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight());
    pdfCanvas.fill();
    // Thêm màu nền cho trang bằng mã màu #f8f4ec
    Color backgroundColor = new DeviceRgb(248, 244, 236);  // Màu #f8f4ec
    // Đăng ký event handler để áp dụng màu nền cho tất cả các trang
    pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new BackgroundEventHandler(backgroundColor));
    Image logoPDF = new Image(imageData);
    // Lấy kích thước trang hiện tại
    Rectangle pageSizepdf = pdfDoc.getDefaultPageSize();
// Đặt kích thước logo phù hợp với kích thước trang (ví dụ, logo chiếm 50% chiều rộng trang)
    logoPDF.setWidth(pageSizepdf.getWidth() * 1.5f);  // Logo sẽ chiếm 50% chiều rộng trang
// Đặt logo ở giữa trang
    logoPDF.setFixedPosition(-190, -100);
// Thiết lập độ mờ cho logo (0.0 là hoàn toàn trong suốt, 1.0 là không trong suốt)
    logoPDF.setOpacity(0.2f);  // Làm cho logo hơi mờ
// Thêm logo vào tài liệu (trước các nội dung khác để nó nằm phía sau)
    document.add(logoPDF);
    // Tạo tiêu đề với tên cửa hàng
    Paragraph header = new Paragraph("OnePoly")
            .setFont(font)
            .setBold()
            .setFontSize(16)
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.CENTER);
    header.setFixedPosition(200, 780, 200); // Vị trí tiêu đề
    // Địa chỉ cửa hàng
    Paragraph address = new Paragraph("Đ/C: \n" +
            "P. Kiều Mai, Phúc Diễn, Bắc Từ Liêm, Hà Nội")
            .setFont(font)
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(textColor);
    address.setFixedPosition(200, 740, 200); // Điều chỉnh vị trí cho phù hợp
    Paragraph title = new Paragraph("Hóa đơn")
            .setFont(font)
            .setBold()
            .setFontSize(16)
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.CENTER);
    title.setFixedPosition(200, 710, 200); // Vị trí tiêu đề
    // Thêm logo, tiêu đề và địa chỉ vào document
    document.add(logo);
    document.add(header);
    document.add(title);
    document.add(address);
    document.add(new Paragraph("").setMarginTop(50)); // Thêm khoảng cách 10 đơn vị
    // Thông tin khách hàng và hóa đơn
    // Tạo đối tượng Paragraph
    Paragraph customerInfo = new Paragraph()
            .setFont(font)
            .setFontSize(12)
            .setFontColor(textColor);
    document.add(new Paragraph("").setMarginTop(20)); // Thêm khoảng cách 10 đơn vị
// Thêm TabStops và nội dung vào Paragraph
    customerInfo.addTabStops(new TabStop(100, TabAlignment.LEFT)); // Căn trái tại vị trí 100
    customerInfo.add("Khách hàng:");
    customerInfo.add(new Tab());
    customerInfo.add(bill[2] + "\n");

    customerInfo.add("Số điện thoại:");
    customerInfo.add(new Tab());
    customerInfo.add(bill[3] + "\n");

    customerInfo.add("Email:");
    customerInfo.add(new Tab());
    customerInfo.add(bill[4] + "\n");

    customerInfo.add("Địa chỉ:");
    customerInfo.add(new Tab());
    customerInfo.add(bill[5] + "\n");

// Thêm Paragraph vào tài liệu PDF
    document.add(customerInfo);

    Paragraph invoiceInfo = new Paragraph("Hóa đơn: "+ bill[0]+"\n"
            + "Ngày tạo: "+ bill[1])
            .setFont(font)
            .setFontSize(12)
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginTop(-70); // Canh lề phải cho phần thông tin hóa đơn
    document.add(invoiceInfo);
    document.add(new Paragraph("").setMarginTop(20)); // Thêm khoảng cách 10 đơn vị
    // Thêm bảng sản phẩm mua
    float[] columnWidths = {2,4, 1, 2, 2}; // Chiều rộng các cột: Tên sản phẩm, Số lượng, Đơn giá, Thành tiền
    Table table = new Table(columnWidths);
    table.setWidth(UnitValue.createPercentValue(100)); // Bảng chiếm 100% chiều ngang
    // Thêm tiêu đề cho các cột
    table.addHeaderCell(new Cell().add(new Paragraph("STT").setFontColor(textColor).setFont(font).setBold()));
    table.addHeaderCell(new Cell().add(new Paragraph("Sản phẩm").setFontColor(textColor).setFont(font).setBold()));
    table.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setFontColor(textColor).setFont(font).setBold()));
    table.addHeaderCell(new Cell().add(new Paragraph("Đơn giá").setFontColor(textColor).setFont(font).setBold()));
    table.addHeaderCell(new Cell().add(new Paragraph("Thành tiền").setFontColor(textColor).setFont(font).setBold()));
    int stt = 1;
    // Thêm các sản phẩm vào bảng
    for (Object[] product : productList) {
        String productName = ((String) product[0]).replace("--", "\n"); // Chuyển đổi dấu '--' thành xuống dòng
////        Integer quantity = (Integer) product[1];  // Số lượng
//        Double unitPrice = (Double) product[2];   // Đơn giá
//        Double totalPrice = (Double) product[4];  // Thành tiền
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(String.valueOf(stt)).setFont(font)));
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(productName).setFont(font)));
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[1].toString()).setFont(font)));
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[3].toString()).setFont(font)));
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[4].toString()).setFont(font)));
        stt++;
    }
// Thêm bảng vào document
    document.add(table);
    document.add(new Paragraph("").setMarginTop(50)); // Thêm khoảng cách 10 đơn vị
    Paragraph total = new Paragraph("Tổng tiền sản phẩm: "+ bill[6]+"\n"
            + "Phí vận chuyển: "+ bill[7]+"\n" +
            "Giảm giá: "+ bill[8]+"\n" +
            "Tổng tiền hóa đơn: "+ bill[9]+"\n")
            .setFont(font)
            .setFontSize(12)
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginTop(-50); // Canh lề phải cho phần thông tin hóa đơn
    document.add(total);
    // Tạo bảng chữ ký khách hàng và nhân viên
    float[] columSignature = {2, 2}; // Hai cột có cùng kích thước
    Table signatureTable = new Table(columSignature);
    signatureTable.setWidth(UnitValue.createPercentValue(100)); // Bảng chiếm 100% chiều ngang
    // Thêm cột chữ ký khách hàng
    Cell customerSignatureCell = new Cell();
    customerSignatureCell.add(new Paragraph("Chữ ký khách hàng")
            .setFont(font)
            .setBold()
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20)); // Khoảng cách trước khi chữ ký
    customerSignatureCell.setFontColor(textColor).add(new Paragraph("___________________________") // Dòng trống cho chữ ký
            .setTextAlignment(TextAlignment.CENTER));
    signatureTable.addCell(customerSignatureCell);
    // Thêm cột chữ ký nhân viên
    Cell staffSignatureCell = new Cell();
    staffSignatureCell.add(new Paragraph("Chữ ký nhân viên")
            .setFont(font)
            .setBold()
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20)); // Khoảng cách trước khi chữ ký
    staffSignatureCell.setFontColor(textColor).add(new Paragraph("___________________________") // Dòng trống cho chữ ký
            .setTextAlignment(TextAlignment.CENTER));
    signatureTable.addCell(staffSignatureCell);
    // Thêm bảng chữ ký vào document
    document.add(signatureTable);
    // Đóng document
    document.close();
    return byteArrayOutputStream;
}

//cai nay dung de tao don doitra
public ByteArrayOutputStream fillPdfReturnExchangeTemplate(Object[] bill,List<Object[]> productReturn,List<Object[]> productExchange) throws Exception {
    // Tạo ByteArrayOutputStream để lưu PDF mới
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    // Tạo PdfWriter và PdfDocument
    PdfWriter writer = new PdfWriter(byteArrayOutputStream);
    PdfDocument pdfDoc = new PdfDocument(writer);
    Document document = new Document(pdfDoc);
    // Thêm trang mới trước khi thêm nội dung
    pdfDoc.addNewPage();
    // Thêm trang đầu tiên vào PDF
    Color textColor = ColorConstants.BLACK;
    // Thêm font hỗ trợ Unicode cho văn bản tiếp theo
    InputStream fontStream = getClass().getClassLoader().getResourceAsStream("static/fornText/Roboto-Regular.ttf");

    PdfFont font = PdfFontFactory.createFont(IOUtils.toByteArray(fontStream), PdfEncodings.IDENTITY_H);
    // Đường dẫn đến file logo
    InputStream logoStream = getClass().getClassLoader().getResourceAsStream("static/img/logoBill.png");
    ImageData imageData = ImageDataFactory.create(IOUtils.toByteArray(logoStream));
    Image logo = new Image(imageData);
    logo.setWidth(160).setHeight(160); // Điều chỉnh kích thước logo cho phù hợp
    logo.setFixedPosition(20, 700); // Đặt logo phía trên bên trái
// Thêm màu nền cho trang bằng mã màu #f8f4ec
    PdfPage page = pdfDoc.getFirstPage();
    Rectangle pageSize = page.getPageSize();
    PdfCanvas pdfCanvas = new PdfCanvas(page);
    pdfCanvas.setFillColor(new DeviceRgb(248, 244, 236));  // Màu #f8f4ec
    pdfCanvas.rectangle(pageSize.getLeft(), pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight());
    pdfCanvas.fill();
    // Thêm màu nền cho trang bằng mã màu #f8f4ec
    Color backgroundColor = new DeviceRgb(248, 244, 236);  // Màu #f8f4ec
    // Đăng ký event handler để áp dụng màu nền cho tất cả các trang
    pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new BackgroundEventHandler(backgroundColor));
    Image logoPDF = new Image(imageData);
    // Lấy kích thước trang hiện tại
    Rectangle pageSizepdf = pdfDoc.getDefaultPageSize();
// Đặt kích thước logo phù hợp với kích thước trang (ví dụ, logo chiếm 50% chiều rộng trang)
    logoPDF.setWidth(pageSizepdf.getWidth() * 1.5f);  // Logo sẽ chiếm 50% chiều rộng trang
// Đặt logo ở giữa trang
    logoPDF.setFixedPosition(-190, -100);
// Thiết lập độ mờ cho logo (0.0 là hoàn toàn trong suốt, 1.0 là không trong suốt)
    logoPDF.setOpacity(0.2f);  // Làm cho logo hơi mờ
// Thêm logo vào tài liệu (trước các nội dung khác để nó nằm phía sau)
    document.add(logoPDF);
    // Tạo tiêu đề với tên cửa hàng
    Paragraph header = new Paragraph("OnePoly")
            .setFont(font)
            .setBold()
            .setFontSize(16)
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.CENTER);
    header.setFixedPosition(200, 780, 200); // Vị trí tiêu đề
    // Địa chỉ cửa hàng
    Paragraph address = new Paragraph("Đ/C: \n" +
            "P. Kiều Mai, Phúc Diễn, Bắc Từ Liêm, Hà Nội")
            .setFont(font)
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(textColor);
    address.setFixedPosition(200, 740, 200); // Điều chỉnh vị trí cho phù hợp
    Paragraph title = new Paragraph("Phiếu đổi - trả sản phẩm")
            .setFont(font)
            .setBold()
            .setFontSize(16)
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.CENTER);
    title.setFixedPosition(200, 710, 200); // Vị trí tiêu đề
    // Thêm logo, tiêu đề và địa chỉ vào document
    document.add(logo);
    document.add(header);
    document.add(title);
    document.add(address);
    document.add(new Paragraph("").setMarginTop(50)); // Thêm khoảng cách 10 đơn vị
    // Thông tin khách hàng và hóa đơn
    // Tạo đối tượng Paragraph
    Paragraph customerInfo = new Paragraph()
            .setFont(font)
            .setFontSize(12)
            .setFontColor(textColor);
    document.add(new Paragraph("").setMarginTop(20)); // Thêm khoảng cách 10 đơn vị
// Thêm TabStops và nội dung vào Paragraph
    customerInfo.addTabStops(new TabStop(100, TabAlignment.LEFT)); // Căn trái tại vị trí 100
    customerInfo.add("Khách hàng:");
    customerInfo.add(new Tab());
    customerInfo.add(bill[4] + "\n");

    customerInfo.add("Số điện thoại:");
    customerInfo.add(new Tab());
    customerInfo.add(bill[6] + "\n");

    customerInfo.add("Email:");
    customerInfo.add(new Tab());
    customerInfo.add(bill[5] + "\n");

    customerInfo.add("Địa chỉ:");
    customerInfo.add(new Tab());
    customerInfo.add(bill[7] + "\n");

// Thêm Paragraph vào tài liệu PDF
    document.add(customerInfo);

    Paragraph invoiceInfo = new Paragraph("Hóa đơn: "+ bill[2]+"\n"
            + "Ngày tạo: "+ bill[10])
            .setFont(font)
            .setFontSize(12)
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginTop(-70); // Canh lề phải cho phần thông tin hóa đơn
    document.add(invoiceInfo);
    document.add(new Paragraph("").setMarginTop(20)); // Thêm khoảng cách 10 đơn vị
    // Thêm bảng sản phẩm mua
    float[] columnWidths = {2,4, 1, 2, 2}; // Chiều rộng các cột: Tên sản phẩm, Số lượng, Đơn giá, Thành tiền
    Table table = new Table(columnWidths);
    table.setWidth(UnitValue.createPercentValue(100)); // Bảng chiếm 100% chiều ngang
    // Thêm tiêu đề cho các cột
    table.addHeaderCell(new Cell().add(new Paragraph("STT").setFontColor(textColor).setFont(font).setBold()));
    table.addHeaderCell(new Cell().add(new Paragraph("Sản phẩm").setFontColor(textColor).setFont(font).setBold()));
    table.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setFontColor(textColor).setFont(font).setBold()));
    table.addHeaderCell(new Cell().add(new Paragraph("Đơn giá").setFontColor(textColor).setFont(font).setBold()));
    table.addHeaderCell(new Cell().add(new Paragraph("Thành tiền").setFontColor(textColor).setFont(font).setBold()));
    int stt = 1;
    // Thêm các sản phẩm vào bảng
    for (Object[] product : productReturn) {
        String productName = ((String) product[0]).replace("--", "\n");// Tên sản phẩm
////        Integer quantity = (Integer) product[1];  // Số lượng
//        Double unitPrice = (Double) product[2];   // Đơn giá
//        Double totalPrice = (Double) product[4];  // Thành tiền
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(String.valueOf(stt)).setFont(font)));
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(productName).setFont(font)));
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[1].toString()).setFont(font)));
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[3].toString()).setFont(font)));
        table.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[4].toString()).setFont(font)));
        stt++;
    }
// Thêm bảng vào document
    // ở đây là nơi có chữ "Sản phẩm trả" cho ra giữa hàng này
    Paragraph productTitleReturn = new Paragraph("Sản phẩm trả")
            .setFont(font)
            .setFontSize(12)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(textColor);
    document.add(productTitleReturn);
    document.add(table);
    // bnage 2
    document.add(new Paragraph("").setMarginTop(20)); // Thêm khoảng cách 10 đơn vị
    // Thêm bảng sản phẩm mua
    // Kiểm tra nếu có bảng "Sản phẩm đổi" thì sang trang mới
    if (productExchange != null && !productExchange.isEmpty()) {
        // Thêm tiêu đề "Sản phẩm đổi"
        Paragraph productTitleExchange = new Paragraph("Sản phẩm đổi")
                .setFont(font)
                .setFontSize(12)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(textColor);
        document.add(productTitleExchange);

        // Tạo và thêm bảng sản phẩm đổi
        float[] columnWidths2 = {2,4, 1, 2, 2}; // Chiều rộng các cột: Tên sản phẩm, Số lượng, Đơn giá, Thành tiền
        Table table2 = new Table(columnWidths2);
        table2.setWidth(UnitValue.createPercentValue(100)); // Bảng chiếm 100% chiều ngang

        // Thêm tiêu đề cho các cột
        table2.addHeaderCell(new Cell().add(new Paragraph("STT").setFontColor(textColor).setFont(font).setBold()));
        table2.addHeaderCell(new Cell().add(new Paragraph("Sản phẩm").setFontColor(textColor).setFont(font).setBold()));
        table2.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setFontColor(textColor).setFont(font).setBold()));
        table2.addHeaderCell(new Cell().add(new Paragraph("Đơn giá").setFontColor(textColor).setFont(font).setBold()));
        table2.addHeaderCell(new Cell().add(new Paragraph("Thành tiền").setFontColor(textColor).setFont(font).setBold()));

        int stt2 = 1;
        // Thêm các sản phẩm vào bảng
        for (Object[] product : productExchange) {
            String productName = ((String) product[0]).replace("--", "\n"); // Tên sản phẩm
            table2.setFontColor(textColor).addCell(new Cell().add(new Paragraph(String.valueOf(stt2)).setFont(font)));
            table2.setFontColor(textColor).addCell(new Cell().add(new Paragraph(productName).setFont(font)));
            table2.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[1].toString()).setFont(font)));
            table2.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[3].toString()).setFont(font)));
            table2.setFontColor(textColor).addCell(new Cell().add(new Paragraph(product[4].toString()).setFont(font)));
            stt2++;
        }

        // Thêm bảng vào document
        document.add(table2);
    }

    double value1 = ((Number) bill[8]).doubleValue();
    double value2 = ((Number) bill[12]).doubleValue();
    double value3 = ((Number) bill[13]).doubleValue();
    double result = (value1 - value3) - value2;
    double result2 = value2 - (value1 - value3);

    document.add(new Paragraph("").setMarginTop(50)); // Thêm khoảng cách 10 đơn vị
    Paragraph total = new Paragraph("Tổng tiền hóa đơn trả: "+ String.format("%,.2f VNĐ", bill[8])+"\n"
            + "Tổng tiền hóa đơn đổi: "+ String.format("%,.2f VNĐ", bill[12])+"\n" +
            "Phí tạo phiếu: "+ String.format("%,.2f VNĐ", bill[13])+"\n" +
            "Giảm giá(chỉ dành cho khi đổi): "+ String.format("%,.2f VNĐ", bill[14])+"\n"+
            "Tổng cộng trả khách: "+ (result < 0 ? "0 VNĐ" : String.format("%,.2f VNĐ", result))+"\n" +
            "Tổng cộng khách trả: "+ (result2 < 0 ? "0 VNĐ" : String.format("%,.2f VNĐ", result2)) +"\n" )
            .setFont(font)
            .setFontSize(12)
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginTop(-50); // Canh lề phải cho phần thông tin hóa đơn
    document.add(total);
    // Tạo bảng chữ ký khách hàng và nhân viên
    float[] columSignature = {2, 2}; // Hai cột có cùng kích thước
    Table signatureTable = new Table(columSignature);
    signatureTable.setWidth(UnitValue.createPercentValue(100)); // Bảng chiếm 100% chiều ngang
    // Thêm cột chữ ký khách hàng
    Cell customerSignatureCell = new Cell();
    customerSignatureCell.add(new Paragraph("Chữ ký khách hàng")
            .setFont(font)
            .setBold()
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20)); // Khoảng cách trước khi chữ ký
    customerSignatureCell.setFontColor(textColor).add(new Paragraph("___________________________") // Dòng trống cho chữ ký
            .setTextAlignment(TextAlignment.CENTER));
    signatureTable.addCell(customerSignatureCell);
    // Thêm cột chữ ký nhân viên
    Cell staffSignatureCell = new Cell();
    staffSignatureCell.add(new Paragraph("Chữ ký nhân viên")
            .setFont(font)
            .setBold()
            .setFontColor(textColor)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20)); // Khoảng cách trước khi chữ ký
    staffSignatureCell.setFontColor(textColor).add(new Paragraph("___________________________") // Dòng trống cho chữ ký
            .setTextAlignment(TextAlignment.CENTER));
    signatureTable.addCell(staffSignatureCell);
    // Thêm bảng chữ ký vào document
    document.add(signatureTable);
    // Đóng document
    document.close();
    return byteArrayOutputStream;
}
}