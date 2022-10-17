package account.mapper;

import account.domain.UserDto;
import account.model.Group;
import account.model.Role;
import account.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        List<Role> roles = user.getGroups().stream().map(Group::getRole).sorted().collect(Collectors.toList());

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    public List<UserDto> toList(List<User> users) {
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }
}
