package com.example.SpringBootTurialVip.shopservice;


import com.example.SpringBootTurialVip.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

	//Lưu customer
	public User saveUser(User user);

	//Kiếm user = email
	public User getUserByEmail(String email);

	//Lấy danh sách user
	public List<User> getUsers(String role);


	public Boolean updateAccountStatus(Integer id, Boolean status);

	public void increaseFailedAttempt(User user);

	public void userAccountLock(User user);

	public boolean unlockAccountTimeExpired(User user);

	public void resetAttempt(int userId);

	public void updateUserResetToken(String email, String resetToken);

	public User getUserByToken(String token);

	public User updateUser(User user);

	public User updateUserProfile(User user, MultipartFile img);

	public User saveAdmin(User user);

	public Boolean existsEmail(String email);


}
