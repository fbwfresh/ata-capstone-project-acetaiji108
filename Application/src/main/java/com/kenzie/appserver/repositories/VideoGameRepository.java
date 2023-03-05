package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.VideoGameRecord;
import org.springframework.data.repository.CrudRepository;

public interface VideoGameRepository extends CrudRepository<VideoGameRecord,String> {
}
