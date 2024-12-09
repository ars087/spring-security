package TestJavaCode.AuthApp.security.utils;

import TestJavaCode.AuthApp.exception.JwtExpirationException;
import TestJavaCode.AuthApp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JWTUtils {


    private final String secretKey = "NewDayfrominItalyyguytytrreewrwwqrafdsgffjloopuytrewqasdfghjkllkmnbvcxzsert";

    private static final long EXPIRATION_TIME_ACCESS_TOKEN = 120000;
    private static final long EXPIRATION_TIME_REFRESH_TOKEN =  86400000;

    public JWTUtils() {

        String secretString = "YOUR_SECRET_KEY"; // Замените на свой секретный ключ
        byte[] keyBytes = secretString.getBytes();

    }


    public String generateAccessToken(UserDetails userDetails) {

        List<String> roleList = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toList();
        Map<String, Object> climesJwt = new HashMap<>();
        climesJwt.put("role", roleList);
        Date issuedDate = new Date(System.currentTimeMillis());
        Date expiredDate = new Date(issuedDate.getTime() + EXPIRATION_TIME_ACCESS_TOKEN);
        return Jwts.builder()
            .setClaims(climesJwt)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(issuedDate)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }


    public String generateRefreshToken( UserDetails userDetails) {
        return Jwts.builder()
            .setClaims(new HashMap<>())
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REFRESH_TOKEN )) // например, 48 часов
            .signWith( SignatureAlgorithm.HS256,secretKey)
            .compact();
    }

    public String extractUsername(String token) {

        return extractClaims(token, Claims::getSubject);
    }


    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) throws  JwtExpirationException {
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
            return claimsTFunction.apply(claims);

    }

    public List<String> extractRoles(String token) {
        return extractClaims(token, claims -> claims.get("role", List.class));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

     public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }
    public  boolean isEqualTo(String token,UserDetails userDetails){

       List<String> sentRole =  extractRoles(token);
       List<String> dataBaseRole = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (sentRole.isEmpty()&& !dataBaseRole.isEmpty()) {
            return false;}

        return true;
    }
}


