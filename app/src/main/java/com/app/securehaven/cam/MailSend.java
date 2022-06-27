package com.app.securehaven.cam;

import android.util.Log;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.sql.Timestamp;

public class MailSend {
    static String HTMLContent = "Temp", base64send;
    String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis())); //Get the current time-stamp
    MailSend(String base64){

        //This uses a pre-defined HTML template for body of the e-mail
        HTMLContent="<p style=\"text-align: center;\"><span style=\"font-size: 24px;\"><strong><span style=\"color: rgb(255, 0, 0);\">Secure Haven Intrusion Alert</span></strong></span></p>\n" +
                "<p style=\"text-align: center;\"><u>A Wrong PIN Was Entered On Your Phone</u></p>\n" +
                "<p style=\"text-align: center;\">Device:"+SecurityService.deviceDetails+"</p>\n" +
                "<p style=\"text-align: center;\">Time Stamp:"+timestamp+"</p>\n" +
                "<p style=\"text-align: center;\">&nbsp;Intruders Image has been attached.</p>\n" +
                "<p style=\"text-align: center;\"><br></p>";
        base64send=base64;
        try {
            if(SecurityService.senderEmail!=null){
                SendMail(HTMLContent);
            }
        } catch (Exception e) {
            Log.d("SecureHaven.MailSend",e.getMessage());
        }
    }

    /**
     * Send the mail and checks response
     * @param HTML The predefined HTML string
     */
    static void SendMail(String HTML) throws JSONException, MailjetSocketTimeoutException, MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient("6b7f838c6e7d5a6ff8795390ff72c5d0", "ffade383d7d8e6df04eeabb4a7ad6194", new ClientOptions("v3.1"));
        //The request contains various JSON objects and JSON key-value pairs
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "novaxertz@protonmail.com")
                                        .put("Name", "Secure Haven Admin"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", SecurityService.senderEmail)
                                                .put("Name", "Client")))
                                .put(Emailv31.Message.SUBJECT, "Greetings from Secure Haven")
                                .put(Emailv31.Message.TEXTPART, "Incorrect Pin Alert")
                                .put(Emailv31.Message.HTMLPART, HTMLContent)
                                .put(Emailv31.Message.ATTACHMENTS, new JSONArray()
                                        .put(new JSONObject()
                                                .put("ContentType", "image/bmp")
                                                .put("Filename", "Intruder.bmp")
                                                .put("Base64Content", base64send)))));
        response = client.post(request);
        Log.d("response", String.valueOf(response.getStatus()));
        Log.d("response", String.valueOf(response.getData()));
        Log.d("mailSent to",SecurityService.senderEmail);
    }
}