package com.example.band_search2.service;

import com.example.band_search2.domain.entity.Community;
import com.example.band_search2.impl.CommunityServiceImpl;
import com.example.band_search2.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CommunityService implements CommunityServiceImpl {
    private final CommunityRepository repository;
    @Override
    public void save(Community community) {
        repository.save(community);
    }
    @Override
    public List<Community> searchByName(String name) {
        return repository.findByName(name);
    }
    @Override
    public List<Community> searchByLocation(String location) {
        return repository.findByLocation(location);
    }
    @Override
    public List<Community> searchByCategory(String category) {
        return repository.findByCategory(category);
    }
    @Override
    public List<Community> searchByDescription(String description) {
        return repository.findByDescription(description);
    }
    @Override
    public List<Community> searchByCommunityUUID(String communityUUID) {
        return repository.findByCommunityUUID(communityUUID);
    }

}
