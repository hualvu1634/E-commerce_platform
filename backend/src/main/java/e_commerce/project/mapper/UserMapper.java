package e_commerce.project.mapper;

import e_commerce.project.dto.request.UserRequest;
import e_commerce.project.dto.response.UserResponse;
import e_commerce.project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Để Spring có thể @Autowired
public interface UserMapper {

    UserResponse toUserResponse(User user);
    @Mapping(target = "password", ignore = true) 
    @Mapping(target = "role", ignore = true)
   @Mapping(target = "id", ignore = true) 
    @Mapping(target = "isActive", ignore = true) 
    User toUser(UserRequest request);
}