package gr.hua.dit.HomeSharing.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import gr.hua.dit.HomeSharing.entities.Home;
import gr.hua.dit.HomeSharing.entities.Rental;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /** Sends a simple HTML welcome mail. */
    @Async               // so the HTTP thread isn’t blocked
    public void sendWelcomeMail(String to, String firstName) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
            helper.setTo(to);
            helper.setSubject("Welcome to HomeSharing!");
            helper.setText(
                "<h2>Hello " + firstName + " 👋</h2>"
              + "<p>Your registration was successful. "
              + "You can now log in and start using HomeSharing.</p>",
              true);      // true => HTML
            mailSender.send(msg);
        } catch (MessagingException ex) {
            // log but never break the registration flow
            System.err.println("E-mail could not be sent: " + ex.getMessage());
        }
    }

    @Async
    public void sendHomeRequestAcceptedMail(String to, String firstName, Home home) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
            helper.setTo(to);
            helper.setSubject("Home Request Accepted");
            helper.setText(
                "<h2>Hello " + firstName + " 👋</h2>"
              + "<p>Your request for the home at <strong>" + home.getCharacteristics().getAddress() + "</strong> has been accepted.</p>"
              + "<p>You home is now open to rentals.</p>",
              true);      // true => HTML
            mailSender.send(msg);
        } catch (MessagingException ex) {
            // log but never break the flow
            System.err.println("E-mail could not be sent: " + ex.getMessage());
        }
    }

    @Async
    public void sendHomeRequestRejectedMail(String to, String firstName, Home home) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
            helper.setTo(to);
            helper.setSubject("Home Request Rejected");
            helper.setText(
                "<h2>Hello " + firstName + " 👋</h2>"
              + "<p>We're sorry to announce that your request for the home at <strong>" + home.getCharacteristics().getAddress() + "</strong> has been rejected.</p>"
              + "<p>Please contact the owner if you want more information.</p>",
              true);      // true => HTML
            mailSender.send(msg);
        } catch (MessagingException ex) {
            // log but never break the flow
            System.err.println("E-mail could not be sent: " + ex.getMessage());
        }
    }

    @Async
    public void sendRentalRequestApprovedMail(String to, String firstName, Home home, Rental rental) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
            helper.setTo(to);
            helper.setSubject("Rental Request Approved");
            helper.setText(
                "<h2>Hello " + firstName + " 👋</h2>"
              + "<p>Your request for the home at <strong>" + home.getCharacteristics().getAddress() + "</strong> has been approved.</p>"
              + "<p>Your rental is now confirmed for the period: <strong>" + rental.getStartDate() + " to " + rental.getEndDate() + "</strong>.</p>",
              true);      // true => HTML
            mailSender.send(msg);
        } catch (MessagingException ex) {
            // log but never break the flow
            System.err.println("E-mail could not be sent: " + ex.getMessage());
        }
    }

    @Async
    public void sendRentalRequestRejectedMail(String to, String firstName, Home home, Rental rental) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
            helper.setTo(to);
            helper.setSubject("Rental Request Rejected");
            helper.setText(
                "<h2>Hello " + firstName + " 👋</h2>"
              + "<p>We're sorry to announce that your rental for the home at <strong>" + home.getCharacteristics().getAddress() + "</strong> for period " + rental.getStartDate() + " - " + rental.getEndDate() + " has been rejected.</p>"
              + "<p>Please contact the owner if you want more information.</p>",
              true);      // true => HTML
            mailSender.send(msg);
        } catch (MessagingException ex) {
            // log but never break the flow
            System.err.println("E-mail could not be sent: " + ex.getMessage());
        }
    }
}
