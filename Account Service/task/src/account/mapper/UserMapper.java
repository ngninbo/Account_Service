package account.mapper;

import account.domain.UserDto;
import account.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getLastname(), user.getEmail());
    }
}
