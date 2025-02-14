package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.ChangePasswordDTO;
import com.thucjava.shopapp.dto.request.ResetPasswordDTO;
import com.thucjava.shopapp.dto.request.UserRequestDTO;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.TokenResponse;
import com.thucjava.shopapp.dto.response.UserResponse;
import com.thucjava.shopapp.exception.ResourceNotFoundException;
import com.thucjava.shopapp.exception.ResoureceExistException;
import com.thucjava.shopapp.model.Role;
import com.thucjava.shopapp.model.RoomChat;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.repository.RoleRepo;
import com.thucjava.shopapp.repository.RoomRepository;
import com.thucjava.shopapp.repository.UserRepo;
import com.thucjava.shopapp.service.JwtService;
import com.thucjava.shopapp.service.UserService;
import com.thucjava.shopapp.utils.RoleType;
import com.thucjava.shopapp.utils.UploadImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder ;
    private final RoomRepository roomRepo;
    @Override
    public User saveUser(UserRequestDTO user) {
        Role role = roleRepo.findById(1L).orElseThrow(()->new ResourceNotFoundException("Khong tim thay role"));
        User existingUser = userRepo.findByEmail(user.getEmail());
        if(existingUser != null) {
            throw new ResoureceExistException("Email đã tồn tại vui lòng thay đổi email!");
        }
        RoomChat roomChat = new RoomChat();
        List<RoomChat> roomChats = new ArrayList<>();
        RoomChat savedRoomChat=roomRepo.save(roomChat);
        List<User> userAdmins = userRepo.findByRoleName(RoleType.ADMIN.getValue());
        for(User u :userAdmins){
            u.getRoomChats().add(savedRoomChat);
        }
        userRepo.saveAll(userAdmins);
        roomChats.add(roomRepo.save(roomChat));
        User userEntity = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .city(user.getCity())
                .district(user.getDistrict())
                .email(user.getEmail())
                .roles(List.of(role))
                .status(true)
                .password(passwordEncoder.encode(user.getPassword()))
                .roomChats(roomChats)
                .build();
        return userRepo.save(userEntity);
    }

    @Override
    public User getUserByEmail(String email) {
        User user =userRepo.findByEmail(email);
        if(user==null) {
            throw new ResourceNotFoundException("Khong tim thay user");
        }
        return user;
    }

    @Override
    public TokenResponse verify(UserRequestDTO user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if(authentication.isAuthenticated()) {
            String email = user.getEmail();
            return TokenResponse.builder()
                    .access_token(jwtService.generateAccessToken(email))
                    .refresh_token(jwtService.generateRefreshToken(email))
                    .build();
        }
        throw new IllegalArgumentException("Email hoặc mật khẩu không đúng!");
    }

    @Override
    public UserResponse getUserDetails(String email) {
        User user = getUserByEmail(email);
        return Converter.toUserResponse(user);
    }

    @Override
    public User updateUser(UserRequestDTO userRequestDTO, MultipartFile imageFile) {
        User user = getUserByEmail(userRequestDTO.getEmail());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setPhone(userRequestDTO.getPhone());
        user.setCity(userRequestDTO.getCity());
        user.setDistrict(userRequestDTO.getDistrict());
        String fileName = UploadImage.uploadImage(imageFile);
        if(fileName!=null && !fileName.isEmpty()) {
            user.setAvatar("http://localhost:8080/"+fileName);
        }
        return userRepo.save(user);
    }

    @Override
    public void updatePassword(ChangePasswordDTO changePasswordDTO) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = getUserByEmail(principal.getName());
        if(!passwordEncoder.matches(changePasswordDTO.getOld_password(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }
        String encodedPassword = passwordEncoder.encode(changePasswordDTO.getNew_password());
        user.setPassword(encodedPassword);
        userRepo.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO,String email) {
        User user=getUserByEmail( email);
        String encodedPassword = passwordEncoder.encode(resetPasswordDTO.getNew_password());
        user.setPassword(encodedPassword);
        userRepo.save(user);
    }

    @Override
    public Long countUsers() {
        return userRepo.count();
    }

    @Override
    public PageResponse<?> getAllAccounts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, Constant.pageSize, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<User> users = userRepo.findAll(pageable);
        List<UserResponse> userResponses = users.getContent().stream().map(Converter::toUserResponse).toList();
        return PageResponse.builder()
                .total((int)users.getTotalElements())
                .pageNo(users.getNumber()+1)
                .pageSize(users.getSize())
                .dataRes(userResponses)
                .build();

    }

    @Override
    public void updateRoles(String email, List<Long> roleIds) {
        User user = getUserByEmail(email);
        List<Role> roles = roleRepo.findByIdIn(roleIds);
        user.setRoles(roles);
        userRepo.save(user);
    }

    @Override
    public UserResponse updateStatus(String email, Boolean status) {
        User user = getUserByEmail(email);
        user.setStatus(status);
        User newUser =userRepo.save(user);
        return Converter.toUserResponse(newUser);
    }

}
