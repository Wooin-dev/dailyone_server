package com.wooin.dailyone.service;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.controller.request.GoalFollowRequest;
import com.wooin.dailyone.controller.response.goal.*;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.User;
import com.wooin.dailyone.repository.DoneRepository;
import com.wooin.dailyone.repository.GoalRepository;
import com.wooin.dailyone.repository.PromiseGoalRepository;
import com.wooin.dailyone.repository.UserRepository;
import com.wooin.dailyone.util.SimpleGoalGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
//@Transactional //TOSTUDY 클래스 레벨에 붙여주는 @Transactional의 동작 알아보기
@Service
public class GoalService { // cmd + shift + T : 테스트 생성 단축키

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final DoneRepository doneRepository;
    private final PromiseGoalRepository promiseGoalRepository;

    private final SimpleGoalGenerator simpleGoalGenerator = new SimpleGoalGenerator();

    @Transactional
    public void create(GoalCreateRequest request, Long userId) {
        //user find
        User user = userRepository.getReferenceById(userId);
        //goal save
        Goal goal = Goal.builderFromRequest(request).user(user).build();
        Goal savesGoal = goalRepository.save(goal);

        //생성이후 본인의 PromiseGoal로 등록
        PromiseGoal myPromiseGoal = PromiseGoal.builderFromRequest(request)
                .goal(savesGoal)
                .user(user)
                .build();
        promiseGoalRepository.save(myPromiseGoal);
    }

    @Transactional
    public void followGoal(GoalFollowRequest request, Long userId) {

        User user = userRepository.getReferenceById(userId);
        Goal goal = goalRepository.getReferenceById(request.getGoalId());

        //중복 체크
        promiseGoalRepository.findFirstByUserAndGoal(user, goal).ifPresent(e -> {
            throw new DailyoneException(ErrorCode.PROMISE_GOAL_ALREADY_EXIST, String.format("promise-goal with %s-goal of %s-user has already existed", request.getGoalId(), userId));
        });

        //Promise-Goal 저장
        PromiseGoal promiseGoal = PromiseGoal.builder()
                .user(user)
                .goal(goal)
                .startDate(request.getStartDate())
                .build();

        promiseGoalRepository.save(promiseGoal);
    }

    @Transactional
    public GoalDetailResponse selectGoal(Long goalId) {
        Goal goal = findGoalById(goalId);
        goal.viewCountUp(); // 조회수 상승
        int challengersCount = promiseGoalRepository.countByGoal_Id(goalId); //도전자 수 count
        int doneCount = doneRepository.countByPromiseGoal_Goal_Id(goalId); // 해당 목표의 모든 done count
        return GoalDetailResponse
                .builderFromDto(GoalDto.fromEntity(goal))
                .challengersCount(challengersCount)
                .doneCount(doneCount)
                .build();
//        return new GoalDetailResponse(GoalDto.fromEntity(goal));
    }

    @Transactional(readOnly = true)
    public GoalThumbListResponse selectGoalThumbPage(Pageable pageable) { //TODO : 정렬기준 추가. 현재는 최신순

        List<Goal> goals = goalRepository.findAll(pageable).getContent();
        List<GoalThumbResponse> goalThumbResponses =
                goals.stream().map(GoalDto::fromEntity)
                        .map(dto -> {
                            int challengersCount = promiseGoalRepository.countByGoal_Id(dto.id());
                            int doneCount = doneRepository.countByPromiseGoal_Goal_Id(dto.id());
                            return GoalThumbResponse.builderFromDto(dto)
                                    .challengersCount(challengersCount) //TODO : count쿼리 합치기
                                    .doneCount(doneCount)
                                    .build();
                        }).filter(goal -> goal.getChallengersCount()>0) //아무도 도전하지 않는 목표 제외. TODO : 가져온 다음 필터링하지말고 조건절 추가해서 해당하는 것만 가져오기
                        .toList();
        return new GoalThumbListResponse(goalThumbResponses);
    }

    @Transactional(readOnly = true)
    public MyGoalListResponse selectMyGoal(Long userId) {
        List<Goal> goals = goalRepository.findByUser_IdOrderByCreatedAtDesc(userId);
        List<MyGoalResponse> goalDtos = goals.stream().map(GoalDto::fromEntity).map(MyGoalResponse::fromDto).toList();
        return new MyGoalListResponse(goalDtos);
    }

    @Transactional
    public void deleteGoal(Long userId) {
        User user = userRepository.getReferenceById(userId);
        //Delete From DB
        goalRepository.deleteByUser(user);
    }

    public GeneratedSimpleGoalResponse generateSimpleGoal(String originalGoal) {
        List<String> simpleGoalList = simpleGoalGenerator.generateSimpleGoal(originalGoal);
        return new GeneratedSimpleGoalResponse(simpleGoalList);
    }


    private Optional<Done> findDoneOfTodayByUserAndGoal(PromiseGoal promiseGoal) {
        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();
        // 오늘의 시작 시간 ( 00:00:00 )
        Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());
        // 오늘의 종료 시간 ( 23:59:59 )
        Timestamp endOfDay = Timestamp.valueOf(today.atTime(LocalTime.MAX));

        return doneRepository.findByPromiseGoalAndCreatedAtBetween(promiseGoal, startOfDay, endOfDay);
    }

    private Goal findGoalById(Long goalId) {
        return goalRepository.findById(goalId).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("Goal of %s is not found", goalId)));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new DailyoneException(ErrorCode.EMAIL_NOT_FOUND, String.format("%s not found", email)));
    }

    private Goal findMyGoalByUser(User user) {
        return goalRepository.findFirstByUserOrderByCreatedAtDesc(user).orElseThrow(() ->
                new DailyoneException(ErrorCode.GOAL_NOT_FOUND, String.format("The goal of %s is not found", user)));
    }
}
