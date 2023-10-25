package com.example.band_search2.impl;

import com.example.band_search2.domain.entity.Community;

import java.util.List;

public interface CommunityServiceImpl {
    void save(Community community);
    List<Community> searchByName(String name);
    List<Community> searchByLocation(String location);
    List<Community> searchByCategory(String category);
    List<Community> searchByDescription(String description);
    List<Community> searchByCommunityUUID(String communityUUID);
    List<Community> searchByCommunityNameAndDesc(String name, String desc);

}
