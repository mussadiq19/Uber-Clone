package com.example.uberreviewservice.controllers;

import com.example.uberreviewservice.adapters.CreateReviewDtoToReviewAdapter;
import com.example.uberreviewservice.dtos.CreateReviewDto;
import com.example.uberreviewservice.models.Review;
import com.example.uberreviewservice.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final CreateReviewDtoToReviewAdapter createReviewDtoToReviewAdapter;
    public ReviewController(ReviewService reviewService, CreateReviewDtoToReviewAdapter createReviewDtoToReviewAdapter){
        this.reviewService = reviewService;
        this.createReviewDtoToReviewAdapter = createReviewDtoToReviewAdapter;
    }

    @PostMapping
    public ResponseEntity<?> publishReview(@RequestBody CreateReviewDto request) {
        System.out.println(request);
        Review incommingReview=this.createReviewDtoToReviewAdapter.convertDto(request);
        System.out.println(incommingReview);
        if(incommingReview==null){
            return new ResponseEntity<>("Invalid arguments", HttpStatus.BAD_REQUEST);
        }
        Review review = this.reviewService.publishReview(incommingReview);
        System.out.println(review);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(){
        List<Review> reviews = this.reviewService.findAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findReviewById(@PathVariable Long reviewId) {
        try {
            Optional<Review> review = this.reviewService.findReviewById(reviewId);
            if(review.isPresent()) {
                return new ResponseEntity<>(review, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Review not found",HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReviewById(@PathVariable Long reviewId) {
        try {
            boolean isDeleted = this.reviewService.deleteReviewById(reviewId);
            if(!isDeleted) return new ResponseEntity<>("Unable to delete Review", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody Review request){
        try {
            Review review = this.reviewService.updateReview(reviewId, request);
            return new ResponseEntity<>(review, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}