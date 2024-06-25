package com.wooin.dailyone.util;

import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;

import java.util.Optional;

public class ClassUtils {

    public static <T> T getSafeCastInstance(Object o, Class<T> clazz) {
        Optional<T> result = clazz != null && clazz.isInstance(o) ? Optional.of(clazz.cast(o)) : Optional.empty();
        return result.orElseThrow(() -> new DailyoneException(ErrorCode.INTERNAL_SERVER_ERROR, "incorrect Casting :"+clazz.getSimpleName()));
    }
}
