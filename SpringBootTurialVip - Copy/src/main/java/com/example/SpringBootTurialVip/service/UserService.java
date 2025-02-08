package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.UserCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.mapper.UserMapper;
import com.example.SpringBootTurialVip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//Layer này sẽ gọi xuống layer repository
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //Khai báo đối tượng mapper
    @Autowired
    private UserMapper userMapper;

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


        return userRepository.save(user);

    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

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

    public UserResponse updateUser(String userId ,UserUpdateRequest request){
    User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found!"));

    userMapper.updateUser(user,request);

    return userMapper.toUserResponse(userRepository.save(user));
}

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
