package account.mapper;

import account.domain.user.UserDto;
import account.model.user.Group;
import account.model.user.Role;
import account.model.user.User;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        List<Role> roles = user.getGroups()
                .stream()
                .map(Group::getRole)
                .sorted(Comparator.comparing(Enum::name))
                .collect(Collectors.toList());

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    public List<UserDto> toList(List<User> users) {
        return users.isEmpty() ? List.of() : users.stream().map(this::toDto).collect(Collectors.toList());
    }
}
