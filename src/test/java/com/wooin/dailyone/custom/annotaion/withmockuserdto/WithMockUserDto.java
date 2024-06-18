package com.wooin.dailyone.custom.annotaion.withmockuserdto;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserDtoSecurityContextFactory.class)
public @interface WithMockUserDto {
    long userid() default 1L;
}
