package com.kenzie.capstone.service.caching;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kenzie.capstone.service.model.VideoGameRecord;
import com.kenzie.capstone.service.dao.NonCachingVideoGameDao;
import com.kenzie.capstone.service.dao.VideoGameDao;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

public class CachingVideoGameDao implements VideoGameDao {
    private static final int VIDEOGAME_READ_TTL = 24 * (60 * 60);
    private static final String VIDEO_GAME_KEY = "VideoGameRecord::%s";
    private final CacheClient cacheClient;
    private final NonCachingVideoGameDao nonCachingVideoGameDao;
    private GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
    private Gson gson = builder.create();

    // Converting out of the cache
    private VideoGameRecord fromJson(String json) {
        return gson.fromJson(json, new TypeToken<VideoGameRecord>() {
        }.getType());
    }

    private void addToCache(VideoGameRecord record) {
        String cacheKey = String.format(VIDEO_GAME_KEY, record.getName());
        /* implementation for cache key */
        cacheClient.setValue(cacheKey,
                VIDEOGAME_READ_TTL,
                gson.toJson(record)
        );
    }

    @Inject
    public CachingVideoGameDao(CacheClient cache, NonCachingVideoGameDao videoGameDao) {
        this.nonCachingVideoGameDao = videoGameDao;
        this.cacheClient = cache;
    }

    @Override
    public VideoGameRecord addVideoGame(VideoGameRecord record) {
        addToCache(record);
        return record;
    }

    @Override
    public boolean deleteVideoGame(VideoGameRecord record) {
        cacheClient.invalidate(record.getName());
        if (cacheClient.getValue(record.getName()).isPresent()) {
            return false;
        }
        return true;
    }

    @Override
    public VideoGameRecord findByName(String name) {
        VideoGameRecord record = fromJson(cacheClient.getValue(name).get());
        if (record == null) {
            record = nonCachingVideoGameDao.findByName(name);
            cacheClient.setValue(name, VIDEOGAME_READ_TTL, gson.toJson(record));
        }
        return record;
    }

    @Override
    public List<VideoGameRecord> getAllGames() {
        return nonCachingVideoGameDao.getAllGames();
    }

    @Override
    public VideoGameRecord updateVideoGame(VideoGameRecord record) {
        cacheClient.invalidate(record.getName());
        addToCache(record);
        return record;
    }
}

//    private List<ReferralRecord> listOfRecords;
//    // Create the Gson object with instructions for ZonedDateTime
//    private GsonBuilder builder = new GsonBuilder().registerTypeAdapter(
//            ZonedDateTime.class,
//            new TypeAdapter<ZonedDateTime>() {
//                @Override
//                public void write(JsonWriter out, ZonedDateTime value) throws IOException {
//                    out.value(value.toString());
//                }
//                @Override
//                public ZonedDateTime read(JsonReader in) throws IOException {
//                    return ZonedDateTime.parse(in.nextString());
//                }
//            }
//    ).enableComplexMapKeySerialization();
//    // Store this in your class
//    private Gson gson = builder.create();
//
//    @Inject
//    public CachingReferralDao(CacheClient cache, NonCachingReferralDao referralDao){
//        this.nonCachingReferralDao = referralDao;
//        this.cacheClient = cache;
//    }
//    // Converting out of the cache
//    private List<ReferralRecord> fromJson(String json) {
//        return gson.fromJson(json, new TypeToken<ArrayList<ReferralRecord>>() { }.getType());
//    }
//    // Setting value
//    private void addToCache(List<ReferralRecord> records) {
//        // for (ReferralRecord record : records) {
//        cacheClient.setValue(REFERRAL_KEY
//                /* your implementation for cache key */,
//                REFERRAL_READ_TTL,
//                gson.toJson(records)
//        );
//        // }
//    }
//    @Override
//    public ReferralRecord addReferral(ReferralRecord referral) {
//        cacheClient.invalidate(referral.getReferrerId());
//        return nonCachingReferralDao.addReferral(referral);
//        //  cacheClient.setValue(referral.getReferrerId(),REFERRAL_READ_TTL, referral.getCustomerId()); //could be the other way around
//        //   return referral;
//    }
//
//    @Override
//    public boolean deleteReferral(ReferralRecord referral) {
//        cacheClient.invalidate(referral.getReferrerId());
//        return nonCachingReferralDao.deleteReferral(referral);
////        if(cacheClient.getValue(referral.getReferrerId()).isPresent()) {
////            cacheClient.invalidate(referral.getReferrerId());
////            return true;
////        }
////        return false;
//    }
//
//    @Override
//    public List<ReferralRecord> findByReferrerId(String referrerId) {
//        // Look up data in cache
//        // Convert between JSON
//        // If the data doesn't exist in the cache,
//        // Get the data from the data source
//        // Add data to the cache, convert between JSON
//        List<ReferralRecord> referralRecordList = fromJson(cacheClient.getValue(referrerId).get());
//        if(referralRecordList == null && referralRecordList.isEmpty()){
//            referralRecordList = nonCachingReferralDao.findByReferrerId(referrerId);
//            cacheClient.setValue(referrerId,REFERRAL_READ_TTL, gson.toJson(referralRecordList));
//        }
//        return referralRecordList;
//    }
//
//    @Override
//    public List<ReferralRecord> findUsersWithoutReferrerId() {
//        return nonCachingReferralDao.findUsersWithoutReferrerId();
//        // return null;
//    }


