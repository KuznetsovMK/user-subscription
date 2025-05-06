package web_rize.user_subscription.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import web_rize.user_subscription.entity.User;
import web_rize.user_subscription.model.UserRequestDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User updateEntity(@MappingTarget User user, UserRequestDto request);
}
