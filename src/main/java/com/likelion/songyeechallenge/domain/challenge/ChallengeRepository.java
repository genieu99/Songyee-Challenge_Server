package com.likelion.songyeechallenge.domain.challenge;

import com.likelion.songyeechallenge.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    @Query("SELECT c FROM Challenge c WHERE c.startDate > :today ORDER BY c.challenge_id DESC")
    List<Challenge> findBeforeStartDateDesc(String today);

    @Query("SELECT c FROM Challenge c WHERE c.startDate <= :today AND c.endDate >= :today ORDER BY c.challenge_id DESC")
    List<Challenge> findBetweenStartDateAndEndDateDesc(String today);

    @Query("SELECT c FROM Challenge c WHERE c.endDate < :today ORDER BY c.challenge_id DESC")
    List<Challenge> findAfterEndDateDesc(String today);

    @Query("SELECT c FROM Challenge c WHERE c.startDate <= :today AND c.endDate >= :today ORDER BY c.participants.size DESC")
    List<Challenge> findBetweenStartDateAndEndDateHot(String today);

    List<Challenge> findByCategory(String category);

    @Query("SELECT c FROM Challenge c WHERE c.title LIKE %:searchWord% OR c.category LIKE %:searchWord% ORDER BY c.challenge_id DESC")
    List<Challenge> findByTitleOrCategory(String searchWord);

    @Query("SELECT c FROM Challenge c JOIN c.participants p WHERE p.user_id = :userId")
    Set<Challenge> findByParticipants(@Param("userId") Long userId);

    @Query("SELECT c FROM Challenge c WHERE c.challenge_id = :challengeId")
    Challenge findByChallenge_id(Long challengeId);
}
