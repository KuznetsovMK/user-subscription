package web_rize.user_subscription.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import web_rize.user_subscription.entity.User;
import web_rize.user_subscription.model.UserDto;
import web_rize.user_subscription.model.UserRequestDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    List<UserDto> toDto(List<User> users);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDto request);
}
