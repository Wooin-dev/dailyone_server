package com.wooin.dailyone.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;

@Slf4j
@NoArgsConstructor
public class CustomSpringELParser {
    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {

        log.debug("parameterNames = " + Arrays.toString(parameterNames));
        log.debug("args = " + Arrays.toString(args));
        log.debug("key = " + key);

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, Object.class);
    }

}
