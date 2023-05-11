package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.VideoGameRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
@EnableScan
public interface VideoGameRepository extends CrudRepository<VideoGameRecord,String> {

}
