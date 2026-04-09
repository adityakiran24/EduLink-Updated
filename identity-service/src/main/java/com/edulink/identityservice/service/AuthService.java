package com.edulink.identityservice.service;

<<<<<<< HEAD
=======
import com.edulink.identityservice.dto.*;
import com.edulink.identityservice.entity.User;
import com.edulink.identityservice.exception.EduLinkException;
import com.edulink.identityservice.repository.UserRepository;
import com.edulink.identityservice.util.JwtUtil;
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import com.edulink.identityservice.dto.ApiResponse;
import com.edulink.identityservice.dto.ChangePasswordRequest;
import com.edulink.identityservice.dto.LoginRequest;
import com.edulink.identityservice.dto.LoginResponse;
import com.edulink.identityservice.dto.UserResponse;
import com.edulink.identityservice.entity.User;
import com.edulink.identityservice.exception.BadRequestException;
import com.edulink.identityservice.exception.EduLinkException;
import com.edulink.identityservice.exception.InvalidCredentialsException;
import com.edulink.identityservice.exception.UserNotFoundException;
import com.edulink.identityservice.repository.UserRepository;
import com.edulink.identityservice.util.JwtUtil;

=======
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
<<<<<<< HEAD
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
=======
                .orElseThrow(() -> new EduLinkException("Invalid email or password", HttpStatus.UNAUTHORIZED));
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06

        if (!user.isActive()) {
            throw new EduLinkException("Account is deactivated", HttpStatus.FORBIDDEN);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
<<<<<<< HEAD
            throw new InvalidCredentialsException("Invalid email or password");
=======
            throw new EduLinkException("Invalid email or password", HttpStatus.UNAUTHORIZED);
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        log.info("User logged in: {} with role: {}", user.getEmail(), user.getRole());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .role(user.getRole().name())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .userId(user.getId())
                .mustChangePassword(user.isMustChangePassword())
                .build();
    }

    public ApiResponse<String> changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
<<<<<<< HEAD
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
=======
                .orElseThrow(() -> new EduLinkException("User not found", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new EduLinkException("Current password is incorrect", HttpStatus.BAD_REQUEST);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new EduLinkException("New password and confirm password do not match", HttpStatus.BAD_REQUEST);
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(false);
        user.setTemporaryPassword(null);
        userRepository.save(user);

        return ApiResponse.success("Password changed successfully", null);
    }

    public LoginResponse refreshToken(String refreshToken) {
        try {
            String email = jwtUtil.extractEmail(refreshToken);
            if (jwtUtil.isTokenExpired(refreshToken)) {
<<<<<<< HEAD
                throw new InvalidCredentialsException("Refresh token expired");
            }
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
=======
                throw new EduLinkException("Refresh token expired", HttpStatus.UNAUTHORIZED);
            }
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EduLinkException("User not found", HttpStatus.NOT_FOUND));
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06

            String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
            String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            return LoginResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .role(user.getRole().name())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .userId(user.getId())
                    .mustChangePassword(user.isMustChangePassword())
                    .build();
        } catch (EduLinkException e) {
            throw e;
        } catch (Exception e) {
<<<<<<< HEAD
            throw new InvalidCredentialsException("Invalid refresh token");
=======
            throw new EduLinkException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        }
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
<<<<<<< HEAD
                .orElseThrow(() -> new UserNotFoundException("User not found"));
=======
                .orElseThrow(() -> new EduLinkException("User not found", HttpStatus.NOT_FOUND));
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .active(user.isActive())
                .mustChangePassword(user.isMustChangePassword())
                .temporaryPassword(user.isMustChangePassword() ? user.getTemporaryPassword() : null)
                .schoolId(user.getSchoolId())
                .classId(user.getClassId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
