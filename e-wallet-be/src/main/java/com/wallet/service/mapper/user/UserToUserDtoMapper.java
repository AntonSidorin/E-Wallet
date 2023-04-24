package com.wallet.service.mapper.user;

import com.wallet.dao.entity.User;
import com.wallet.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserToUserDtoMapper implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {
        return new UserDto(
                user.getFirstname(),
                user.getLastname()
        );
    }
}
