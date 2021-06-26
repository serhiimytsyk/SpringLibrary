package smytsyk.final_project.spring.library.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import smytsyk.final_project.spring.library.library.entitiy.Book;
import smytsyk.final_project.spring.library.library.entitiy.Order;
import smytsyk.final_project.spring.library.library.entitiy.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.format.DateTimeFormatter;

@Service
public class EmailSenderService {
    private final JavaMailSender emailSender;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String PATH_TO_PICTURE = "C:\\Users\\Сергей\\Desktop\\1\\book.jpg";

    @Autowired
    public EmailSenderService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    private void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("mailsender.smytsyk@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Picture", file);

        emailSender.send(message);
    }

    public void sendNotification(User user, Book book, Order order) {
        String text = "Dear, " + user.getFirstName() + " " + user.getLastName() + "!" + System.lineSeparator() +
                "You should return book №" + book.getId() + " " + book.getAuthor() + " " + book.getName() +
                 " " + book.getPublisher() + " " + book.getPublicationDate() + System.lineSeparator() +
                "not later than " + order.getReturnDate().format(formatter) + " or you will be required to pay fine!" +
                System.lineSeparator() + " Respectfully yours, " + System.lineSeparator() + "Library";
        try {
            sendMessageWithAttachment(user.getEmail(), "Book return notification", text, PATH_TO_PICTURE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
