package com.example.uberreviewservice;

import com.example.uberreviewservice.adapters.CreateReviewDtoToReviewAdapter;
import com.example.uberreviewservice.controllers.ReviewController;
import com.example.uberreviewservice.dtos.CreateReviewDto;
import com.example.uberreviewservice.models.Booking;
import com.example.uberreviewservice.models.Review;
import com.example.uberreviewservice.services.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {
    /**
     * REVISION CHEATSHEET:
     * <p>
     //@Mock
     * -> Creates a fake/dummy instance of a dependency.
     * -> Controls what external methods return (stubbing).
     * <p>
     //@InjectMocks
     * -> Creates the REAL instance of the class you are testing.
     * -> Automatically plugs the @Mock fields into this real object.
     * -> Eliminates the need to manually call 'new ClassUnderTest(mock1, mock2)'.
     * <p>
     * Framework Note: Requires @ExtendWith(MockitoExtension.class) on the class
     * to active these annotations, otherwise fields will remain null.
     */

    @InjectMocks
    private ReviewController  reviewController;
    @Mock
    private ReviewService reviewService;
    @Mock
    private CreateReviewDtoToReviewAdapter createReviewDtoToReviewAdapter;
//    @BeforeEach
//    public void setup(){
//
//    }
    @Test
    public void testFindReviewById_Success(){
        long reviewId=1L;
        Review mockreview=Review.builder().build();
        mockreview.setId(reviewId);
        //moking
        when(reviewService.findReviewById(reviewId)).thenReturn(Optional.of(mockreview));

        //perform the test
        ResponseEntity<?> response=reviewController.findReviewById(reviewId);

        //assertions

        assertEquals(HttpStatus.OK,response.getStatusCode());
        Optional<Review>returnedReview =(Optional<Review>) response.getBody();
        assertEquals(reviewId,returnedReview.get().getId());

    }
    @Test
    public void testPublishReview_Success(){
        CreateReviewDto requestDto =new CreateReviewDto();
        Booking booking = new Booking();
        booking.setId(1L);
        requestDto.setBookingId(booking.getId());

        Review incomingReview = Review.builder()
                                 .content("Test review content")
                                .rating(4.5)
                                .booking(booking).build();
        when(createReviewDtoToReviewAdapter.convertDto(requestDto)).thenReturn(incomingReview);
        Review savedReview = Review.builder()
                        .content(incomingReview.getContent())
                        .rating(incomingReview.getRating())
                        .booking(incomingReview.getBooking()).build();
        when(reviewService.publishReview(incomingReview)).thenReturn(savedReview);

        ResponseEntity<?>response=reviewController.publishReview(requestDto);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }
    @Test
    public void testFindReviewById_NOTFOUND(){
        long reviewId=1L;
        Review mockreview=Review.builder().build();
        mockreview.setId(reviewId);
        //moking
        when(reviewService.findReviewById(reviewId)).thenReturn(Optional.empty());

        //perform the test
        ResponseEntity<?> response=reviewController.findReviewById(reviewId);

        //assertions

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
//        Optional<Review>returnedReview =(Optional<Review>) response.getBody();
//        assertEquals(reviewId,returnedReview.get().getId());

    }
}
