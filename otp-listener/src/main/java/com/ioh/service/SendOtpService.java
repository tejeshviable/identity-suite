package com.ioh.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class SendOtpService {

    @Value("${template.retrieve.templateId}")
    String retrieveTemplateIdId;
    @Value("${template.retrieve.template}")
    String retrieveTemplate;

    public void sendOtp(String mobileNumber, String otp) {
            sendSMSUsingNHATemplate(retrieveTemplate.replace("{0}", otp), mobileNumber, retrieveTemplateIdId);

    }

    public void sendSMSUsingNHATemplate(String template, String mobileNumber, String templateId) {

        String message = template;

        String postData = "username=" + "hprsms" + "&password=" + "*i8p5E-B" + "&type=0&dlr=1" + "&destination=" + mobileNumber
                + "&source=" + "NHASMS" + "&message=" + message + "&entityid=" + "1001548700000010184" + "&tempid=" + templateId;

        try {
            String response = sendSingleSMS("https://sms6.rmlconnect.net:8443/bulksms/bulksms?", postData, mobileNumber);
            log.info("SendOtpService::sendSMSUsingNHATemplate::response:{}",response);
        } catch (Exception ex) {
            log.error("SendOtpService::sendSMSUsingNHATemplate::SMS Error: To mobile Number: {} TemplateId:{} GWResponse:{}", mobileNumber, templateId,
                    ex.toString());
        }
    }


    private String sendSingleSMS(String smsURL, String postData, String mnumber) {
        InputStream is = null;
        OutputStream os = null;
        int responseCode = 0;
        StringBuilder responseBuffer = new StringBuilder();
        String responseMsg = "";
        try {
            URL url = new URL(smsURL);
            URLConnection conn = url.openConnection();

            if (conn instanceof HttpsURLConnection) {
                log.debug("HTTPS");
                HttpsURLConnection con = (HttpsURLConnection) conn;
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setConnectTimeout(20000);
                con.setReadTimeout(20000);
                con.connect();
                try {
                    os = con.getOutputStream();
                } catch (Exception e) {
                    os = null;
                    log.error("SendOtpService::sendSingleSMS:: ## Exception while fetching output stream of the connection. URL --> " + url);
                }
                if (os != null)
                    os.write(postData.getBytes(StandardCharsets.UTF_8));

                responseCode = con.getResponseCode();
                try {
                    is = con.getInputStream();
                } catch (Exception e) {
                    is = null;
                    log.error("SendOtpService::sendSingleSMS::## Exception while fetching input stream of the connection. URL --> " + url);
                }
            } else {
                log.debug("HTTP");
                HttpURLConnection con = (HttpURLConnection) conn;
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setConnectTimeout(20000);
                con.setReadTimeout(20000);
                con.connect();
                try {
                    os = con.getOutputStream();
                } catch (Exception e) {
                    os = null;
                    responseBuffer.append("SendOtpService::sendSingleSMS::Exception while sending SMS " + e.getMessage());
                }
                if (os != null)
                    os.write(postData.getBytes(StandardCharsets.UTF_8));

                responseCode = con.getResponseCode();
                try {
                    is = con.getInputStream();
                } catch (Exception e) {
                    is = null;
                    responseBuffer.append("SendOtpService::sendSingleSMS::Exception while sending SMS " + e.getMessage());
                }
            }
            if (responseCode == HttpsURLConnection.HTTP_OK && is != null) {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";

                while ((inputLine = responseReader.readLine()) != null) {
                    responseBuffer.append(inputLine);
                }

                responseReader.close();
                is.close();
                responseMsg = responseBuffer.toString();
                if (responseMsg.contains("Message Rejected")) {
                    throw new Exception(responseMsg);
                }
            }
        } catch (Exception e) {
            responseBuffer.append("SendOtpService::sendSingleSMS::Exception while sending SMS " + e.getMessage());
            log.error("SendOtpService::sendSingleSMS::Exception occured while sending SMS:", e);
        }

        return responseBuffer.toString();
    }

}
