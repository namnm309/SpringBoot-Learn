package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service // Đánh dấu đây là một Spring Service (Bean) để Spring quản lý
public class JwtService {

    // Lấy giá trị secret key từ file application.properties
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    // Lấy giá trị thời gian hết hạn của token từ file application.properties
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Phương thức này trích xuất username từ token (nằm trong claim 'sub')
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Phương thức này dùng để trích xuất một claim cụ thể từ token.
     *
     * @param token: Token JWT
     * @param claimsResolver: Một function để lấy giá trị từ Claims
     * @return Giá trị của claim cần lấy
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Phương thức tạo token chỉ với username của UserDetails
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Phương thức tạo token với các claims bổ sung
     *
     * @param extraClaims: Các claims bổ sung (ví dụ: roles, permissions,...)
     * @param userDetails: Thông tin người dùng
     * @return Token JWT đã tạo
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Phương thức lấy thời gian hết hạn của token từ properties
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Phương thức này xây dựng token với thông tin của user
     *
     * @param extraClaims: Các thông tin bổ sung cho token (roles, email,...)
     * @param userDetails: Thông tin của user
     * @param expiration: Thời gian hết hạn của token
     * @return Token JWT
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims) // Thêm các thông tin bổ sung vào token
                .setSubject(userDetails.getUsername()) // Username của user
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thời gian phát hành token
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Thời gian hết hạn của token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký token với thuật toán HS256
                .compact(); // Trả về token dạng chuỗi
    }

    /**
     * Kiểm tra token có hợp lệ không
     *
     * @param token: Token JWT
     * @param userDetails: Thông tin user để so sánh
     * @return true nếu token hợp lệ, false nếu không
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Lấy username từ token
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Kiểm tra token có hết hạn chưa
     *
     * @param token: Token JWT
     * @return true nếu token đã hết hạn, false nếu chưa
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // So sánh thời gian hết hạn với thời gian hiện tại
    }

    /**
     * Trích xuất thời gian hết hạn của token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Trích xuất toàn bộ thông tin Claims từ token
     *
     * @param token: Token JWT
     * @return Claims chứa thông tin của token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Set khóa bí mật để giải mã token
                .build()
                .parseClaimsJws(token) // Giải mã token JWT
                .getBody(); // Lấy nội dung claims
    }

    /**
     * Trả về khóa bí mật dùng để ký token JWT
     *
     * @return Key được tạo từ secretKey
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Giải mã secret key từ base64
        return Keys.hmacShaKeyFor(keyBytes); // Tạo key để ký token với thuật toán HS256
    }
}
