package e_commerce.project.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import e_commerce.project.dto.request.UserRequest;
import e_commerce.project.dto.response.ApiResponse;
import e_commerce.project.dto.response.PageResponse;
import e_commerce.project.dto.response.UserResponse;
import e_commerce.project.entity.Cart;
import e_commerce.project.entity.User;
import e_commerce.project.enumerate.ErrorCode;
import e_commerce.project.enumerate.Role;
import e_commerce.project.exception.AppException;
import e_commerce.project.mapper.UserMapper;
import e_commerce.project.repository.CartRepository;
import e_commerce.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class UserService {

private final UserRepository userRepository;
private final CartRepository cartRepository;
private  final PasswordEncoder passwordEncoder;
private final UserMapper userMapper;


@SuppressWarnings("unused")
public UserResponse addAccount(UserRequest accountRequest){
    if(userRepository.existsByUsername(accountRequest.getUsername()))
        throw new AppException(ErrorCode.USER_EXISTED);
    if(userRepository.existsByPhoneNumber(accountRequest.getPhoneNumber()))
        throw new AppException(ErrorCode.USER_EXISTED);
    User accounts = userMapper.toUser(accountRequest);
    accounts.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
    accounts.setRole(Role.USER);
    User saveAccounts = userRepository.save(accounts);
    Cart cart = Cart.builder().user(saveAccounts).build();
    Cart savedCart = cartRepository.save(cart);
    UserResponse accountResponse = userMapper.toUserResponse(saveAccounts);
    return accountResponse;                                    
                                      
}

public PageResponse<UserResponse> getAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by( "id").ascending());
        Page<User> pageData = userRepository.findAll(pageable);

        List<UserResponse> responseList = pageData.getContent().stream()
                .map(userMapper::toUserResponse)
                .toList();
        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responseList)
                .build();
    }

     public UserResponse getAccount() {
       String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }



    public ApiResponse deleteAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        user.setIsActive(false); 
        userRepository.save(user); 
        return ApiResponse.builder()
        .timestamp(LocalDateTime.now())
        .code(200)
        .message("Xóa người dùng thành công")
        .build();
    
    }




}

