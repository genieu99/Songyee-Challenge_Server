package com.likelion.songyeechallenge.service;

import com.likelion.songyeechallenge.config.JwtTokenProvider;
import com.likelion.songyeechallenge.domain.challenge.Challenge;
import com.likelion.songyeechallenge.domain.challenge.ChallengeRepository;
import com.likelion.songyeechallenge.domain.review.Review;
import com.likelion.songyeechallenge.domain.review.ReviewRepository;
import com.likelion.songyeechallenge.domain.user.User;
import com.likelion.songyeechallenge.domain.user.UserRepository;
import com.likelion.songyeechallenge.web.dto.ChallengeListResponseDto;
//import com.likelion.songyeechallenge.web.dto.MyMissionResponseDto;
import com.likelion.songyeechallenge.web.dto.MyReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private LocalDate today = LocalDate.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String formattedToday = today.format(formatter);

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PictureService pictureService;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<ChallengeListResponseDto> findMyRecruiting(String jwtToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(jwtToken);
        User user = userRepository.findByUser_id(userId);
        Set<Challenge> participatedChallenges = challengeRepository.findByParticipants(user.getUser_id());

        return challengeRepository.findBeforeStartDesc(formattedToday).stream()
                .filter(participatedChallenges::contains)
                .map(challenge -> new ChallengeListResponseDto(challenge, pictureService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChallengeListResponseDto> findMyInProcess(String jwtToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(jwtToken);
        User user = userRepository.findByUser_id(userId);
        Set<Challenge> participatedChallenges = challengeRepository.findByParticipants(user.getUser_id());

        return challengeRepository.findInProcessDesc(formattedToday).stream()
                .filter(participatedChallenges::contains)
                .map(challenge -> new ChallengeListResponseDto(challenge, pictureService))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ChallengeListResponseDto> findMyFinished(String jwtToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(jwtToken);
        User user = userRepository.findByUser_id(userId);
        Set<Challenge> participatedChallenges = challengeRepository.findByParticipants(user.getUser_id());

        return challengeRepository.findFinishedDesc(formattedToday).stream()
                .filter(participatedChallenges::contains)
                .map(challenge -> new ChallengeListResponseDto(challenge, pictureService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MyReviewResponseDto> findMyReview(String jwtToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(jwtToken);
        User user = userRepository.findByUser_id(userId);
        List<Review> reviews = reviewRepository.findByUser(user.getUser_id());

        return reviews.stream()
                .map(MyReviewResponseDto::new)
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<MyMissionResponseDto> getUserMissions(String jwtToken) {
//        String userEmail = jwtTokenProvider.getUserEmailFromToken(jwtToken);
//
//        List<Mission> userMissions = missionRepository.findByChallengeParticipant(userEmail);
//
//        return userMissions.stream()
//                .map(mission -> new MyMissionResponseDto(mission))
//                .collect(Collectors.toList());
//    }
}