package com.example.SpringBootTurialVip.service;


import com.example.SpringBootTurialVip.constant.PredefinedRole;
import com.example.SpringBootTurialVip.dto.request.UserCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserUpdateRequest;
import com.example.SpringBootTurialVip.dto.request.VerifyAccountRequest;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.mapper.UserMapper;
import com.example.SpringBootTurialVip.repository.RoleRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service//Layer này sẽ gọi xuống layer repository
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //Khai báo đối tượng mapper
    @Autowired
    private UserMapper userMapper;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    //Tạo tài khoản
    public User createUser(UserCreationRequest request){

//        if(userRepository.existsByUsername(request.getUsername()))
//            throw new RuntimeException("User đã tồn tại");

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);//Sử dụng class AppException để báo lỗi đã define tại ErrorCode

//        if(userRepository.existsByUsername(request.getUsername()))
//            throw new RuntimeException(""); dành cho khi chưa định nghĩa lỗi trong ErrorCode

        //Công dụng của @Builder bên DTO UserCreationRequest
//        UserCreationRequest userCreationRequest=UserCreationRequest.builder()
//                .username(request.getUsername())
//                .password(request.getPassword())
//                .email(request.getEmail())
//                .bod(request.getBod())
//                .fullname(request.getFullname())
//                .parentid(request.getParentid())
//                .phone(request.getPhone())
//                .gender(request.getGender())
//                .build();

          //Khi ko có mapper
//        User user=new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setEmail(request.getEmail());
//        user.setBod(request.getBod());
//        user.setFullname(request.getFullname());
//        user.setParentid(request.getParentid());
//        user.setPhone(request.getPhone());
//        user.setGender(request.getGender());



        User user=userMapper.toUser(request);//Khi có mapper

        //Áp dụng thuật toán mã hóa mật khẩu bytecrypt
        //Bytecrypt nó là 1 implement của PasswordEncoder
        //PasswordEncoder là 1 interface do Spring Security cung cấp
//        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);//tham số truyền vào là độ mạnh của mã hóa

        //Mã hóa password user
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //Set mặc định khi tạo user mới là sẽ tạo cho customer chứ ko phải admin hay staff
//        HashSet<String> roles=new HashSet<>();
        HashSet<Role> roles=new HashSet<>();
//        roles.add(Role.CUSTOMER.name());
//        user.setRoles(roles); //comment lỗi @Manytomany trong entity user


        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setVerification_code(generateVerificationCode());
        user.setVerification_expired_at(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userRepository.save(user);

    }

    //Hàm xác thực người dùng
    public void verifyUser(VerifyAccountRequest input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerification_expired_at().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerification_code().equals(input.getVerificationcode())) {
                user.setEnabled(true);
                user.setVerification_code(null);
                user.setVerification_expired_at(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    //Hàm gửi lại xác thực cho người dùng
    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerification_code(generateVerificationCode());
            user.setVerification_expired_at(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerification_code();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
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
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


    //Danh sách user
    @PreAuthorize("hasRole('ADMIN')")//Chỉ cho phép admin
    public List<User> getUsers(){
        log.info("PreAuthorize đã chặn nếu ko có quyền truy cập nên ko thấy dòng này trong console ," +
                "chỉ có Admin mới thấy đc dòng này sau khi đăng nhập ");
        return userRepository.findAll();
    }

//    public Optional<User> getUserName(String username){
//        return userRepository.findByUsername(username);
//    }


    @PostAuthorize("hasRole('ADMIN') || returnObject.username == authentication.name")//Post sẽ run method trc r check sau
    //Như khai báo thì chỉ cho phép truy cập nếu id kiếm trùng id đang login
    //Kiếm user băằng ID
    public UserResponse getUserById(String id){
        return userMapper.toUserResponse(userRepository.findById(id).
                orElseThrow(()->new RuntimeException("User not found!")));//Nếu ko tìm thấy báo lỗi
    }

//    public User updateUser(String userId ,UserUpdateRequest request){
//         User user=getUserById(userId);
//        //Khi chưa có mapper
////        user.setPassword(request.getPassword());
////        user.setEmail(request.getEmail());
////        user.setBod(request.getBod());
////        user.setFullname(request.getFullname());
////        user.setParentid(request.getParentid());
////        user.setPhone(request.getPhone());
////        user.setGender(request.getGender());
//
//        userMapper.updateUser(user,request);
//
//        return userRepository.save(user);
//    }

    //Lấy thông tin hiện tại đang log in
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        //Tên user đang log in
        String name=context.getAuthentication().getName();

        User user =userRepository.findByUsername(name).orElseThrow(
                () -> new AppException((ErrorCode.USER_NOT_EXISTED)));

                return userMapper.toUserResponse(user);
    }

    //Cập nhật user
//    public UserResponse updateUser(String userId ,UserUpdateRequest request){
//    User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found!"));
//
//    userMapper.updateUser(user,request);
//
//    return userMapper.toUserResponse(userRepository.save(user));
//}

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    //Xóa user
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
