package com.wooin.dailyone.service;

import com.wooin.dailyone.controller.request.PromiseGoalCreateRequest;
import com.wooin.dailyone.controller.response.promisegoal.MyPromiseGoalListResponse;
import com.wooin.dailyone.controller.response.promisegoal.MyPromiseGoalResponse;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.dto.PromiseGoalDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PromiseGoalService {

    private final DoneService doneService;
    private final SuperDoneService superDoneService;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final PromiseGoalRepository promiseGoalRepository;
    private final DoneRepository doneRepository;
    private final SuperDoneRepository superDoneRepository;


    @Transactional
    public void createPromiseGoal(PromiseGoalCreateRequest request, String email) {
        User user = findUserByEmail(email);
        Goal goal = findGoalById(request.getGoalId());
        PromiseGoal createPromiseGoal = PromiseGoal.builder()
                .user(user)
                .goal(goal)
                .promiseDoneCount(request.getPromiseDoneCount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        promiseGoalRepository.save(createPromiseGoal);
    }

    @Transactional(readOnly = true)
    public MyPromiseGoalListResponse selectMyPromiseGoal(String email) {
        User user = findUserByEmail(email);
        List<PromiseGoal> myPromiseGoalList = promiseGoalRepository.findByUserOrderByCreatedAtDesc(user);
//        List<PromiseGoal> promiseGoal = promiseGoalRepository.findAllByUserOrderByCreatedAtDesc(user).orElseThrow(()->
//                new DailyoneException(ErrorCode.PROMISE_GOAL_NOT_FOUND));
        var myPromiseGoalResponseList = myPromiseGoalList.stream().map(promiseGoal -> {
            int doneCount = doneRepository.countByPromiseGoal(promiseGoal);
            boolean isDoneToday = doneService.findTodayDoneByPromiseGoal(promiseGoal).isPresent();
            int superDoneCount = superDoneRepository.countByPromiseGoal(promiseGoal);
            boolean isSuperDoneToday = superDoneService.findTodaySuperDoneByPromiseGoal(promiseGoal).isPresent();

            return new MyPromiseGoalResponse(
                    GoalDto.fromEntity(promiseGoal.getGoal()),
                    PromiseGoalDto.fromEntity(promiseGoal),
                    doneCount,
                    isDoneToday,
                    superDoneCount,
                    isSuperDoneToday);
        }).toList();
        //TODO : 여러데이터를 한번에 담는 현재의 방식을 여러 요청으로 쪼갤지 고민. (ex. DoneStatus라는 응답을 따로 반환?)
        return new MyPromiseGoalListResponse(myPromiseGoalResponseList);
    }

    @Transactional
    public void deleteMyPromiseGoal(String email) {
        PromiseGoal myPromiseGoal = findPromiseGoalByEmail(email); //TODO: 현재는 단건조회(PromiseGoal을 하나만 갖는다는 전제). 추후 다건 조회가능할때 그에맞게 변경 필요
        //연관된 DONE & SuperDONE 삭제
        doneRepository.deleteByPromiseGoal(myPromiseGoal);
        superDoneRepository.deleteByPromiseGoal(myPromiseGoal);
        //PromiseGoal 삭제
        promiseGoalRepository.delete(myPromiseGoal);
        //TODO: Goal또한 같이 삭제할지 여부
    }

    @Transactional
    public void deletePromiseGoal(Long promiseGoalId) {
        PromiseGoal myPromiseGoal = findPromiseGoalById(promiseGoalId);
        //연관된 DONE & SuperDONE 삭제
        doneRepository.deleteByPromiseGoal(myPromiseGoal);
        superDoneRepository.deleteByPromiseGoal(myPromiseGoal);
        //PromiseGoal 삭제
        promiseGoalRepository.deleteById(promiseGoalId);
    }

    @Transactional
    public void finishPromiseGoal(String email, Long promiseGoalId) {
        PromiseGoal promiseGoal = findPromiseGoalById(promiseGoalId);
        int doneCount = doneService.countDoneByPromiseGoalId(promiseGoal.getGoal().getId());
        //카운트 수 검증
        if (doneCount < promiseGoal.getPromiseDoneCount()) throw new DailyoneException(ErrorCode.PROMISE_GOAL_NOT_FINISHED);
        //finishedAt 정보 입력
        promiseGoal.setFinished();
    }

    private Goal findGoalById(Long goalId) {
        return goalRepository.findById(goalId).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("Goal of %s is not found", goalId)));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s not found", email)));
    }
    private PromiseGoal findPromiseGoalById(Long promiseGoalId) {
        return promiseGoalRepository.findById(promiseGoalId).orElseThrow(() ->
                new DailyoneException(ErrorCode.PROMISE_GOAL_NOT_FOUND, String.format("PromiseGoal of %s(id) is not found", promiseGoalId)));

    }

    private PromiseGoal findPromiseGoalByEmail(String email) {
        User user = findUserByEmail(email);
        return promiseGoalRepository.findByUser(user).orElseThrow(() ->
                new DailyoneException(ErrorCode.PROMISE_GOAL_NOT_FOUND, String.format("PromiseGoal of %s is not found", email)));

    }
}
