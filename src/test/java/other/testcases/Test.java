package other.testcases;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Test {

    public static void main(String[] args) {


//        String[][] names = new String[3][2];
//        String[][] names = { {"Sam", "Smith"}, {"Robert", "Delgro"}, {"James", "Gosling"}, };
//
//        names [0][0] = "Test1";
//        names [0][1] = "one";
//
//        names [1][0] = "Test1";
//        names [1][1] = "two";
//
//        names [2][0] = "Test3";
//        names [2][1] = "three";
//
//        System.out.println("answer : "+ Arrays.deepToString(names));

//        System.out.println("answer : "+names [0][0]+ ":" + names [0][1]);
//        System.out.println("answer : "+names [1][0]+ ":" + names [1][1]);

//        String test = "0.10";
//
//        double dtest = Double.parseDouble(test);
//
//        DecimalFormat df = new DecimalFormat("0.00");
//
//        System.out.println(df.format(dtest));


//        String[] names = {"Sam", "Smith", "Robert"};
//        String[] names2 = {"Rob", "Robert", "Ash"};
//
//        Set<String> s = new HashSet<String>();
//
//        for (String name : names) {
//            s.add(name);
//        }
//
//        for (String name : names2) {
//            if (!s.add(name)) {
//                System.out.println(name);
//            }
//        }

//        System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");


        String host = "smtp.gmail.com";
        final String user = "krishpavulur@gmail.com";//change accordingly
        final String password = "vglgzybjkfjekibl";//change accordingly

        String to = "krish.pavulur@bell.ca";//change accordingly

        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("javatpoint");
            message.setText("This is simple program of sending email using JavaMail API");

            //send the message
            Transport.send(message);

            System.out.println("message sent successfully...");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
