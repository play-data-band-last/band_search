package com.example.band_search2.repository;

import com.example.band_search2.domain.entity.AccessLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccessLogRepository extends CrudRepository<AccessLog, String> {

}