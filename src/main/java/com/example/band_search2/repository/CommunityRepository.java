package com.example.band_search2.repository;

import com.example.band_search2.domain.entity.Community;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommunityRepository extends CrudRepository<Community, String> {
    List<Community> findAllByName(String name);
    List<Community> findByLocation(String location);
    List<Community> findByCategory(String category);
    List<Community> findByDescription(String description);
    List<Community> findByCommunityUUID(String communityUUID);
}