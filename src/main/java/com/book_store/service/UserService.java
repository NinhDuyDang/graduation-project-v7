package com.book_store.service;

import com.book_store.dto.UserDto;
import com.book_store.entity.Customer;
import com.book_store.entity.User;
import com.book_store.repository.CustomerRepository;
import com.book_store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CustomerRepository  customerRepository;

    public User getById(int id) {
        return userRepository.getById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Congratulations";
        String senderName = "This is a project";
        String content = "<table style=\"width: 100% !important\" >\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <div>\n" +
                "                            <h2>Hello " + user.getUsername() + "</h2>\n" +
                "                        </div>\n" +
                "                        <div>\n" +
                "                            Recently, you registered your account in our system. We are delighted that you chose our store.\n" +
                "                        </div>\n" +
                "                        <br>\n" +
                "\n" +
                "                        <div>\n" +
                "                            Enjoy shopping on our website: \n" +
                "                            <a href=\"http://160.30.161.63:8080/\" target=\"_blank\">Click to shop</a>\n" +
                "                        </div>\n" +
                "\n" +
                "                        <br>\n" +
                "                        <div>\n" +
                "                            Best regards,\n" +
                "                            <h4>This is a project</h4>\n" +
                "                        </div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("EdulanSupport@gmail.com", senderName);
        helper.setTo(user.getCustomer().getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    public void deleteUser(int id){
        userRepository.deleteById(id);
    }

    public UserDto getUserByEmail(final String email) {
        Customer user = customerRepository.findByEmail(email);
        if (user != null) {
            UserDto userDto = new UserDto(user.getId(), user.getUser().getUsername(), user.getEmail());
            return userDto;
        }
        throw new RuntimeException("No user available for the given user name");
    }
}
