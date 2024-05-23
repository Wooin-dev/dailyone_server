package com.wooin.dailyone.util;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
//@SpringBootTest
class SimpleGoalGeneratorTest {
    SimpleGoalGenerator simpleGoalGenerator = new SimpleGoalGenerator();
    @Test
    void 목표_간단하기_수치감소_숫자1개인_경우_1로시작하는_10이상의_수() {
        String testStr = "푸시업 15개하기";
        String expectedResult = "푸시업 1개하기";

        List<String> result = simpleGoalGenerator.generateSimpleGoal(testStr);

        boolean chkExpect = false;
        for (String str : result) {
            log.debug("str = " + str);
            System.out.println("str = " + str);
            if (str.equals(expectedResult)) chkExpect = true;
        }
        Assertions.assertThat(chkExpect).isTrue();
    }

    @Test
    void 목표_간단하기_수치감소_숫자1개인_경우_1이_아닌_수로시작하는_10이상의_수() {
        String testStr = "푸시업 30개하기";
        String expectedResult = "푸시업 10개하기";

        List<String> result = simpleGoalGenerator.generateSimpleGoal(testStr);

        boolean chkExpect = false;
        for (String str : result) {
            log.debug("str = " + str);
            System.out.println("str = " + str);
            if (str.equals(expectedResult)) chkExpect = true;
        }
        Assertions.assertThat(chkExpect).isTrue();
    }

    @Test
    void 목표_간단하기_수치감소_숫자3개인_경우() {
        String testStr = "헬스장 5동작 12세트 250%";
        String expectedResult = "헬스장 1동작 1세트 100%";

        List<String> result = simpleGoalGenerator.generateSimpleGoal(testStr);

        boolean chkExpect = false;
        for (String str : result) {
            log.debug("str = " + str);
            System.out.println("str = " + str);
            if (str.equals(expectedResult)) chkExpect = true;
        }
        Assertions.assertThat(chkExpect).isTrue();
    }
}