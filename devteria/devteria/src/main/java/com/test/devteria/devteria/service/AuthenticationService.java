package com.test.devteria.devteria.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.test.devteria.devteria.dto.request.AuthenticationRequest;
import com.test.devteria.devteria.dto.request.IntrospectRequest;
import com.test.devteria.devteria.dto.request.LogoutRequest;
import com.test.devteria.devteria.dto.request.RefreshTokenRequest;
import com.test.devteria.devteria.dto.response.AuthenticationResponse;
import com.test.devteria.devteria.dto.response.IntrospectResponse;
import com.test.devteria.devteria.entity.InvalidatedToken;
import com.test.devteria.devteria.entity.User;
import com.test.devteria.devteria.exception.AppException;
import com.test.devteria.devteria.exception.ErrorCode;
import com.test.devteria.devteria.repository.InvalidatedTokenRepository;
import com.test.devteria.devteria.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    InvalidatedTokenRepository invalidatedTokenRepository;

    PasswordEncoder passwordEncoder ;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected Long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duaration}")
    protected Long REFRESH_DURATION;

    UserRepository userRepository;

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generalToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    // VERIFIER TOKEN
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        boolean invalid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            invalid = false;
        }
        // GET TOKEN FROM REQUEST

        return IntrospectResponse.builder().valid(invalid).build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            // VERIFY TOKEN FROM REQUEST
            SignedJWT signToken = verifyToken(request.getToken(), true);

            // GET ID OF TOKEN
            String jwtIdToken = signToken.getJWTClaimsSet().getJWTID();

            // VERIFY TOKEN EXPIRATION TIME OR NOT
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            // ASSIGN INFOR TOKEN
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwtIdToken)
                    .expiryTime(expiryTime)
                    .build();

            // SAVE TO DATABASE
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {

        // VERIFY TOKEN FROM REQUEST
        SignedJWT signJWT = verifyToken(request.getToken(), true);

        // GET ID OF TOKEN
        String jwtIdToken = signJWT.getJWTClaimsSet().getJWTID();

        // VERIFY EXPIRATION TIME
        Date expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        // ASSIGN INFOR TOKEN
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jwtIdToken).expiryTime(expiryTime).build();

        // SAVE TO DATABASE
        invalidatedTokenRepository.save(invalidatedToken);

        // VERIFY USERNAME
        String username = signJWT.getJWTClaimsSet().getSubject();

        // VERIFY USERNAME EXIST IN DATABASE OR NOT
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // GENERAL NEW TOKEN
        String token = generalToken(user);

        // RESPONSE
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        // VERIFY TOKEN, USING SIGNER_KEY TO CHECK TOKEN CAN  REPLACE OR NOT
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        // DECRYPTION TOKEN
        SignedJWT signedJWT = SignedJWT.parse(token);

        // CHECK TOKEN EXPIRATION TIME
        Date expityTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        // VERIFY SIGNER_KEY NOT REPLACE
        boolean verified = signedJWT.verify(verifier);

        // CHECK TOKEN VALID OR NOT AND TOKEN EXPITY TIME OR NOT
        if (!(verified && expityTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        // CHECK TOKEN LOGOUT OR NOT
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    public String generalToken(User user) {
        //  CREATE HEADER FOR TOKEN WITH ALGORITM HS512 TO SIGN
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // CREATE CLAIMS FOR TOKEN
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("duymonkey.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        // CREATE PAYLOAD CONTAIN INFOR TOKEN AND AFTER PARSE CLAIMS TO JSON AND PUT INTO PAYLOAD
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // CREATE AND SIGN TOKEN
        // EXPLAIN : JWSOBJECT IS OBJECT REPRESENT FOR JWT, CREATED TO HEADER AND PAYLOAD
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            // SIGN TOKEN WITH PRIVATE KEY
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            // CONVERT TOKEN TO STRING
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        // USING StringJoiner TO CONNECT STRING TOGETHER
        // HERE ROLES AND PERMISSIONS OF USER WITH SPACE AS SEPARATORS
        StringJoiner stringJoiner = new StringJoiner(" ");

        // GET ROLES AND PERMISSIONS AND BUILD LIMIT CHAIN
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            // FOOP EACH ROLE
            user.getRoles().forEach(role -> {

                // IF USER HAVE ROLE ADD EACH ROLE TO StringJoiner WITH PREFIX ROLE_
                stringJoiner.add("ROLE_" + role.getName());

                // FOR EACH PERMISSION
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    // IF ROLE HAVE PERMISSIONS, ADD PERMISSION TO StringJoiner
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });
        }
        // RETURN ROLES AND PERMISSION CONNECT TOGETHER
        return stringJoiner.toString();
    }
}
