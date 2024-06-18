package com.wooin.dailyone.custom.annotaion.withmockuserdto;

import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.model.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserDtoSecurityContextFactory implements WithSecurityContextFactory<WithMockUserDto> {
    @Override
    public SecurityContext createSecurityContext(WithMockUserDto annotation) {
        Long userId = annotation.userid();
        UserDto userDto = UserDto.builder().id(userId).role(UserRole.USER).build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDto, null, userDto.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        return context;
    }
}
