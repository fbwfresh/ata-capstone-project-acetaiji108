package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.VideoGameService;

import com.kenzie.capstone.service.dao.VideoGameDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public VideoGameService provideVideoGameService(@Named("VideoGameDao") VideoGameDao videoGameDao) {
        return new VideoGameService(videoGameDao);
    }
}

