package com.ReportManager;

import javax.mail.*;
import java.util.Properties;

public class ReadEmail {

    public static void check(String host, String storeType, String user, String password) {

        // System.setProperty("java.net.useSystemProxies", "false");

        try {

            // create properties field
            Properties properties = new Properties();

            // properties.put("proxySet", true);
            // properties.put("socksProxyHost", "");
            // properties.put("socksProxyPort", "8083");

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");

//       properties.setProperty("proxySet", "true");
//      properties.setProperty("mail.pop3.socks.host", "");
//      properties.setProperty("mail.pop3.socks.port", "8083");

            // mail.smtp.proxy.host
            //
            // mail.smtp.proxy.port
            //
            // mail.smtp.proxy.user
            //
            // mail.smtp.proxy.password

            // Mailer mailer = new Mailer(// note: from 5.0.0 on use MailerBuilder instead
            // new ServerConfig("localhost", thePort, theUser, thePasswordd),
            // TransportStrategy.SMTP_PLAIN,
            // new ProxyConfig(proxyHost, proxyPort /*, proxyUser, proxyPassword */)
            // );

            Session emailSession = Session.getDefaultInstance(properties);

            // create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore(storeType);

            store.connect(host, user, password);

            // create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = 10; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                // System.out.println("Text: " + message.getContent().toString());

            }

            // close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String host = "pop.yopmail.com";// change accordingly
        String mailStoreType = "pop3";
        String username = "auto@yopmail.com";// change accordingly
        String password = "auto@yopmail.com";// change accordingly

        check(host, mailStoreType, username, password);

    }

}
