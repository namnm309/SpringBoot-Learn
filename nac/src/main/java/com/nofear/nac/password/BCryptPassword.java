package com.nofear.nac.password;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//Class nay ma hoa mat khau de nap vao database
@Component
public class BCryptPassword extends BCryptPasswordEncoder {
}
