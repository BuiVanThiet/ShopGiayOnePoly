package com.example.shopgiayonepoly.baseMethod;

import com.example.shopgiayonepoly.entites.Token;
import com.example.shopgiayonepoly.service.EmailSenderService;
import com.example.shopgiayonepoly.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

public abstract class BaseEmail {
    @Autowired
    protected EmailSenderService emailSenderService;
    @Autowired
    protected TokenService tokenService;
    protected String templateEmailExchangePassWord(String emailSend,String hosst) {
        String receiverEmail = emailSend;
        String subject = "Đổi mật khẩu!";
        String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"format-detection\" content=\"telephone=no\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Đây là email từ hệ thống cửa hàng giày one poly</title><style type=\"text/css\" emogrify=\"no\">#outlook a { padding:0; } .ExternalClass { width:100%; } .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div { line-height: 100%; } table td { border-collapse: collapse; mso-line-height-rule: exactly; } .editable.image { font-size: 0 !important; line-height: 0 !important; } .nl2go_preheader { display: none !important; mso-hide:all !important; mso-line-height-rule: exactly; visibility: hidden !important; line-height: 0px !important; font-size: 0px !important; } body { width:100% !important; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%; margin:0; padding:0; } img { outline:none; text-decoration:none; -ms-interpolation-mode: bicubic; } a img { border:none; } table { border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt; } th { font-weight: normal; text-align: left; } *[class=\"gmail-fix\"] { display: none !important; } </style><style type=\"text/css\" emogrify=\"no\"> @media (max-width: 600px) { .gmx-killpill { content: ' \\03D1';} } </style><style type=\"text/css\" emogrify=\"no\">@media (max-width: 600px) { .gmx-killpill { content: ' \\03D1';} .r0-o { border-style: solid !important; margin: 0 auto 0 auto !important; width: 320px !important } .r1-i { background-color: #ffffff !important } .r2-c { box-sizing: border-box !important; text-align: center !important; valign: top !important; width: 100% !important } .r3-o { border-style: solid !important; margin: 0 auto 0 auto !important; width: 100% !important } .r4-i { padding-bottom: 20px !important; padding-left: 0px !important; padding-right: 0px !important; padding-top: 20px !important } .r5-c { box-sizing: border-box !important; display: block !important; valign: top !important; width: 100% !important } .r6-o { border-style: solid !important; width: 100% !important } .r7-i { padding-left: 0px !important; padding-right: 0px !important } .r8-c { box-sizing: border-box !important; text-align: left !important; valign: top !important; width: 100% !important } .r9-o { border-style: solid !important; margin: 0 auto 0 0 !important; width: 100% !important } .r10-i { padding-top: 15px !important; text-align: center !important } .r11-c { box-sizing: border-box !important; padding-bottom: 15px !important; padding-left: 0px !important; padding-right: 0px !important; padding-top: 15px !important; text-align: center !important; valign: top !important; width: 100% !important } .r12-c { box-sizing: border-box !important; padding-bottom: 15px !important; padding-top: 15px !important; text-align: left !important; valign: top !important; width: 100% !important } .r13-c { box-sizing: border-box !important; padding: 0 !important; text-align: center !important; valign: top !important; width: 100% !important } .r14-o { border-style: solid !important; margin: 0 auto 0 auto !important; margin-bottom: 15px !important; margin-top: 15px !important; width: 100% !important } .r15-i { padding: 0 !important; text-align: center !important } .r16-r { background-color: #1B1B1B !important; border-radius: 4px !important; border-width: 0px !important; box-sizing: border-box; height: initial !important; padding: 0 !important; padding-bottom: 12px !important; padding-left: 5px !important; padding-right: 5px !important; padding-top: 12px !important; text-align: center !important; width: 100% !important } .r17-i { background-color: #eff2f7 !important; padding-bottom: 20px !important; padding-left: 15px !important; padding-right: 15px !important; padding-top: 20px !important } .r18-i { padding-bottom: 0px !important; padding-top: 15px !important; text-align: center !important } .r19-i { padding-bottom: 0px !important; padding-top: 0px !important; text-align: center !important } .r20-c { box-sizing: border-box !important; text-align: center !important; width: 100% !important } .r21-i { padding-bottom: 15px !important; padding-left: 0px !important; padding-right: 0px !important; padding-top: 0px !important } .r22-c { box-sizing: border-box !important; text-align: center !important; valign: top !important; width: 129px !important } .r23-o { border-style: solid !important; margin: 0 auto 0 auto !important; width: 129px !important } body { -webkit-text-size-adjust: none } .nl2go-responsive-hide { display: none } .nl2go-body-table { min-width: unset !important } .mobshow { height: auto !important; overflow: visible !important; max-height: unset !important; visibility: visible !important } .resp-table { display: inline-table !important } .magic-resp { display: table-cell !important } } </style><style type=\"text/css\">p, h1, h2, h3, h4, ol, ul, li { margin: 0; } a, a:link { color: #696969; text-decoration: underline } .nl2go-default-textstyle { color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word } .default-button { color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px; font-style: normal; font-weight: normal; line-height: 1.15; text-decoration: none; word-break: break-word } .default-heading1 { color: #1F2D3D; font-family: arial,helvetica,sans-serif; font-size: 36px; word-break: break-word } .default-heading2 { color: #1F2D3D; font-family: arial,helvetica,sans-serif; font-size: 32px; word-break: break-word } .default-heading3 { color: #1F2D3D; font-family: arial,helvetica,sans-serif; font-size: 24px; word-break: break-word } .default-heading4 { color: #1F2D3D; font-family: arial,helvetica,sans-serif; font-size: 18px; word-break: break-word } a[x-apple-data-detectors] { color: inherit !important; text-decoration: inherit !important; font-size: inherit !important; font-family: inherit !important; font-weight: inherit !important; line-height: inherit !important; } .no-show-for-you { border: none; display: none; float: none; font-size: 0; height: 0; line-height: 0; max-height: 0; mso-hide: all; overflow: hidden; table-layout: fixed; visibility: hidden; width: 0; } </style><!--[if mso]><xml> <o:OfficeDocumentSettings> <o:AllowPNG/> <o:PixelsPerInch>96</o:PixelsPerInch> </o:OfficeDocumentSettings> </xml><![endif]--><style type=\"text/css\">a:link{color: #696969; text-decoration: underline;}</style></head><body bgcolor=\"#ffffff\" text=\"#3b3f44\" link=\"#696969\" yahoo=\"fix\" style=\"background-color: #ffffff;\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" class=\"nl2go-body-table\" width=\"100%\" style=\"background-color: #ffffff; width: 100%;\"><tr><td> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"600\" align=\"center\" class=\"r0-o\" style=\"table-layout: fixed; width: 600px;\"><tr><td valign=\"top\" class=\"r1-i\" style=\"background-color: #ffffff;\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" align=\"center\" class=\"r3-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td class=\"r4-i\" style=\"padding-bottom: 20px; padding-top: 20px;\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\"><tr><th width=\"100%\" valign=\"top\" class=\"r5-c\" style=\"font-weight: normal;\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r6-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td valign=\"top\" class=\"r7-i\" style=\"padding-left: 15px; padding-right: 15px;\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\"><tr><td class=\"r8-c\" align=\"left\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r9-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td align=\"center\" valign=\"top\" class=\"r10-i nl2go-default-textstyle\" style=\"color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-top: 15px; text-align: center;\"> <div><h1 class=\"default-heading1\" style=\"margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 36px; word-break: break-word;\">One poly</h1></div> </td> </tr></table></td> </tr><tr><td class=\"r11-c\" align=\"center\" style=\"font-size: 0px; line-height: 0px; padding-bottom: 15px; padding-top: 15px; valign: top;\"> <img src=\"https://img.mailinblue.com/8315004/images/content_library/original/6720ddb04946571a90b2b8df.png\" width=\"570\" border=\"0\" style=\"display: block; width: 100%;\"></td> </tr></table></td> </tr></table></th> </tr></table></td> </tr></table><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" align=\"center\" class=\"r3-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td class=\"r4-i\" style=\"padding-bottom: 20px; padding-top: 20px;\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\"><tr><th width=\"100%\" valign=\"top\" class=\"r5-c\" style=\"font-weight: normal;\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r6-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td valign=\"top\" class=\"r7-i\" style=\"padding-left: 10px; padding-right: 10px;\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\"><tr><td class=\"r8-c\" align=\"left\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r9-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td align=\"center\" valign=\"top\" class=\"r10-i nl2go-default-textstyle\" style=\"color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-top: 15px; text-align: center;\"> <div><h2 class=\"default-heading2\" style=\"margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 32px; word-break: break-word;\">Đổi mật khẩu</h2></div> </td> </tr></table></td> </tr><tr><td class=\"r12-c nl2go-default-textstyle\" align=\"left\" style=\"color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-bottom: 15px; padding-top: 15px; text-align: left; valign: top;\"> <div><p style=\"margin: 0;\">Chào bạn {{ contact.EMAIL }}, </p></div> </td> </tr><tr><td class=\"r12-c nl2go-default-textstyle\" align=\"left\" style=\"color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-bottom: 15px; padding-top: 15px; text-align: left; valign: top;\"> <div><p style=\"margin: 0;\">Bạn đã quên mật khẩu tài khoản bạn sao? <br>Đừng lo, dưới đây sẽ là đường dẫn gửi bạn đến trang đổi mật khẩu của hệ thống mình, mời bạn ấn nút bên dưới:</p></div> </td> </tr><tr><td class=\"r13-c\" align=\"center\" style=\"align: center; padding-bottom: 15px; padding-top: 15px; valign: top;\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"290\" class=\"r14-o\" style=\"background-color: #1B1B1B; border-collapse: separate; border-color: #1B1B1B; border-radius: 4px; border-style: solid; border-width: 0px; table-layout: fixed; width: 290px;\"><tr><td height=\"18\" align=\"center\" valign=\"top\" class=\"r15-i nl2go-default-textstyle\" style=\"word-break: break-word; background-color: #1B1B1B; border-radius: 4px; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px; font-style: normal; line-height: 1.15; padding-bottom: 12px; padding-left: 5px; padding-right: 5px; padding-top: 12px; text-align: center;\"> <a href=\""+hosst+"\" class=\"r16-r default-button\" target=\"_blank\" data-btn=\"1\" style=\"font-style: normal; font-weight: normal; line-height: 1.15; text-decoration: none; word-break: break-word; word-wrap: break-word; display: block; -webkit-text-size-adjust: none; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px;\"> <span>Đổi mật khẩu</span></a> </td> </tr></table></td> </tr></table></td> </tr></table></th> </tr></table></td> </tr></table><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" align=\"center\" class=\"r3-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td class=\"r17-i\" style=\"background-color: #eff2f7; padding-bottom: 20px; padding-top: 20px;\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\"><tr><th width=\"100%\" valign=\"top\" class=\"r5-c\" style=\"font-weight: normal;\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r6-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td valign=\"top\" class=\"r7-i\" style=\"padding-left: 15px; padding-right: 15px;\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\"><tr><td class=\"r8-c\" align=\"left\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r9-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td align=\"center\" valign=\"top\" class=\"r18-i nl2go-default-textstyle\" style=\"color: #3b3f44; font-family: arial,helvetica,sans-serif; word-break: break-word; font-size: 18px; line-height: 1.5; padding-top: 15px; text-align: center;\"> <div><p style=\"margin: 0;\"><strong>OnePoLySneaker</strong></p></div> </td> </tr></table></td> </tr><tr><td class=\"r8-c\" align=\"left\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r9-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td align=\"center\" valign=\"top\" class=\"r19-i nl2go-default-textstyle\" style=\"color: #3b3f44; font-family: arial,helvetica,sans-serif; word-break: break-word; font-size: 18px; line-height: 1.5; text-align: center;\"> <div><p style=\"margin: 0; font-size: 14px;\">BacTuLiem, 000084, HaNoi</p></div> </td> </tr></table></td> </tr><tr><td class=\"r8-c\" align=\"left\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r9-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td align=\"center\" valign=\"top\" class=\"r18-i nl2go-default-textstyle\" style=\"color: #3b3f44; font-family: arial,helvetica,sans-serif; word-break: break-word; font-size: 18px; line-height: 1.5; padding-top: 15px; text-align: center;\"> <div><p style=\"margin: 0; font-size: 14px;\">This email was sent to {{contact.EMAIL}}</p></div> </td> </tr></table></td> </tr><tr><td class=\"r8-c\" align=\"left\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" class=\"r9-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td align=\"center\" valign=\"top\" class=\"r19-i nl2go-default-textstyle\" style=\"color: #3b3f44; font-family: arial,helvetica,sans-serif; word-break: break-word; font-size: 18px; line-height: 1.5; text-align: center;\"> <div><p style=\"margin: 0; font-size: 14px;\">Chúc bạn có một ngày vui vẻ &lt;3</p></div> </td> </tr></table></td> </tr><tr><td class=\"r20-c\" align=\"center\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"100%\" align=\"center\" class=\"r3-o\" style=\"table-layout: fixed; width: 100%;\"><tr><td valign=\"top\" class=\"r21-i\" style=\"padding-bottom: 15px;\"> <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\"><tr><td class=\"r22-c\" align=\"center\"> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" width=\"129\" class=\"r23-o\" style=\"table-layout: fixed;\"><tr><td height=\"48\" style=\"font-size: 0px; line-height: 0px;\"> <a href=\"https://www.brevo.com?utm_source=logo_client&utm_medium=email\"><img src=\"https://creative-assets.mailinblue.com/rnb-assets/en.png\" width=\"129\" height=\"48\" border=\"0\" style=\"display: block; width: 100%;\"></a></td> </tr></table></td> </tr></table></td> </tr></table></td> </tr></table></td> </tr></table></th> </tr></table></td> </tr></table></td> </tr></table></td> </tr></table></body></html>\n";
        this.emailSenderService.sendEmail(receiverEmail,subject,content);
        return null;
    }

    public String templateEmailConfigmBill(String emailSend, String hosst, String codeHD) {
        String receiverEmail = emailSend;
        String subject = "Đơn hàng đã được xác nhận!";
        String content = """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:o="urn:schemas-microsoft-com:office:office">
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="format-detection" content="telephone=no">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Xác nhận đặt hàng</title>
                    <style type="text/css" emogrify="no">
                        #outlook a {
                            padding: 0;
                        }
                                
                        .ExternalClass {
                            width: 100%;
                        }
                                
                        .ExternalClass,
                        .ExternalClass p,
                        .ExternalClass span,
                        .ExternalClass font,
                        .ExternalClass td,
                        .ExternalClass div {
                            line-height: 100%;
                        }
                                
                        table td {
                            border-collapse: collapse;
                            mso-line-height-rule: exactly;
                        }
                                
                        .editable.image {
                            font-size: 0 !important;
                            line-height: 0 !important;
                        }
                                
                        .nl2go_preheader {
                            display: none !important;
                            mso-hide: all !important;
                            mso-line-height-rule: exactly;
                            visibility: hidden !important;
                            line-height: 0px !important;
                            font-size: 0px !important;
                        }
                                
                        body {
                            width: 100% !important;
                            -webkit-text-size-adjust: 100%;
                            -ms-text-size-adjust: 100%;
                            margin: 0;
                            padding: 0;
                        }
                                
                        img {
                            outline: none;
                            text-decoration: none;
                            -ms-interpolation-mode: bicubic;
                        }
                                
                        a img {
                            border: none;
                        }
                                
                        table {
                            border-collapse: collapse;
                            mso-table-lspace: 0pt;
                            mso-table-rspace: 0pt;
                        }
                                
                        th {
                            font-weight: normal;
                            text-align: left;
                        }
                                
                        *[class="gmail-fix"] {
                            display: none !important;
                        }
                    </style>
                    <style type="text/css" emogrify="no">
                        @media (max-width: 600px) {
                            .gmx-killpill {
                                content: ' \\03D1';
                            }
                        }
                    </style>
                    <style type="text/css" emogrify="no">
                        @media (max-width: 600px) {
                            .gmx-killpill {
                                content: ' \\03D1';
                            }
                                
                            .r0-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 320px !important
                            }
                                
                            .r1-i {
                                background-color: #ffffff !important
                            }
                                
                            .r2-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 100% !important
                            }
                                
                            .r3-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 100% !important
                            }
                                
                            .r4-i {
                                padding-bottom: 20px !important;
                                padding-left: 10px !important;
                                padding-right: 10px !important;
                                padding-top: 20px !important
                            }
                                
                            .r5-c {
                                box-sizing: border-box !important;
                                display: block !important;
                                valign: top !important;
                                width: 100% !important
                            }
                                
                            .r6-o {
                                border-style: solid !important;
                                width: 100% !important
                            }
                                
                            .r7-i {
                                padding-left: 0px !important;
                                padding-right: 0px !important
                            }
                                
                            .r8-i {
                                padding-bottom: 15px !important;
                                padding-top: 15px !important
                            }
                                
                            .r9-c {
                                box-sizing: border-box !important;
                                text-align: left !important;
                                valign: top !important;
                                width: 100% !important
                            }
                                
                            .r10-o {
                                border-style: solid !important;
                                margin: 0 auto 0 0 !important;
                                width: 100% !important
                            }
                                
                            .r11-i {
                                padding-top: 15px !important;
                                text-align: center !important
                            }
                                
                            .r12-c {
                                box-sizing: border-box !important;
                                padding-top: 15px !important;
                                text-align: left !important;
                                valign: top !important;
                                width: 100% !important
                            }
                                
                            .r13-c {
                                box-sizing: border-box !important;
                                padding: 0 !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 100% !important
                            }
                                
                            .r14-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                margin-bottom: 15px !important;
                                margin-top: 15px !important;
                                width: 100% !important
                            }
                                
                            .r15-i {
                                padding: 0 !important;
                                text-align: center !important
                            }
                                
                            .r16-r {
                                background-color: #1B1B1B !important;
                                border-radius: 4px !important;
                                border-width: 0px !important;
                                box-sizing: border-box;
                                height: initial !important;
                                padding: 0 !important;
                                padding-bottom: 12px !important;
                                padding-left: 5px !important;
                                padding-right: 5px !important;
                                padding-top: 12px !important;
                                text-align: center !important;
                                width: 100% !important
                            }
                                
                            .r17-i {
                                background-color: #eff2f7 !important;
                                padding-bottom: 20px !important;
                                padding-left: 15px !important;
                                padding-right: 15px !important;
                                padding-top: 20px !important
                            }
                                
                            .r18-i {
                                color: #3b3f44 !important;
                                padding-bottom: 0px !important;
                                padding-top: 15px !important;
                                text-align: center !important
                            }
                                
                            .r19-i {
                                color: #3b3f44 !important;
                                padding-bottom: 0px !important;
                                padding-top: 0px !important;
                                text-align: center !important
                            }
                                
                            .r20-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                width: 100% !important
                            }
                                
                            .r21-i {
                                padding-bottom: 15px !important;
                                padding-left: 0px !important;
                                padding-right: 0px !important;
                                padding-top: 0px !important
                            }
                                
                            .r22-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 129px !important
                            }
                                
                            .r23-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 129px !important
                            }
                                
                            body {
                                -webkit-text-size-adjust: none
                            }
                                
                            .nl2go-responsive-hide {
                                display: none
                            }
                                
                            .nl2go-body-table {
                                min-width: unset !important
                            }
                                
                            .mobshow {
                                height: auto !important;
                                overflow: visible !important;
                                max-height: unset !important;
                                visibility: visible !important
                            }
                                
                            .resp-table {
                                display: inline-table !important
                            }
                                
                            .magic-resp {
                                display: table-cell !important
                            }
                        }
                    </style>
                    <style type="text/css">
                        p,
                        h1,
                        h2,
                        h3,
                        h4,
                        ol,
                        ul,
                        li {
                            margin: 0;
                        }
                                
                        a,
                        a:link {
                            color: #696969;
                            text-decoration: underline
                        }
                                
                        .nl2go-default-textstyle {
                            color: #3b3f44;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 16px;
                            line-height: 1.5;
                            word-break: break-word
                        }
                                
                        .default-button {
                            color: #ffffff;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 16px;
                            font-style: normal;
                            font-weight: bold;
                            line-height: 1.15;
                            text-decoration: none;
                            word-break: break-word
                        }
                                
                        .default-heading1 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 36px;
                            word-break: break-word
                        }
                                
                        .default-heading2 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 32px;
                            word-break: break-word
                        }
                                
                        .default-heading3 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 24px;
                            word-break: break-word
                        }
                                
                        .default-heading4 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 18px;
                            word-break: break-word
                        }
                                
                        a[x-apple-data-detectors] {
                            color: inherit !important;
                            text-decoration: inherit !important;
                            font-size: inherit !important;
                            font-family: inherit !important;
                            font-weight: inherit !important;
                            line-height: inherit !important;
                        }
                                
                        .no-show-for-you {
                            border: none;
                            display: none;
                            float: none;
                            font-size: 0;
                            height: 0;
                            line-height: 0;
                            max-height: 0;
                            mso-hide: all;
                            overflow: hidden;
                            table-layout: fixed;
                            visibility: hidden;
                            width: 0;
                        }
                    </style>
                    <!--[if mso]><xml> <o:OfficeDocumentSettings> <o:AllowPNG/> <o:PixelsPerInch>96</o:PixelsPerInch> </o:OfficeDocumentSettings> </xml><![endif]-->
                    <style type="text/css">
                        a:link {
                            color: #696969;
                            text-decoration: underline;
                        }
                    </style>
                </head>
                                
                <body bgcolor="#ffffff" text="#3b3f44" link="#696969" yahoo="fix" style="background-color: #ffffff;">
                    <table cellspacing="0" cellpadding="0" border="0" role="presentation" class="nl2go-body-table" width="100%"
                        style="background-color: #ffffff; width: 100%;">
                        <tr>
                            <td>
                                <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="600" align="center"
                                    class="r0-o" style="table-layout: fixed; width: 600px;">
                                    <tr>
                                        <td valign="top" class="r1-i" style="background-color: #ffffff;">
                                            <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="100%"
                                                align="center" class="r3-o" style="table-layout: fixed; width: 100%;">
                                                <tr>
                                                    <td class="r4-i" style="padding-bottom: 20px; padding-top: 20px;">
                                                        <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                                            role="presentation">
                                                            <tr>
                                                                <th width="100%" valign="top" class="r5-c" style="font-weight: normal;">
                                                                    <table cellspacing="0" cellpadding="0" border="0"
                                                                        role="presentation" width="100%" class="r6-o"
                                                                        style="table-layout: fixed; width: 100%;">
                                                                        <tr>
                                                                            <td valign="top" class="r7-i">
                                                                                <table width="100%" cellspacing="0" cellpadding="0"
                                                                                    border="0" role="presentation">
                                                                                    <tr>
                                                                                        <td class="r2-c" align="center">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="276" class="r3-o"
                                                                                                style="table-layout: fixed; width: 276px;">
                                                                                                <tr>
                                                                                                    <td class="r8-i"
                                                                                                        style="font-size: 0px; line-height: 0px; padding-bottom: 15px; padding-top: 15px;">
                                                                                                        <img src="https://img.mailinblue.com/8315004/images/content_library/original/6731007acf0be6181c8e9bb4.png"
                                                                                                            width="276" border="0"
                                                                                                            style="display: block; width: 100%;">
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r11-i nl2go-default-textstyle"
                                                                                                        style="color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-top: 15px; text-align: center;">
                                                                                                        <div>
                                                                                                            <h1 class="default-heading1"
                                                                                                                style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 36px; word-break: break-word;">
                                                                                                                Đã xác nhận</h1>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r12-c nl2go-default-textstyle"
                                                                                            align="left"
                                                                                            style="color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-top: 15px; text-align: left; valign: top; word-wrap: break-word;">
                                                                                            <div>
                                                                                                <h4 class="default-heading4"
                                                                                                    style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 18px; word-break: break-word;">
                                                                                                    Chào {{contact.EMAIL}},</h4>
                                                                                                <p style="margin: 0;"> </p>
                                                                                                <p style="margin: 0;">Cảm ơn bạn đã đặt
                                                                                                    mua sản phẩm của One Poly, đơn hàng
                                                                                                    cả bạn đã được xác nhận.</p>
                                                                                                <p style="margin: 0;"><br>Mã đơn hàng :
                                                                                                """+codeHD+"""
                                                                                                            </p>
                                                                                                    <p style="margin: 0;"> </p>
                                                                                                    <p style="margin: 0;">Để có thể xem
                                                                                                        trạng thái của hóa đơn, bạn có thể
                                                                                                        ấn nút phía dưới hoặc lên trang <a
                                                                                                            href="http://localhost:8080/onepoly/home"
                                                                                                            target="_blank"
                                                                                                            style="color: #696969; text-decoration: underline;">onepoly</a>
                                                                                                        rồi tìm đến phần tìm kiếm hóa đơn để
                                                                                                        có thể theo dõi trạng thái hóa
                                                                                                        đơn.<br> </p>
                                                                                                    <p style="margin: 0;">Bên mình còn rất
                                                                                                        nhiều sản phẩm khác, nếu bạn quan
                                                                                                        tâm, thì hãy tới trang <a
                                                                                                            href="http://localhost:8080/onepoly/home"
                                                                                                            target="_blank"
                                                                                                            style="color: #696969; text-decoration: underline;">onepoly</a>
                                                                                                        để tìm kiếm cho bản thân mình những
                                                                                                        món đồ phú hợp.</p>
                                                                                                    <p style="margin: 0;"> </p>
                                                                                                    <h4 class="default-heading4"
                                                                                                        style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 18px; word-break: break-word;">
                                                                                                        Chúc bạn một ngày mới tốt lành &lt;3
                                                                                                    </h4>
                                                                                                    <p style="margin: 0;"> </p>
                                                                                                </div>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td class="r13-c" align="center"
                                                                                                style="align: center; padding-bottom: 15px; padding-top: 15px; valign: top;">
                                                                                                <table cellspacing="0" cellpadding="0"
                                                                                                    border="0" role="presentation"
                                                                                                    width="300" class="r14-o"
                                                                                                    style="background-color: #1B1B1B; border-collapse: separate; border-color: #1B1B1B; border-radius: 4px; border-style: solid; border-width: 0px; table-layout: fixed; width: 300px;">
                                                                                                    <tr>
                                                                                                        <td height="18" align="center"
                                                                                                            valign="top"
                                                                                                            class="r15-i nl2go-default-textstyle"
                                                                                                            style="word-break: break-word; background-color: #1B1B1B; border-radius: 4px; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px; font-style: normal; line-height: 1.15; padding-bottom: 12px; padding-left: 5px; padding-right: 5px; padding-top: 12px; text-align: center;">
                                                                                                            <a href="
                                                                                                            """+hosst+"""
                                                                                                                "class="r16-r default-button"
                                                                                                                target="_blank" data-btn="1"
                                                                                                                style="font-style: normal; font-weight: bold; line-height: 1.15; text-decoration: none; word-break: break-word; word-wrap: break-word; display: block; -webkit-text-size-adjust: none; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px;">
                                                                                                                <span>Đơn hàng</span></a>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </th>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="100%"
                                                    align="center" class="r3-o" style="table-layout: fixed; width: 100%;">
                                                    <tr>
                                                        <td class="r17-i"
                                                            style="background-color: #eff2f7; padding-bottom: 20px; padding-top: 20px;">
                                                            <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                                                role="presentation">
                                                                <tr>
                                                                    <th width="100%" valign="top" class="r5-c" style="font-weight: normal;">
                                                                        <table cellspacing="0" cellpadding="0" border="0"
                                                                            role="presentation" width="100%" class="r6-o"
                                                                            style="table-layout: fixed; width: 100%;">
                                                                            <tr>
                                                                                <td valign="top" class="r7-i"
                                                                                    style="padding-left: 15px; padding-right: 15px;">
                                                                                    <table width="100%" cellspacing="0" cellpadding="0"
                                                                                        border="0" role="presentation">
                                                                                        <tr>
                                                                                            <td class="r9-c" align="left">
                                                                                                <table cellspacing="0" cellpadding="0"
                                                                                                    border="0" role="presentation"
                                                                                                    width="100%" class="r10-o"
                                                                                                    style="table-layout: fixed; width: 100%;">
                                                                                                    <tr>
                                                                                                        <td align="center" valign="top"
                                                                                                            class="r18-i nl2go-default-textstyle"
                                                                                                            style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; padding-top: 15px; text-align: center;">
                                                                                                            <div>
                                                                                                                <p style="margin: 0;">
                                                                                                                    <strong>OnePoLySneaker</strong>
                                                                                                                </p>
                                                                                                            </div>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td class="r9-c" align="left">
                                                                                                <table cellspacing="0" cellpadding="0"
                                                                                                    border="0" role="presentation"
                                                                                                    width="100%" class="r10-o"
                                                                                                    style="table-layout: fixed; width: 100%;">
                                                                                                    <tr>
                                                                                                        <td align="center" valign="top"
                                                                                                            class="r19-i nl2go-default-textstyle"
                                                                                                            style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; text-align: center;">
                                                                                                            <div>
                                                                                                                <p
                                                                                                                    style="margin: 0; font-size: 14px;">
                                                                                                                    BacTuLiem, 000084, HaNoi
                                                                                                                </p>
                                                                                                            </div>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td class="r9-c" align="left">
                                                                                                <table cellspacing="0" cellpadding="0"
                                                                                                    border="0" role="presentation"
                                                                                                    width="100%" class="r10-o"
                                                                                                    style="table-layout: fixed; width: 100%;">
                                                                                                    <tr>
                                                                                                        <td align="center" valign="top"
                                                                                                            class="r18-i nl2go-default-textstyle"
                                                                                                            style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; padding-top: 15px; text-align: center;">
                                                                                                            <div>
                                                                                                                <p
                                                                                                                    style="margin: 0; font-size: 14px;">
                                                                                                                    This email was sent
                                                                                                                    to {{contact.EMAIL}}</p>
                                                                                                                <p
                                                                                                                    style="margin: 0; font-size: 14px;">
                                                                                                                    Chúc bạn có một ngày vui
                                                                                                                    vẻ &lt;3</p>
                                                                                                            </div>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td class="r20-c" align="center">
                                                                                                <table cellspacing="0" cellpadding="0"
                                                                                                    border="0" role="presentation"
                                                                                                    width="100%" align="center" class="r3-o"
                                                                                                    style="table-layout: fixed; width: 100%;">
                                                                                                    <tr>
                                                                                                        <td valign="top" class="r21-i"
                                                                                                            style="padding-bottom: 15px;">
                                                                                                            <table width="100%"
                                                                                                                cellspacing="0"
                                                                                                                cellpadding="0" border="0"
                                                                                                                role="presentation">
                                                                                                                <tr>
                                                                                                                    <td class="r22-c"
                                                                                                                        align="center">
                                                                                                                        <table
                                                                                                                            cellspacing="0"
                                                                                                                            cellpadding="0"
                                                                                                                            border="0"
                                                                                                                            role="presentation"
                                                                                                                            width="129"
                                                                                                                            class="r23-o"
                                                                                                                            style="table-layout: fixed;">
                                                                                                                            <tr>
                                                                                                                                <td height="48"
                                                                                                                                    style="font-size: 0px; line-height: 0px;">
                                                                                                                                    <a
                                                                                                                                        href="https://www.brevo.com?utm_source=logo_client&utm_medium=email"><img
                                                                                                                                            src="https://creative-assets.mailinblue.com/rnb-assets/en.png"
                                                                                                                                            width="129"
                                                                                                                                            height="48"
                                                                                                                                            border="0"
                                                                                                                                            style="display: block; width: 100%;"></a>
                                                                                                                                </td>
                                                                                                                            </tr>
                                                                                                                        </table>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                            </table>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </th>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </body>
                    
                    </html>
                """;
        this.emailSenderService.sendEmail(receiverEmail,subject,content);
        return null;
    }

    public String templateCreateBillClient(String emailSend, String hosst, String codeHD) {
        String receiverEmail = emailSend;
        String subject = "Đơn hàng chờ xác nhận!";
        String content = """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:o="urn:schemas-microsoft-com:office:office">
                    
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="format-detection" content="telephone=no">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Đơn hàng chờ xác nhận</title>
                    <style type="text/css" emogrify="no">
                        #outlook a {
                            padding: 0;
                        }
                    
                        .ExternalClass {
                            width: 100%;
                        }
                    
                        .ExternalClass,
                        .ExternalClass p,
                        .ExternalClass span,
                        .ExternalClass font,
                        .ExternalClass td,
                        .ExternalClass div {
                            line-height: 100%;
                        }
                    
                        table td {
                            border-collapse: collapse;
                            mso-line-height-rule: exactly;
                        }
                    
                        .editable.image {
                            font-size: 0 !important;
                            line-height: 0 !important;
                        }
                    
                        .nl2go_preheader {
                            display: none !important;
                            mso-hide: all !important;
                            mso-line-height-rule: exactly;
                            visibility: hidden !important;
                            line-height: 0px !important;
                            font-size: 0px !important;
                        }
                    
                        body {
                            width: 100% !important;
                            -webkit-text-size-adjust: 100%;
                            -ms-text-size-adjust: 100%;
                            margin: 0;
                            padding: 0;
                        }
                    
                        img {
                            outline: none;
                            text-decoration: none;
                            -ms-interpolation-mode: bicubic;
                        }
                    
                        a img {
                            border: none;
                        }
                    
                        table {
                            border-collapse: collapse;
                            mso-table-lspace: 0pt;
                            mso-table-rspace: 0pt;
                        }
                    
                        th {
                            font-weight: normal;
                            text-align: left;
                        }
                    
                        *[class="gmail-fix"] {
                            display: none !important;
                        }
                    </style>
                    <style type="text/css" emogrify="no">
                        @media (max-width: 600px) {
                            .gmx-killpill {
                                content: ' \\03D1';
                            }
                        }
                    </style>
                    <style type="text/css" emogrify="no">
                        @media (max-width: 600px) {
                            .gmx-killpill {
                                content: ' \\03D1';
                            }
                    
                            .r0-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 320px !important
                            }
                    
                            .r1-i {
                                background-color: #ffffff !important
                            }
                    
                            .r2-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 100% !important
                            }
                    
                            .r3-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 100% !important
                            }
                    
                            .r4-i {
                                padding-bottom: 20px !important;
                                padding-left: 10px !important;
                                padding-right: 10px !important;
                                padding-top: 20px !important
                            }
                    
                            .r5-c {
                                box-sizing: border-box !important;
                                display: block !important;
                                valign: top !important;
                                width: 100% !important
                            }
                    
                            .r6-o {
                                border-style: solid !important;
                                width: 100% !important
                            }
                    
                            .r7-i {
                                padding-left: 0px !important;
                                padding-right: 0px !important
                            }
                    
                            .r8-i {
                                padding-bottom: 15px !important;
                                padding-top: 15px !important
                            }
                    
                            .r9-c {
                                box-sizing: border-box !important;
                                text-align: left !important;
                                valign: top !important;
                                width: 100% !important
                            }
                    
                            .r10-o {
                                border-style: solid !important;
                                margin: 0 auto 0 0 !important;
                                width: 100% !important
                            }
                    
                            .r11-i {
                                padding-top: 15px !important;
                                text-align: center !important
                            }
                    
                            .r12-c {
                                box-sizing: border-box !important;
                                padding-top: 15px !important;
                                text-align: left !important;
                                valign: top !important;
                                width: 100% !important
                            }
                    
                            .r13-c {
                                box-sizing: border-box !important;
                                padding: 0 !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 100% !important
                            }
                    
                            .r14-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                margin-bottom: 15px !important;
                                margin-top: 15px !important;
                                width: 100% !important
                            }
                    
                            .r15-i {
                                padding: 0 !important;
                                text-align: center !important
                            }
                    
                            .r16-r {
                                background-color: #1B1B1B !important;
                                border-radius: 4px !important;
                                border-width: 0px !important;
                                box-sizing: border-box;
                                height: initial !important;
                                padding: 0 !important;
                                padding-bottom: 12px !important;
                                padding-left: 5px !important;
                                padding-right: 5px !important;
                                padding-top: 12px !important;
                                text-align: center !important;
                                width: 100% !important
                            }
                    
                            .r17-i {
                                background-color: #eff2f7 !important;
                                padding-bottom: 20px !important;
                                padding-left: 15px !important;
                                padding-right: 15px !important;
                                padding-top: 20px !important
                            }
                    
                            .r18-i {
                                color: #3b3f44 !important;
                                padding-bottom: 0px !important;
                                padding-top: 15px !important;
                                text-align: center !important
                            }
                    
                            .r19-i {
                                color: #3b3f44 !important;
                                padding-bottom: 0px !important;
                                padding-top: 0px !important;
                                text-align: center !important
                            }
                    
                            .r20-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                width: 100% !important
                            }
                    
                            .r21-i {
                                padding-bottom: 15px !important;
                                padding-left: 0px !important;
                                padding-right: 0px !important;
                                padding-top: 0px !important
                            }
                    
                            .r22-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 129px !important
                            }
                    
                            .r23-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 129px !important
                            }
                    
                            body {
                                -webkit-text-size-adjust: none
                            }
                    
                            .nl2go-responsive-hide {
                                display: none
                            }
                    
                            .nl2go-body-table {
                                min-width: unset !important
                            }
                    
                            .mobshow {
                                height: auto !important;
                                overflow: visible !important;
                                max-height: unset !important;
                                visibility: visible !important
                            }
                    
                            .resp-table {
                                display: inline-table !important
                            }
                    
                            .magic-resp {
                                display: table-cell !important
                            }
                        }
                    </style>
                    <style type="text/css">
                        p,
                        h1,
                        h2,
                        h3,
                        h4,
                        ol,
                        ul,
                        li {
                            margin: 0;
                        }
                    
                        a,
                        a:link {
                            color: #696969;
                            text-decoration: underline
                        }
                    
                        .nl2go-default-textstyle {
                            color: #3b3f44;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 16px;
                            line-height: 1.5;
                            word-break: break-word
                        }
                    
                        .default-button {
                            color: #ffffff;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 16px;
                            font-style: normal;
                            font-weight: bold;
                            line-height: 1.15;
                            text-decoration: none;
                            word-break: break-word
                        }
                    
                        .default-heading1 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 36px;
                            word-break: break-word
                        }
                    
                        .default-heading2 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 32px;
                            word-break: break-word
                        }
                    
                        .default-heading3 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 24px;
                            word-break: break-word
                        }
                    
                        .default-heading4 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 18px;
                            word-break: break-word
                        }
                    
                        a[x-apple-data-detectors] {
                            color: inherit !important;
                            text-decoration: inherit !important;
                            font-size: inherit !important;
                            font-family: inherit !important;
                            font-weight: inherit !important;
                            line-height: inherit !important;
                        }
                    
                        .no-show-for-you {
                            border: none;
                            display: none;
                            float: none;
                            font-size: 0;
                            height: 0;
                            line-height: 0;
                            max-height: 0;
                            mso-hide: all;
                            overflow: hidden;
                            table-layout: fixed;
                            visibility: hidden;
                            width: 0;
                        }
                    </style>
                    <!--[if mso]><xml> <o:OfficeDocumentSettings> <o:AllowPNG/> <o:PixelsPerInch>96</o:PixelsPerInch> </o:OfficeDocumentSettings> </xml><![endif]-->
                    <style type="text/css">
                        a:link {
                            color: #696969;
                            text-decoration: underline;
                        }
                    </style>
                </head>
                    
                <body bgcolor="#ffffff" text="#3b3f44" link="#696969" yahoo="fix" style="background-color: #ffffff;">
                    <table cellspacing="0" cellpadding="0" border="0" role="presentation" class="nl2go-body-table" width="100%"
                        style="background-color: #ffffff; width: 100%;">
                        <tr>
                            <td>
                                <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="600" align="center"
                                    class="r0-o" style="table-layout: fixed; width: 600px;">
                                    <tr>
                                        <td valign="top" class="r1-i" style="background-color: #ffffff;">
                                            <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="100%"
                                                align="center" class="r3-o" style="table-layout: fixed; width: 100%;">
                                                <tr>
                                                    <td class="r4-i" style="padding-bottom: 20px; padding-top: 20px;">
                                                        <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                                            role="presentation">
                                                            <tr>
                                                                <th width="100%" valign="top" class="r5-c" style="font-weight: normal;">
                                                                    <table cellspacing="0" cellpadding="0" border="0"
                                                                        role="presentation" width="100%" class="r6-o"
                                                                        style="table-layout: fixed; width: 100%;">
                                                                        <tr>
                                                                            <td valign="top" class="r7-i">
                                                                                <table width="100%" cellspacing="0" cellpadding="0"
                                                                                    border="0" role="presentation">
                                                                                    <tr>
                                                                                        <td class="r2-c" align="center">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="276" class="r3-o"
                                                                                                style="table-layout: fixed; width: 276px;">
                                                                                                <tr>
                                                                                                    <td class="r8-i"
                                                                                                        style="font-size: 0px; line-height: 0px; padding-bottom: 15px; padding-top: 15px;">
                                                                                                        <img src="https://img.mailinblue.com/8315004/images/content_library/original/6731007acf0be6181c8e9bb4.png"
                                                                                                            width="276" border="0"
                                                                                                            style="display: block; width: 100%;">
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r11-i nl2go-default-textstyle"
                                                                                                        style="color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-top: 15px; text-align: center;">
                                                                                                        <div>
                                                                                                            <h1 class="default-heading1"
                                                                                                                style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 36px; word-break: break-word;">
                                                                                                                Xác nhận đơn đặt hàng
                                                                                                            </h1>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r12-c nl2go-default-textstyle"
                                                                                            align="left"
                                                                                            style="color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-top: 15px; text-align: left; valign: top; word-wrap: break-word;">
                                                                                            <div>
                                                                                                <h4 class="default-heading4"
                                                                                                    style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 18px; word-break: break-word;">
                                                                                                    Chào {{contact.EMAIL}},</h4>
                                                                                                <p style="margin: 0;"> </p>
                                                                                                <p style="margin: 0;">Cảm ơn bạn đã đặt
                                                                                                    mua sản phẩm của One Poly, đơn hàng
                                                                                                    cả bạn đã được gửi cho bên quản lý
                                                                                                    xác nhận đơn hàng, thời gian xác
                                                                                                    nhận có hơi lâu xíu, rất mong bạn
                                                                                                    thông cảm.</p>
                                                                                                <p style="margin: 0;"><br>Mã đơn hàng :
                                                                                                    """+codeHD+ """
                                                                                                </p>
                                                                                                <p style="margin: 0;"> </p>
                                                                                                <p style="margin: 0;">Để có thể xem
                                                                                                    trạng thái của hóa đơn, bạn có thể
                                                                                                    ấn nút phía dưới hoặc lên trang <a
                                                                                                        href="http://localhost:8080/onepoly/home"
                                                                                                        target="_blank"
                                                                                                        style="color: #696969; text-decoration: underline;">onepoly</a>
                                                                                                    rồi tìm đến phần tìm kiếm hóa đơn để
                                                                                                    có thể theo dõi trạng thái hóa
                                                                                                    đơn.<br> </p>
                                                                                                <p style="margin: 0;">Bên mình còn rất
                                                                                                    nhiều sản phẩm khác, nếu bạn quan
                                                                                                    tâm, thì hãy tới trang <a
                                                                                                        href="http://localhost:8080/onepoly/home"
                                                                                                        target="_blank"
                                                                                                        style="color: #696969; text-decoration: underline;">onepoly</a>
                                                                                                    để tìm kiếm cho bản thân mình những
                                                                                                    món đồ phú hợp.</p>
                                                                                                <p style="margin: 0;"> </p>
                                                                                                <h4 class="default-heading4"
                                                                                                    style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 18px; word-break: break-word;">
                                                                                                    Chúc bạn một ngày mới tốt lành &lt;3
                                                                                                </h4>
                                                                                                <p style="margin: 0;"> </p>
                                                                                            </div>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r13-c" align="center"
                                                                                            style="align: center; padding-bottom: 15px; padding-top: 15px; valign: top;">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="300" class="r14-o"
                                                                                                style="background-color: #1B1B1B; border-collapse: separate; border-color: #1B1B1B; border-radius: 4px; border-style: solid; border-width: 0px; table-layout: fixed; width: 300px;">
                                                                                                <tr>
                                                                                                    <td height="18" align="center"
                                                                                                        valign="top"
                                                                                                        class="r15-i nl2go-default-textstyle"
                                                                                                        style="word-break: break-word; background-color: #1B1B1B; border-radius: 4px; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px; font-style: normal; line-height: 1.15; padding-bottom: 12px; padding-left: 5px; padding-right: 5px; padding-top: 12px; text-align: center;">
                                                                                                        <a href=" 
                                                                                                        """+hosst+"""
                                                                                                        "
                                                                                                            class="r16-r default-button"
                                                                                                            target="_blank" data-btn="1"
                                                                                                            style="font-style: normal; font-weight: bold; line-height: 1.15; text-decoration: none; word-break: break-word; word-wrap: break-word; display: block; -webkit-text-size-adjust: none; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px;">
                                                                                                            <span>Đơn hàng</span></a>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </th>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                            <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="100%"
                                                align="center" class="r3-o" style="table-layout: fixed; width: 100%;">
                                                <tr>
                                                    <td class="r17-i"
                                                        style="background-color: #eff2f7; padding-bottom: 20px; padding-top: 20px;">
                                                        <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                                            role="presentation">
                                                            <tr>
                                                                <th width="100%" valign="top" class="r5-c" style="font-weight: normal;">
                                                                    <table cellspacing="0" cellpadding="0" border="0"
                                                                        role="presentation" width="100%" class="r6-o"
                                                                        style="table-layout: fixed; width: 100%;">
                                                                        <tr>
                                                                            <td valign="top" class="r7-i"
                                                                                style="padding-left: 15px; padding-right: 15px;">
                                                                                <table width="100%" cellspacing="0" cellpadding="0"
                                                                                    border="0" role="presentation">
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r18-i nl2go-default-textstyle"
                                                                                                        style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; padding-top: 15px; text-align: center;">
                                                                                                        <div>
                                                                                                            <p style="margin: 0;">
                                                                                                                <strong>OnePoLySneaker</strong>
                                                                                                            </p>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r19-i nl2go-default-textstyle"
                                                                                                        style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; text-align: center;">
                                                                                                        <div>
                                                                                                            <p
                                                                                                                style="margin: 0; font-size: 14px;">
                                                                                                                BacTuLiem, 000084, HaNoi
                                                                                                            </p>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r18-i nl2go-default-textstyle"
                                                                                                        style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; padding-top: 15px; text-align: center;">
                                                                                                        <div>
                                                                                                            <p
                                                                                                                style="margin: 0; font-size: 14px;">
                                                                                                                This email was sent
                                                                                                                to {{contact.EMAIL}}</p>
                                                                                                            <p
                                                                                                                style="margin: 0; font-size: 14px;">
                                                                                                                Chúc bạn có một ngày vui
                                                                                                                vẻ &lt;3</p>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r20-c" align="center">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" align="center" class="r3-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td valign="top" class="r21-i"
                                                                                                        style="padding-bottom: 15px;">
                                                                                                        <table width="100%"
                                                                                                            cellspacing="0"
                                                                                                            cellpadding="0" border="0"
                                                                                                            role="presentation">
                                                                                                            <tr>
                                                                                                                <td class="r22-c"
                                                                                                                    align="center">
                                                                                                                    <table
                                                                                                                        cellspacing="0"
                                                                                                                        cellpadding="0"
                                                                                                                        border="0"
                                                                                                                        role="presentation"
                                                                                                                        width="129"
                                                                                                                        class="r23-o"
                                                                                                                        style="table-layout: fixed;">
                                                                                                                        <tr>
                                                                                                                            <td height="48"
                                                                                                                                style="font-size: 0px; line-height: 0px;">
                                                                                                                                <a
                                                                                                                                    href="https://www.brevo.com?utm_source=logo_client&utm_medium=email"><img
                                                                                                                                        src="https://creative-assets.mailinblue.com/rnb-assets/en.png"
                                                                                                                                        width="129"
                                                                                                                                        height="48"
                                                                                                                                        border="0"
                                                                                                                                        style="display: block; width: 100%;"></a>
                                                                                                                            </td>
                                                                                                                        </tr>
                                                                                                                    </table>
                                                                                                                </td>
                                                                                                            </tr>
                                                                                                        </table>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </th>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                    
                </html>
                """;
        this.emailSenderService.sendEmail(receiverEmail,subject,content);
        return null;
    }

    public void templateRequestBill(String emailSend, String hosst, String codeHD) {
        String receiverEmail = emailSend;
        String subject = "Yêu cầu hóa đơn!";
        String content = """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:o="urn:schemas-microsoft-com:office:office">
                        
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="format-detection" content="telephone=no">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Yêu cầu hóa đơn điện tử</title>
                    <style type="text/css" emogrify="no">
                        #outlook a {
                            padding: 0;
                        }
                        
                        .ExternalClass {
                            width: 100%;
                        }
                        
                        .ExternalClass,
                        .ExternalClass p,
                        .ExternalClass span,
                        .ExternalClass font,
                        .ExternalClass td,
                        .ExternalClass div {
                            line-height: 100%;
                        }
                        
                        table td {
                            border-collapse: collapse;
                            mso-line-height-rule: exactly;
                        }
                        
                        .editable.image {
                            font-size: 0 !important;
                            line-height: 0 !important;
                        }
                        
                        .nl2go_preheader {
                            display: none !important;
                            mso-hide: all !important;
                            mso-line-height-rule: exactly;
                            visibility: hidden !important;
                            line-height: 0px !important;
                            font-size: 0px !important;
                        }
                        
                        body {
                            width: 100% !important;
                            -webkit-text-size-adjust: 100%;
                            -ms-text-size-adjust: 100%;
                            margin: 0;
                            padding: 0;
                        }
                        
                        img {
                            outline: none;
                            text-decoration: none;
                            -ms-interpolation-mode: bicubic;
                        }
                        
                        a img {
                            border: none;
                        }
                        
                        table {
                            border-collapse: collapse;
                            mso-table-lspace: 0pt;
                            mso-table-rspace: 0pt;
                        }
                        
                        th {
                            font-weight: normal;
                            text-align: left;
                        }
                        
                        *[class="gmail-fix"] {
                            display: none !important;
                        }
                    </style>
                    <style type="text/css" emogrify="no">
                        @media (max-width: 600px) {
                            .gmx-killpill {
                                content: ' \\03D1';
                            }
                        }
                    </style>
                    <style type="text/css" emogrify="no">
                        @media (max-width: 600px) {
                            .gmx-killpill {
                                content: ' \\03D1';
                            }
                        
                            .r0-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 320px !important
                            }
                        
                            .r1-i {
                                background-color: #ffffff !important
                            }
                        
                            .r2-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 100% !important
                            }
                        
                            .r3-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 100% !important
                            }
                        
                            .r4-i {
                                padding-bottom: 20px !important;
                                padding-left: 10px !important;
                                padding-right: 10px !important;
                                padding-top: 20px !important
                            }
                        
                            .r5-c {
                                box-sizing: border-box !important;
                                display: block !important;
                                valign: top !important;
                                width: 100% !important
                            }
                        
                            .r6-o {
                                border-style: solid !important;
                                width: 100% !important
                            }
                        
                            .r7-i {
                                padding-left: 0px !important;
                                padding-right: 0px !important
                            }
                        
                            .r8-i {
                                padding-bottom: 15px !important;
                                padding-top: 15px !important
                            }
                        
                            .r9-c {
                                box-sizing: border-box !important;
                                text-align: left !important;
                                valign: top !important;
                                width: 100% !important
                            }
                        
                            .r10-o {
                                border-style: solid !important;
                                margin: 0 auto 0 0 !important;
                                width: 100% !important
                            }
                        
                            .r11-i {
                                padding-top: 15px !important;
                                text-align: center !important
                            }
                        
                            .r12-c {
                                box-sizing: border-box !important;
                                padding-top: 15px !important;
                                text-align: left !important;
                                valign: top !important;
                                width: 100% !important
                            }
                        
                            .r13-c {
                                box-sizing: border-box !important;
                                padding: 0 !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 100% !important
                            }
                        
                            .r14-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                margin-bottom: 15px !important;
                                margin-top: 15px !important;
                                width: 100% !important
                            }
                        
                            .r15-i {
                                padding: 0 !important;
                                text-align: center !important
                            }
                        
                            .r16-r {
                                background-color: #1B1B1B !important;
                                border-radius: 4px !important;
                                border-width: 0px !important;
                                box-sizing: border-box;
                                height: initial !important;
                                padding: 0 !important;
                                padding-bottom: 12px !important;
                                padding-left: 5px !important;
                                padding-right: 5px !important;
                                padding-top: 12px !important;
                                text-align: center !important;
                                width: 100% !important
                            }
                        
                            .r17-i {
                                background-color: #eff2f7 !important;
                                padding-bottom: 20px !important;
                                padding-left: 15px !important;
                                padding-right: 15px !important;
                                padding-top: 20px !important
                            }
                        
                            .r18-i {
                                color: #3b3f44 !important;
                                padding-bottom: 0px !important;
                                padding-top: 15px !important;
                                text-align: center !important
                            }
                        
                            .r19-i {
                                color: #3b3f44 !important;
                                padding-bottom: 0px !important;
                                padding-top: 0px !important;
                                text-align: center !important
                            }
                        
                            .r20-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                width: 100% !important
                            }
                        
                            .r21-i {
                                padding-bottom: 15px !important;
                                padding-left: 0px !important;
                                padding-right: 0px !important;
                                padding-top: 0px !important
                            }
                        
                            .r22-c {
                                box-sizing: border-box !important;
                                text-align: center !important;
                                valign: top !important;
                                width: 129px !important
                            }
                        
                            .r23-o {
                                border-style: solid !important;
                                margin: 0 auto 0 auto !important;
                                width: 129px !important
                            }
                        
                            body {
                                -webkit-text-size-adjust: none
                            }
                        
                            .nl2go-responsive-hide {
                                display: none
                            }
                        
                            .nl2go-body-table {
                                min-width: unset !important
                            }
                        
                            .mobshow {
                                height: auto !important;
                                overflow: visible !important;
                                max-height: unset !important;
                                visibility: visible !important
                            }
                        
                            .resp-table {
                                display: inline-table !important
                            }
                        
                            .magic-resp {
                                display: table-cell !important
                            }
                        }
                    </style>
                    <style type="text/css">
                        p,
                        h1,
                        h2,
                        h3,
                        h4,
                        ol,
                        ul,
                        li {
                            margin: 0;
                        }
                        
                        a,
                        a:link {
                            color: #696969;
                            text-decoration: underline
                        }
                        
                        .nl2go-default-textstyle {
                            color: #3b3f44;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 16px;
                            line-height: 1.5;
                            word-break: break-word
                        }
                        
                        .default-button {
                            color: #ffffff;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 16px;
                            font-style: normal;
                            font-weight: bold;
                            line-height: 1.15;
                            text-decoration: none;
                            word-break: break-word
                        }
                        
                        .default-heading2 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 32px;
                            word-break: break-word
                        }
                        
                        .default-heading3 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 24px;
                            word-break: break-word
                        }
                        
                        .default-heading4 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 18px;
                            word-break: break-word
                        }
                        
                        .default-heading1 {
                            color: #1F2D3D;
                            font-family: arial, helvetica, sans-serif;
                            font-size: 36px;
                            word-break: break-word
                        }
                        
                        a[x-apple-data-detectors] {
                            color: inherit !important;
                            text-decoration: inherit !important;
                            font-size: inherit !important;
                            font-family: inherit !important;
                            font-weight: inherit !important;
                            line-height: inherit !important;
                        }
                        
                        .no-show-for-you {
                            border: none;
                            display: none;
                            float: none;
                            font-size: 0;
                            height: 0;
                            line-height: 0;
                            max-height: 0;
                            mso-hide: all;
                            overflow: hidden;
                            table-layout: fixed;
                            visibility: hidden;
                            width: 0;
                        }
                    </style>
                    <!--[if mso]><xml> <o:OfficeDocumentSettings> <o:AllowPNG/> <o:PixelsPerInch>96</o:PixelsPerInch> </o:OfficeDocumentSettings> </xml><![endif]-->
                    <style type="text/css">
                        a:link {
                            color: #696969;
                            text-decoration: underline;
                        }
                    </style>
                </head>
                        
                <body bgcolor="#ffffff" text="#3b3f44" link="#696969" yahoo="fix" style="background-color: #ffffff;">
                    <table cellspacing="0" cellpadding="0" border="0" role="presentation" class="nl2go-body-table" width="100%"
                        style="background-color: #ffffff; width: 100%;">
                        <tr>
                            <td>
                                <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="600" align="center"
                                    class="r0-o" style="table-layout: fixed; width: 600px;">
                                    <tr>
                                        <td valign="top" class="r1-i" style="background-color: #ffffff;">
                                            <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="100%"
                                                align="center" class="r3-o" style="table-layout: fixed; width: 100%;">
                                                <tr>
                                                    <td class="r4-i" style="padding-bottom: 20px; padding-top: 20px;">
                                                        <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                                            role="presentation">
                                                            <tr>
                                                                <th width="100%" valign="top" class="r5-c" style="font-weight: normal;">
                                                                    <table cellspacing="0" cellpadding="0" border="0"
                                                                        role="presentation" width="100%" class="r6-o"
                                                                        style="table-layout: fixed; width: 100%;">
                                                                        <tr>
                                                                            <td valign="top" class="r7-i">
                                                                                <table width="100%" cellspacing="0" cellpadding="0"
                                                                                    border="0" role="presentation">
                                                                                    <tr>
                                                                                        <td class="r2-c" align="center">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="276" class="r3-o"
                                                                                                style="table-layout: fixed; width: 276px;">
                                                                                                <tr>
                                                                                                    <td class="r8-i"
                                                                                                        style="font-size: 0px; line-height: 0px; padding-bottom: 15px; padding-top: 15px;">
                                                                                                        <img src="https://img.mailinblue.com/8315004/images/content_library/original/6731007acf0be6181c8e9bb4.png"
                                                                                                            width="276" border="0"
                                                                                                            style="display: block; width: 100%;">
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r11-i nl2go-default-textstyle"
                                                                                                        style="color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-top: 15px; text-align: center;">
                                                                                                        <div>
                                                                                                            <h1 class="default-heading1"
                                                                                                                style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 36px; word-break: break-word;">
                                                                                                                Yêu cầu hóa đơn điện tử
                                                                                                            </h1>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r12-c nl2go-default-textstyle"
                                                                                            align="left"
                                                                                            style="color: #3b3f44; font-family: arial,helvetica,sans-serif; font-size: 16px; line-height: 1.5; word-break: break-word; padding-top: 15px; text-align: left; valign: top;">
                                                                                            <div>
                                                                                                <h4 class="default-heading4"
                                                                                                    style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 18px; word-break: break-word;">
                                                                                                    Chào {{contact.EMAIL}},</h4>
                                                                                                <p style="margin: 0;"><br>Mã đơn hàng : 
                                                                                                     
                                                                                                    """+codeHD+"""
                                                                                                    </p>
                                                                                                <p style="margin: 0;"> </p>
                                                                                                <p style="margin: 0;">Bên mình vừa tiếp
                                                                                                    nhận được ý kiến của bạn về việc cấp
                                                                                                    hóa đơn điện tử của hóa đơn bạn mua,
                                                                                                    mời bạn ấn nút ở phía dưới để tải
                                                                                                    hóa đơn về.</p>
                                                                                                <p style="margin: 0;"> </p>
                                                                                                <p style="margin: 0;">Nếu bạn quan tâm,
                                                                                                    thì hãy tới trang <a
                                                                                                        href="http://localhost:8080/onepoly/home"
                                                                                                        target="_blank"
                                                                                                        style="color: #696969; text-decoration: underline;">onepoly</a>
                                                                                                    để tìm kiếm cho bản thân mình những
                                                                                                    món đồ phú hợp.</p>
                                                                                                <p style="margin: 0;"> </p>
                                                                                                <h4 class="default-heading4"
                                                                                                    style="margin: 0; color: #1f2d3d; font-family: arial,helvetica,sans-serif; font-size: 18px; word-break: break-word;">
                                                                                                    Chúc bạn một ngày mới tốt lành &lt;3
                                                                                                </h4>
                                                                                                <p style="margin: 0;"> </p>
                                                                                            </div>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r13-c" align="center"
                                                                                            style="align: center; padding-bottom: 15px; padding-top: 15px; valign: top;">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="300" class="r14-o"
                                                                                                style="background-color: #1B1B1B; border-collapse: separate; border-color: #1B1B1B; border-radius: 4px; border-style: solid; border-width: 0px; table-layout: fixed; width: 300px;">
                                                                                                <tr>
                                                                                                    <td height="18" align="center"
                                                                                                        valign="top"
                                                                                                        class="r15-i nl2go-default-textstyle"
                                                                                                        style="word-break: break-word; background-color: #1B1B1B; border-radius: 4px; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px; font-style: normal; line-height: 1.15; padding-bottom: 12px; padding-left: 5px; padding-right: 5px; padding-top: 12px; text-align: center;">
                                                                                                        <a href="
                                                                                                        """+hosst+"""
                                                                                                        "
                                                                                                            class="r16-r default-button"
                                                                                                            target="_blank" data-btn="1"
                                                                                                            style="font-style: normal; font-weight: bold; line-height: 1.15; text-decoration: none; word-break: break-word; word-wrap: break-word; display: block; -webkit-text-size-adjust: none; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px;">
                                                                                                            <span>Tải hóa đơn</span></a>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </th>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                            <table cellspacing="0" cellpadding="0" border="0" role="presentation" width="100%"
                                                align="center" class="r3-o" style="table-layout: fixed; width: 100%;">
                                                <tr>
                                                    <td class="r17-i"
                                                        style="background-color: #eff2f7; padding-bottom: 20px; padding-top: 20px;">
                                                        <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                                            role="presentation">
                                                            <tr>
                                                                <th width="100%" valign="top" class="r5-c" style="font-weight: normal;">
                                                                    <table cellspacing="0" cellpadding="0" border="0"
                                                                        role="presentation" width="100%" class="r6-o"
                                                                        style="table-layout: fixed; width: 100%;">
                                                                        <tr>
                                                                            <td valign="top" class="r7-i"
                                                                                style="padding-left: 15px; padding-right: 15px;">
                                                                                <table width="100%" cellspacing="0" cellpadding="0"
                                                                                    border="0" role="presentation">
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r18-i nl2go-default-textstyle"
                                                                                                        style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; padding-top: 15px; text-align: center;">
                                                                                                        <div>
                                                                                                            <p style="margin: 0;">
                                                                                                                <strong>OnePoLySneaker</strong>
                                                                                                            </p>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r19-i nl2go-default-textstyle"
                                                                                                        style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; text-align: center;">
                                                                                                        <div>
                                                                                                            <p
                                                                                                                style="margin: 0; font-size: 14px;">
                                                                                                                BacTuLiem, 000084, HaNoi
                                                                                                            </p>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r9-c" align="left">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" class="r10-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td align="center" valign="top"
                                                                                                        class="r18-i nl2go-default-textstyle"
                                                                                                        style="font-family: arial,helvetica,sans-serif; word-break: break-word; color: #3b3f44; font-size: 18px; line-height: 1.5; padding-top: 15px; text-align: center;">
                                                                                                        <div>
                                                                                                            <p
                                                                                                                style="margin: 0; font-size: 14px;">
                                                                                                                This email was sent
                                                                                                                to {{contact.EMAIL}}</p>
                                                                                                            <p
                                                                                                                style="margin: 0; font-size: 14px;">
                                                                                                                Chúc bạn có một ngày vui
                                                                                                                vẻ &lt;3</p>
                                                                                                        </div>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="r20-c" align="center">
                                                                                            <table cellspacing="0" cellpadding="0"
                                                                                                border="0" role="presentation"
                                                                                                width="100%" align="center" class="r3-o"
                                                                                                style="table-layout: fixed; width: 100%;">
                                                                                                <tr>
                                                                                                    <td valign="top" class="r21-i"
                                                                                                        style="padding-bottom: 15px;">
                                                                                                        <table width="100%"
                                                                                                            cellspacing="0"
                                                                                                            cellpadding="0" border="0"
                                                                                                            role="presentation">
                                                                                                            <tr>
                                                                                                                <td class="r22-c"
                                                                                                                    align="center">
                                                                                                                    <table
                                                                                                                        cellspacing="0"
                                                                                                                        cellpadding="0"
                                                                                                                        border="0"
                                                                                                                        role="presentation"
                                                                                                                        width="129"
                                                                                                                        class="r23-o"
                                                                                                                        style="table-layout: fixed;">
                                                                                                                        <tr>
                                                                                                                            <td height="48"
                                                                                                                                style="font-size: 0px; line-height: 0px;">
                                                                                                                                <a
                                                                                                                                    href="https://www.brevo.com?utm_source=logo_client&utm_medium=email"><img
                                                                                                                                        src="https://creative-assets.mailinblue.com/rnb-assets/en.png"
                                                                                                                                        width="129"
                                                                                                                                        height="48"
                                                                                                                                        border="0"
                                                                                                                                        style="display: block; width: 100%;"></a>
                                                                                                                            </td>
                                                                                                                        </tr>
                                                                                                                    </table>
                                                                                                                </td>
                                                                                                            </tr>
                                                                                                        </table>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </th>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                        
                </html>
                """;
        this.emailSenderService.sendEmail(receiverEmail,subject,content);
    }

    protected void setUpToken(Integer id, String nameTable, String email) {
        Token token = new Token();
        token.setIdAccount(id);
        token.setNameTable(nameTable);
        token.setEmailSend(email);
        token.setStatus(1);
        // Không thiết lập token.setId() ở đây
        Token tokenSave = this.tokenService.saveGetId(token); // Gọi phương thức lưu
        this.templateEmailExchangePassWord(tokenSave.getEmailSend(), "http://localhost:8080/forgotPassword/changepassword/" + tokenSave.getId());
    }
}
