package kr.ac.cnu.swacademy.cagong.service;

import kr.ac.cnu.swacademy.cagong.dto.ReviewListResponseDto;
import kr.ac.cnu.swacademy.cagong.dto.ReviewResponseDto;
import kr.ac.cnu.swacademy.cagong.dto.ReviewSaveRequestDto;
import kr.ac.cnu.swacademy.cagong.dto.ReviewUpdateRequestDto;
import kr.ac.cnu.swacademy.cagong.entity.Cafe;
import kr.ac.cnu.swacademy.cagong.entity.Review;
import kr.ac.cnu.swacademy.cagong.entity.User;
import kr.ac.cnu.swacademy.cagong.repository.CafeRepository;
import kr.ac.cnu.swacademy.cagong.repository.ReviewRepository;
import kr.ac.cnu.swacademy.cagong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    @Transactional
    public List<ReviewListResponseDto> findAll()
    {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponseDto findById(Long id)
    {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + id));
        return new ReviewResponseDto(review);
    }

    @Transactional
    public Long save(ReviewSaveRequestDto requestDto) {
        User user = userRepository
                .findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id의 유저가 없습니다"));
        Cafe cafe = cafeRepository
                .findById(requestDto.getCafeId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id의 카페가 없습니다"));
        return reviewRepository.save(requestDto.toEntity(user, cafe)).getId();
    }

    @Transactional
    public Long update(Long reviewId, ReviewUpdateRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("해당하는 id의 리뷰가 없습니다"));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당하는 id의 유저가 없습니다"));
        Cafe cafe = cafeRepository.findById(requestDto.getCafeId()).orElseThrow(() -> new IllegalArgumentException("해당하는 id의 카페가 없습니다"));

        review.update(requestDto, user, cafe);
        return review.getId();
    }

    @Transactional
    public void delete(Long reviewId) {
        reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("해당 id의 리뷰가 존재하지 않습니다."));
        reviewRepository.deleteById(reviewId);
    }

}
