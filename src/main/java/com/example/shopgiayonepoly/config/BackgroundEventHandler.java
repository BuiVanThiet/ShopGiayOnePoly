package com.example.shopgiayonepoly.config;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class BackgroundEventHandler implements IEventHandler {
    private Color backgroundColor;

    // Constructor để truyền vào màu nền
    public BackgroundEventHandler(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), docEvent.getDocument());

        // Đặt màu nền cho toàn bộ trang
        pdfCanvas.setFillColor(backgroundColor);
        pdfCanvas.rectangle(pageSize.getLeft(), pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight());
        pdfCanvas.fill();

        // Thêm logo mờ vào giữa trang
        try {
            InputStream logoStream = getClass().getClassLoader().getResourceAsStream("static/img/logoBill.png");
            ImageData imageData = ImageDataFactory.create(IOUtils.toByteArray(logoStream));
            float logoWidth = pageSize.getWidth() * 1.5f; // Logo sẽ chiếm 50% chiều rộng trang
            float logoHeight = (logoWidth / imageData.getWidth()) * imageData.getHeight(); // Giữ tỷ lệ của logo

            float x = (pageSize.getWidth() - logoWidth) / 2; // Vị trí ngang của logo
            float y = (pageSize.getHeight() - logoHeight) / 2; // Vị trí dọc của logo

            // Lưu trạng thái hiện tại
            pdfCanvas.saveState();

            // Cài đặt độ mờ cho hình ảnh
            PdfExtGState gs = new PdfExtGState();
            gs.setFillOpacity(0.2f); // Đặt độ mờ 20%
            pdfCanvas.setExtGState(gs);

            // Tạo một hình chữ nhật với vị trí và kích thước logo để thêm logo vào PdfCanvas
            Rectangle logoRect = new Rectangle(-190, -100, logoWidth, logoHeight);

            // Thêm hình ảnh logo vào PdfCanvas
            pdfCanvas.addImageFittedIntoRectangle(imageData, logoRect, false);

            // Khôi phục trạng thái gốc sau khi thêm logo
            pdfCanvas.restoreState();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
