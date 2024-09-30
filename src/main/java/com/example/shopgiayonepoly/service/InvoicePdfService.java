package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.entites.Bill;
import com.example.shopgiayonepoly.entites.BillDetail;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class InvoicePdfService {
    public void createPdf(String fileName) throws DocumentException, IOException {
        Document document = new Document();
        String filePath = "D:\\" + fileName + ".pdf";

        // Tạo PdfWriter và liên kết với document
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Thêm thông tin cửa hàng
        Font fontTitle = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph storeName = new Paragraph("SNEAKER BEESHOES", fontTitle);
        storeName.setAlignment(Element.ALIGN_CENTER);
        document.add(storeName);

        Paragraph storeInfo = new Paragraph("Địa chỉ: Số X, Xã Tân Quang, Hưng Yên\n");
        storeInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(storeInfo);

        // Thêm logo (tuỳ chỉnh đường dẫn)
        Image logo = Image.getInstance("D:\\DuAnTotNghiep\\ShopGiayOnePoly\\src\\main\\resources\\static\\img\\1.jpg");
        logo.setAlignment(Element.ALIGN_CENTER);
        document.add(logo);

        document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));
        document.add(new Paragraph("Mã hóa đơn: HD123456"));

        // Thêm bảng sản phẩm
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        table.addCell("Tên sản phẩm");
        table.addCell("Mã sản phẩm");
        table.addCell("Số lượng");
        table.addCell("Đơn giá");
        table.addCell("Thành tiền");

        table.addCell("Giày MLB Chunky Wide New York");
        table.addCell("123456");
        table.addCell("2");
        table.addCell("100,000");
        table.addCell("200,000");

        document.add(table);

        // Thêm mã QR
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode("http://example.com", 100, 100, null);
        Image codeQrImage = barcodeQRCode.getImage();
        codeQrImage.setAbsolutePosition(450, 100); // Điều chỉnh vị trí QR code
        codeQrImage.scaleAbsolute(100, 100);
        document.add(codeQrImage);

        // Thêm mã vạch (Barcode 128)
        Barcode128 barcode = new Barcode128();
        barcode.setCode("HD123456");
        Image code128Image = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);  // Sử dụng writer.getDirectContent()
        code128Image.setAbsolutePosition(30, 100); // Điều chỉnh vị trí mã vạch
        code128Image.scaleAbsolute(100, 50);
        document.add(code128Image);

        // Đóng tài liệu PDF
        document.close();
    }
    public void createPdfByBill(Bill bill, List billDetailList) throws DocumentException, IOException {
        Document document = new Document();
        String filePath = "D:\\" + bill.getCodeBill() + ".pdf";

        // Tạo PdfWriter và liên kết với document
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Thêm thông tin cửa hàng
        Font fontTitle = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph storeName = new Paragraph("OnePoly Snaker", fontTitle);
        storeName.setAlignment(Element.ALIGN_CENTER);
        document.add(storeName);

        Paragraph storeInfo = new Paragraph("Địa chỉ: Số X, Xã Tân Quang, Hưng Yên\n");
        storeInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(storeInfo);

        // Thêm logo (tuỳ chỉnh đường dẫn)
        Image logo = Image.getInstance("D:\\DuAnTotNghiep\\ShopGiayOnePoly\\src\\main\\resources\\static\\img\\1.jpg");
        logo.setAlignment(Element.ALIGN_CENTER);
        document.add(logo);

        document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));
        document.add(new Paragraph("Mã hóa đơn: HD123456"));

        // Thêm bảng sản phẩm
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        table.addCell("Tên sản phẩm");
        table.addCell("Mã sản phẩm");
        table.addCell("Số lượng");
        table.addCell("Đơn giá");
        table.addCell("Thành tiền");

        table.addCell("Giày MLB Chunky Wide New York");
        table.addCell("123456");
        table.addCell("2");
        table.addCell("100,000");
        table.addCell("200,000");

        document.add(table);

        // Thêm mã QR
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode("http://example.com", 100, 100, null);
        Image codeQrImage = barcodeQRCode.getImage();
        codeQrImage.setAbsolutePosition(450, 100); // Điều chỉnh vị trí QR code
        codeQrImage.scaleAbsolute(100, 100);
        document.add(codeQrImage);

        // Thêm mã vạch (Barcode 128)
        Barcode128 barcode = new Barcode128();
        barcode.setCode("HD123456");
        Image code128Image = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);  // Sử dụng writer.getDirectContent()
        code128Image.setAbsolutePosition(30, 100); // Điều chỉnh vị trí mã vạch
        code128Image.scaleAbsolute(100, 50);
        document.add(code128Image);

        // Đóng tài liệu PDF
        document.close();
    }
}
