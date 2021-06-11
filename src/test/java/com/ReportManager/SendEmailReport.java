package com.ReportManager;

/**
 * Created by Krish on 21.07.2018.
 **/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmailReport {
  public static void main(String[] args) throws IOException {

    String to = "krish.pavulur@bell.ca";// change accordingly
    final String user = "krish.pavulur@bell.ca";// change accordingly
    final String password = "Bell1234";// change accordingly

    // 1) get the session object
    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", "app-mail.bell.corp.bce.ca");
    properties.setProperty("mail.smtp.socketFactory.port", "80");
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.socketFactory.fallback", "true");
    properties.put("mail.smtp.starttls.enable", "false");
    properties.put("mail.smtp.ssl.enable", "false");

    // get Session
    Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
      }
    });

    // 2) compose message
    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(user));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject("Message Alert");

      // zip file
      SendEmailReport appZip = new SendEmailReport();
      String input_folder = "C:\\Automation\\TestNGParallelDemo\\test-output\\14062018";
      String output_zip_folder = "C:\\Automation\\TestNGParallelDemo\\test-output\\14062018.zip";
      appZip.pack(input_folder, output_zip_folder);


      // 3) create MimeBodyPart object and set your message text
      BodyPart messageBodyPart1 = new MimeBodyPart();
      messageBodyPart1.setText("C:\\\\Automation\\\\TestNGParallelDemo\\\\test-output\\\\14062018");

      // 4) create new MimeBodyPart object and set DataHandler object to this object
      MimeBodyPart messageBodyPart2 = new MimeBodyPart();

      // change accordingly
      DataSource source = new FileDataSource(output_zip_folder);
      messageBodyPart2.setDataHandler(new DataHandler(source));
      messageBodyPart2.setFileName("Report_name.zip");


      // 5) create Multipart object and add MimeBodyPart objects to this object
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart1);
      multipart.addBodyPart(messageBodyPart2);

      // 6) set the multiplart object to the message object
      message.setContent(multipart);

      // 7) send message
      Transport.send(message);

      System.out.println("message sent....");
    } catch (MessagingException ex) {
      ex.printStackTrace();
    }



  }


  public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
    Path p;

    p = Files.createFile(Paths.get(zipFilePath));

    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
      Path pp = Paths.get(sourceDirPath);
      Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
        try {
          zs.putNextEntry(zipEntry);
          Files.copy(path, zs);
          zs.closeEntry();
        } catch (IOException e) {
          System.err.println(e);
        }
      });
    }
  }

}
