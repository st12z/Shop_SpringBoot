package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.ChangePasswordDTO;
import com.thucjava.shopapp.dto.request.ResetPasswordDTO;
import com.thucjava.shopapp.dto.request.UserRequestDTO;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.TokenResponse;
import com.thucjava.shopapp.dto.response.UserResponse;
import com.thucjava.shopapp.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User saveUser(UserRequestDTO user);
    User getUserByEmail(String email);
    TokenResponse verify(UserRequestDTO user);
    UserResponse getUserDetails(String email);

    User updateUser( UserRequestDTO userRequestDTO, MultipartFile imageFile);

    void updatePassword(ChangePasswordDTO changePasswordDTO);

    void resetPassword(ResetPasswordDTO resetPasswordDTO,String email);
    Long countUsers();


    UserResponse updateStatus(String email, Boolean status);

    PageResponse<?> getAllAccounts(int pageNo);

    void updateRoles(String email, List<Long> roleIds);
}
