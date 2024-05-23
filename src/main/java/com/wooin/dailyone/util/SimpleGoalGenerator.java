package com.wooin.dailyone.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SimpleGoalGenerator {

    public List<String> generateSimpleGoal(String originalGoal) {
        List<String> simpleGoalList = new ArrayList<>();
        //01. 숫자가 있는지 확인 -> 낮은 숫자로 변환
        if (strHasNumber(originalGoal)) simpleGoalList.add(generateNumberSimpleGoal(originalGoal));
        //키워드필터를 통한 변환
        simpleGoalList.addAll(generateSimpleGoalByKeyword(originalGoal));
        log.debug("simpleGoalList = " + simpleGoalList);
        return simpleGoalList;
    }
    private boolean strHasNumber(String originalGoal) {
        return strHasNumber(originalGoal, 0);
    }
    private boolean strHasNumber(String originalGoal, int startIdx) {
        char[] charArr = originalGoal.toCharArray();
        for( int i =startIdx; i<originalGoal.length(); i++) {
            if (Character.isDigit(charArr[i])) return true;
        }
        return false;
    }

    private String generateNumberSimpleGoal(String originalGoal) {
        return generateNumberSimpleGoal(originalGoal, 0);
    }
    private String generateNumberSimpleGoal(String originalGoal, int startIdx) {
        System.out.println("originalGoal = " + originalGoal);
        //재귀 종료 조건
        if (!strHasNumber(originalGoal, startIdx)) return originalGoal;
        //숫자 위치 인덱스 찾기
        int firstNumIdx = -1;
        char[] charArr = originalGoal.toCharArray();
        for(int i=startIdx; i< charArr.length; i++) {
            if ( Character.isDigit(charArr[i]) ) {
                firstNumIdx = i;
                break;
            }
        }
        int lastNumIdx = firstNumIdx;
        for(int i=firstNumIdx+1; i< charArr.length; i++) {
            if ( Character.isDigit(charArr[i]) ) {
                lastNumIdx = i;
            } else {
                break;
            }
        }
        log.debug("firstNumIdx = " + firstNumIdx);
        log.debug("lastNumIdx = " + lastNumIdx);
        //숫자위치 기준으로 String 나누기
        String forthStr = originalGoal.substring(0, firstNumIdx);
        String numStr = originalGoal.substring(firstNumIdx, lastNumIdx+1);
        String backStr = originalGoal.substring(lastNumIdx+1);
        //숫자 낮추기
        String simplifiedNum = simplifyNum(numStr);
        //이후 존재하는 숫자도 낮추기. 재귀함수.
        return generateNumberSimpleGoal(forthStr+simplifiedNum+backStr, lastNumIdx+simplifiedNum.length());
    }

    /**
     * 목표 내의 숫자를 축소시키는 메소드
     * @param number
     * @return
     * 자릿수는 동일하지만 가장 작은 수로 리턴
     * ex) 123 ->100, 5678 -> 1000, 1->1
     */
    private String simplifyNum(String number) {
        // 1은 그대로 반환
        if (number.equals("1")) return number;
        // 가장 높은 자릿수의 숫자가 1인 경우 한자리수 낮게 반환
        if (number.charAt(0) == '1') number = number.substring(0,number.length()-1);
        // 첫 번째 숫자를 '1'로 변경하고 나머지 자릿수를 '0'으로 변경
        char[] chars = new char[number.length()];
        chars[0] = '1';
        for (int i = 1; i < chars.length; i++) {
            chars[i] = '0';
        }
        // 수정된 문자열을 다시 정수로 변환
        return new String(chars);
    }

    private List<String> generateSimpleGoalByKeyword(String originalGoal) {
        List<String> simpleGoalList = new ArrayList<>();
        //TODO : DB에서 조회해서 해당 기준에 맞으면 변경 구현
        return simpleGoalList;
    }
}
