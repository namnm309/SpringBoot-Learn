package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.AuthenticationRequest;
import com.example.SpringBootTurialVip.dto.request.LogoutRequest;
import com.example.SpringBootTurialVip.dto.request.VerifyTokenRequest;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;
import com.example.SpringBootTurialVip.dto.response.VerifyTokenResponse;
import com.example.SpringBootTurialVip.entity.InvalidatedToken;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.repository.InvalidatedTokenRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor//Autowired các bean
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
//@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    //Lấy thông tin user
    @Autowired
    UserRepository userRepository;

    @NonFinal//đánh dấu ko inject vào constructure
    @Value("${jwt.signerKey}")//đọc key từ file properties
    protected  String SIGN_KEY;

    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

//    public AuthenticationService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    //Check user nhập true or wrong
//    public boolean authencicate(AuthenticationRequest request){
//        //Tạo method lấy username bên repository
//         var user=userRepository.findByUsername(request.getUsername())
//                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//         //Tạo obj đ dùng nó sử dụng method matches trong lớp PasswordEncoder
//        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);
//        //So sánh 2 password
//        return passwordEncoder.matches(request.getPassword(), user.getPassword());
//    }
    @Override
    public VerifyTokenResponse introspect(VerifyTokenRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
        }

        return VerifyTokenResponse.builder().valid(isValid).build();
    }
    @Override
    public AuthenticationResponse authencicate(AuthenticationRequest request){
        //Tạo obj đ dùng nó sử dụng method matches trong lớp PasswordEncoder
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);

        //Tạo method lấy username bên repository
        var user=userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        //Xác định user có login thành công ko
        boolean authenticated=passwordEncoder.matches(request.getPassword(),user.getPassword());

        // Kiểm tra xem tài khoản có bị vô hiệu hóa không
        if (!user.isEnabled()) {
            throw new AppException(ErrorCode.USER_DISABLED); // Tạo error code mới
        }

        //Thông báo
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        //Add thư viện tạo token
        //Sử dụng hàm tạo token
        //Nếu authenticated thành công tạo token
//        var token=generateToken(request.getUsername());
        //Update lại để phân quyền
        var token=generateToken(user);


        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();

//        return AuthenticationResponse.builder()
//                .authenticated(true)
//                .token(token)
//                .build();


    }

    //Tạo method tạo token
    private String generateToken(User user){//truyền username vào token , có thể truyền thêm
        //Thuật toán bảo vệ token
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet=new JWTClaimsSet.Builder()
                .subject(user.getUsername())//đại diện cho user đăng nhập
                .issuer("mNamDEv")//token đc issuser từ ai
                .issueTime(new Date())//time tạo token
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli()//define hết hạn sau 1h
                ))//time tồn tại của token
                .jwtID(UUID.randomUUID().toString())//tạo id cho token
                .claim("email", user.getEmail())
                .claim("scope",buildScope(user))
                .build();

        //Payload
        Payload payload=new Payload(jwtClaimsSet.toJSONObject());

        //Cần 2 param gồm header và payload
        JWSObject jwsObject=new JWSObject(header,payload);

        //Ký token
        //Kí với method MACSigner
        //Cần 1 chuỗi secret 32bytes để sử dụng method MACSigner
        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Không thể tạo token",e);
            throw new RuntimeException(e);
        }
    }

    //Build scope từ 1 user
//    private String buildScope(User user ) {
//        StringJoiner stringJoiner = new StringJoiner(" ");
//        if (!CollectionUtils.isEmpty(user.getRoles()))
//            user.getRoles().forEach(stringJoiner::add); //comment lỗi @Manytomany trong entity user
//        return stringJoiner.toString();
//        }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    //Logout
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    //Tạo method Verify Token
//    public VerifyTokenResponse verifyTokenResponse(VerifyTokenRequest request)
//            throws JOSEException, ParseException {
//        var token=request.getToken();
//
//        //Để verify token thì thư viện nimsbot cung cấp JWSVerifier
//        JWSVerifier verifier=new MACVerifier(SIGN_KEY.getBytes());
//
//        SignedJWT signedJWT=SignedJWT.parse(token);
//
//        //Check xem token hết hạn ?
//        Date expityTime=signedJWT.getJWTClaimsSet().getExpirationTime();
//
//        var verified=signedJWT.verify(verifier);//check
//
//        return VerifyTokenResponse.builder()
//                .valid(verified && expityTime.after(new Date()))
//                .build();
//
//    }
    }




