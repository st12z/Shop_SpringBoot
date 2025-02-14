package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.ChangePasswordDTO;
import com.thucjava.shopapp.dto.request.ResetPasswordDTO;
import com.thucjava.shopapp.dto.request.UserRequestDTO;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.dto.response.TokenResponse;
import com.thucjava.shopapp.dto.response.UserResponse;
import com.thucjava.shopapp.model.ResetToken;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.service.ResetTokenService;
import com.thucjava.shopapp.service.UserService;
import com.thucjava.shopapp.utils.SendMail;
import com.thucjava.shopapp.utils.TokenRandom;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final SendMail mail;
    private final ResetTokenService resetTokenService;
    @PostMapping("/register")
    public ResponseData<?> register(@RequestBody @Valid UserRequestDTO user) {
        try{
            User result =userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "create success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseData<?> login(@RequestBody @Valid UserRequestDTO user) {
        TokenResponse token = userService.verify(user);
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "login success",token);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "login failed");
        }

    }
    @GetMapping("/detail")
    public ResponseData<?> getUser() {
        try{
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            UserResponse user = userService.getUserDetails(principal.getName());
            return new ResponseData<>(HttpStatus.OK.value(), "get success",user);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @PatchMapping(value = "/change-user",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<?> changeUser(@RequestPart  UserRequestDTO userRequestDTO,@RequestPart(required = false) MultipartFile imageFile) {
        try{
            User user = userService.updateUser(userRequestDTO,imageFile);
            UserResponse userReponse = Converter.toUserResponse(user);
            return new ResponseData<>(HttpStatus.OK.value(), "change success",userReponse);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @PatchMapping(value = "/change-password")
    public ResponseData<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try{
            userService.updatePassword(changePasswordDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "update password success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }
    @GetMapping(value="/forgot-password")
    public ResponseData<?> getForgotPassword(@RequestParam String email) {
        try{
            String token= TokenRandom.randomToken(6);
            resetTokenService.saveToken(email,token);
            String content = "<p>Mã OTP của bạn là :<strong>"+token+"</strong></p>" +
                    "<p>Thời gian hết hạn :<strong>10 phút</strong></p>" +
                    "<p>Vui lòng nhấn vào link sau để reset password: <a href=http://localhost:3000/otp-password?email="+email+">Reset Password</a></p>";
            mail.sendMail(email,content,"Reset Password");
            return new ResponseData<>(HttpStatus.OK.value(), "Đã gửi mã OTP",email);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @GetMapping(value="/otp-password")
    public ResponseData<?> getOtpPassword(@RequestParam String email,@RequestParam String otp) {
        try{
            ResetToken resetToken = resetTokenService.getResetToken(email,otp);
            return new ResponseData<>(HttpStatus.OK.value(), "OTP valid");
        }catch (Exception e){
           return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @PostMapping(value="/reset-password")
    public ResponseData<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO,@RequestParam String email,@RequestParam String otp) {
        try{
            ResetToken resetToken = resetTokenService.getResetToken(email,otp);

            userService.resetPassword(resetPasswordDTO,email);
            return new ResponseData<>(HttpStatus.OK.value(), "reset password success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
