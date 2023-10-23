package com.example.band_search2.controller;

import com.example.band_search2.domain.entity.Community;
import com.example.band_search2.domain.request.CommunityRequest;
import com.example.band_search2.impl.CommunityServiceImpl;
import com.example.band_search2.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommunityController {
    // 필드별 검색..

    private final CommunityServiceImpl communityServiceImpl;

    @PostMapping
    public void save(@RequestBody CommunityRequest communityRequest) {
        communityServiceImpl.save(communityRequest.toEntity());
    }

    @GetMapping("/name")
    public List<Community> searchByName(@RequestParam String name) {
        return communityServiceImpl.searchByName(name);
    }

    @GetMapping("/location")
    public List<Community> searchByLocation(@RequestParam String location) {
        return communityServiceImpl.searchByLocation(location);
    }

    @GetMapping("/category")
    public List<Community> searchByCategory(@RequestParam String category) {
        return communityServiceImpl.searchByCategory(category);
    }

    @GetMapping("/description")
    public List<Community> searchByDescription(@RequestParam String description) {
        return communityServiceImpl.searchByDescription(description);
    }

    @GetMapping("/communityUUID")
    public List<Community> searchByCommunityUUID(@RequestParam String communityUUID) {
        return communityServiceImpl.searchByCommunityUUID(communityUUID);
    }
}
