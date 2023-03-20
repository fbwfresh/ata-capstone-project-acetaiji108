package com.kenzie.capstone.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.common.base.Verify;
import com.google.gson.Gson;
import com.kenzie.capstone.service.converter.VideoGameConverter;
import com.kenzie.capstone.service.dao.NonCachingVideoGameDao;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.dependency.DaoModule;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.lambda.GetVideoGame;
import com.kenzie.capstone.service.model.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VideoGameLambdaServiceTest {
    //class ReferralServiceTest {
    /**
     * ------------------------------------------------------------------------
     * expenseService.getExpenseById
     * ------------------------------------------------------------------------
     **/
    private VideoGameDao videoGameDao;
    private VideoGameService videoGameService;

    @BeforeAll
    void setup() {
        DaoModule module = new DaoModule();
        this.videoGameDao = new NonCachingVideoGameDao(module.provideDynamoDBMapper());
        this.videoGameService = new VideoGameService(videoGameDao);
    }

    @Test
    void testHandleVideoGame() throws InvalidGameException {
        // GIVEN
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setPathParameters(Map.of("videoGameId", "1"));

        VideoGameRecord record = new VideoGameRecord();
        record.getName().equals("Test Game");

        // WHEN
        APIGatewayProxyResponseEvent result = new GetVideoGame().handleRequest(input, null);

        // THEN
        assertEquals(200, result.getStatusCode());
        assertNotNull(result.getBody());

        VideoGameResponse response = new Gson().fromJson(result.getBody(), VideoGameResponse.class);
        assertNotNull(response);
        assertEquals("Test Game", response.getName());
    }

}
//        @Test
//        public void getCustomerReferralSummary_noReferralsTest() {
//            String customerId = "CUST-001";
//            CustomerReferrals result = videoGameService.getCustomerReferralSummary(customerId);
//            assertEquals(0, result.getNumFirstLevelReferrals());
//            assertEquals(0, result.getNumSecondLevelReferrals());
//            assertEquals(0, result.getNumThirdLevelReferrals());
//        }
//        @Test
//        void deleteReferralsTest() {
//            // GIVEN
//            ReferralService referralService = new ReferralService(referralDao);
//            List<String> customerIds = Arrays.asList("customerId1", "customerId2");
//            ReferralRecord record1 = new ReferralRecord();
//            record1.setCustomerId("customerId1");
//            ReferralRecord record2 = new ReferralRecord();
//            record2.setCustomerId("customerId2");
//            ArgumentCaptor<ReferralRecord> argumentCaptor = ArgumentCaptor.forClass(ReferralRecord.class);
//            // WHEN
//            when(referralDao.deleteReferral(argumentCaptor.capture())).thenReturn(true);
//            boolean result = referralService.deleteReferrals(customerIds);
//            // THEN
//            assertTrue(result);
//            List<ReferralRecord> capturedRecords = argumentCaptor.getAllValues();
//            assertEquals(2, capturedRecords.size());
//            assertEquals("customerId1", capturedRecords.get(0).getCustomerId());
//            assertEquals("customerId2", capturedRecords.get(1).getCustomerId());
//            verify(referralDao, times(2)).deleteReferral(any());
//            // GIVEN
//            when(referralDao.deleteReferral(any())).thenReturn(false);
//            // WHEN
//            result = referralService.deleteReferrals(customerIds);
//            // THEN
//            assertFalse(result);
//            verify(referralDao, times(4)).deleteReferral(any());
//        }
//        @Test
//        void condenseListsTest() throws ExecutionException, InterruptedException {
//            // GIVEN
//            List<Future<LeaderboardEntry>> futures = new ArrayList<>();
//            futures.add(mock(Future.class));
//            futures.add(mock(Future.class));
//            LeaderboardEntry entry1 = new LeaderboardEntry(5, "user1");
//            LeaderboardEntry entry2 = new LeaderboardEntry(3, "user2");
//            // Set up mock future behavior to return leaderboard entries
//            when(futures.get(0).get()).thenReturn(entry1);
//            when(futures.get(1).get()).thenReturn(entry2);
//            // Call the method being tested
//            List<LeaderboardEntry> result = videoGameService.condenseLists(futures);
//            // Verify that the expected entries are in the result
//            assertTrue(result.contains(entry1));
//            assertTrue(result.contains(entry2));
//            // Verify that the result is sorted by number of referrals
//            assertEquals(result.get(0), entry1);
//            assertEquals(result.get(1), entry2);
//        }
//        @Test
//        void addVideoGameTest() {
//         //   ArgumentCaptor<VideoGameRecord> referralCaptor = ArgumentCaptor.forClass(VideoGameRecord.class);
//            // GIVEN
//            String gameName = "Contra";
//            String description = "Contra is a run-and-gun action platformer, notorious for its high difficulty. " +
//                    "The player character comes armed with a gun that can shoot infinitely. " +
//                    "Different weapons with new abilities and that shoot different types of projectiles can be acquired" +
//                    " as the player progresses through the levels.";
//            VideoGame game = new VideoGame(gameName,description, Consoles.DS,Consoles.GC,Consoles.GBA,Consoles.NS,Consoles.WII,Consoles.WIIU);
//            VideoGameRequest request = new VideoGameRequest();
//            request.setName(game.getName());
//            request.setConsoles(game.getConsoles());
//            request.setDescription(game.getDescription());
//            request.setUpwardVote(request.getUpwardVote());
//            request.setDownwardVote(request.getDownwardVote());
//            request.setVotingPercentage(request.getVotingPercentage());
//            // WHEN
//            VideoGameResponse response = this.videoGameService.addVideoGame(request);
//            // THEN
//
//          //  verify(this.videoGameDao, times(1)).addVideoGame(referralCaptor.capture());
//            VideoGameRecord record = new VideoGameRecord();
//            record.setName(response.getName());
//            record.setConsoles(response.getConsoles());
//            record.setDescription(response.getDescription());
//            record.setDownwardVote(response.getDownwardVote());
//            record.setVotingPercentage(response.getTotalVote());
//            record.setUpwardVote(response.getUpwardVote());
//            assertNotNull(record, "The record is valid");
//          //  assertEquals(gameName, record.getName(), "The record name should match");
//            assertEquals(description, record.getDescription(), "The record description should match");
//            assertNotNull(record.getConsoles(), "The record consoles exist");
//            assertNotNull(response, "A response is returned");
//            assertEquals(customerId, response.getCustomerId(), "The response customerId should match");
//            assertEquals(referrerId, response.getReferrerId(), "The response referrerId should match");
//            assertNotNull(response.getReferralDate(), "The response referral date exists");

//        @Test
//        void addReferralTest_no_customer_id() {
//            // GIVEN
//            String customerId = "";
//            String referrerId = "";
//            ReferralRequest request = new ReferralRequest();
//            request.setCustomerId(customerId);
//            request.setReferrerId(referrerId);
//            // WHEN / THEN
//            assertThrows(InvalidDataException.class, () -> this.videoGameService.addReferral(request));
//        }
//        @Test
//        void getDirectReferralsTest() {
//            // GIVEN
//            String customerId = "fakecustomerid";
//            List<ReferralRecord> recordList = new ArrayList<>();
//            ReferralRecord record1 = new ReferralRecord();
//            record1.setCustomerId("customer1");
//            record1.setReferrerId(customerId);
//            record1.setDateReferred(ZonedDateTime.now());
//            recordList.add(record1);
//            ReferralRecord record2 = new ReferralRecord();
//            record2.setCustomerId("customer2");
//            record2.setReferrerId(customerId);
//            record2.setDateReferred(ZonedDateTime.now());
//            recordList.add(record2);
//            when(referralDao.findByReferrerId(customerId)).thenReturn(recordList);
//            // WHEN
//            List<Referral> referrals = this.videoGameService.getDirectReferrals(customerId);
//            // THEN
//            verify(referralDao, times(1)).findByReferrerId(customerId);
//            assertNotNull(referrals, "The returned referral list is valid");
//            assertEquals(2, referrals.size(), "The referral list has 2 items");
//            for (Referral referral : referrals) {
//                if (record1.getCustomerId().equals(referral.getCustomerId())) {
//                    assertEquals(record1.getReferrerId(), customerId);
//                    assertEquals(new ZonedDateTimeConverter().convert(record1.getDateReferred()), referral.getReferralDate());
//                } else if (record2.getCustomerId().equals(referral.getCustomerId())) {
//                    assertEquals(record2.getReferrerId(), customerId);
//                    assertEquals(new ZonedDateTimeConverter().convert(record2.getDateReferred()), referral.getReferralDate());
//                } else {
//                    fail("A Referral was returned that does not match record 1 or 2.");
//                }
//            }
//        }
//        @Test
//        void getReferralLeaderboardTest() {
//            // GIVEN
//            List<ReferralRecord> records = List.of();
//            ReferralDao referralDao = mock(ReferralDao.class);
//            when(referralDao.findUsersWithoutReferrerId()).thenReturn(records);
//            ReferralService referralService = new ReferralService(referralDao);
//            // WHEN
//            List<LeaderboardEntry> leaderboard = referralService.getReferralLeaderboard();
//            // THEN
//            assertNotNull(leaderboard, "The returned leaderboard should not be null");
//            assertEquals(0, leaderboard.size(), "The leaderboard should be empty");
//        }
//    //}
//}
