package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.LoginUserDto;
import com.example.SpringBootTurialVip.dto.RegisterUserDto;
import com.example.SpringBootTurialVip.dto.VerifyUserDto;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.UserOptionalRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserOptionalRepository userOptionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(//constructor

                                 UserRepository userRepository,
            UserOptionalRepository userOptionalRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.userRepository=userRepository;
        this.authenticationManager = authenticationManager;
        this.userOptionalRepository = userOptionalRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    //Method đăng ký
    public User signup(RegisterUserDto input) {
        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        user.setVerification_code(generateVerificationCode());
        user.setVerfication_expiration(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
//        User user = userRepository.findByEmail(input.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));

                User user = userOptionalRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("Tài khoản không hợp lệ"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt .Xin vui lòng kích hoạt tài khoản của bạn");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return user;
    }

    //Method xác nhận user
    public void verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userOptionalRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerfication_expiration().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerification_code().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerification_code(null);
                user.setVerification_expired(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    //Method gửi lại mã verificatione code nếu
    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userOptionalRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerification_code(generateVerificationCode());
            user.setVerification_expired(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    //Form send email
    private void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerification_code();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our vaccine application!!!</h2>"
                + "<p style=\"font-size: 16px;\">Mời bạn nhập mã code phía dưới để xác thực tài khoản :</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Mã Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    //Method tạo mã verification codee
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
