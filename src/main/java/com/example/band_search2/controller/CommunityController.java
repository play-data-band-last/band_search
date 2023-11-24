package com.example.band_search2.controller;

import com.example.band_search2.domain.entity.Community;
import com.example.band_search2.domain.request.CommunitySearchRequest;
import com.example.band_search2.impl.CommunityServiceImpl;
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
    public void save(@RequestBody CommunitySearchRequest communitySearchRequest) {
        communityServiceImpl.save(communitySearchRequest.toEntity());
    }

    @GetMapping("/name")
    public List<Community> searchByName(@RequestParam String name) {

        List<Community> communities = communityServiceImpl.searchByName(name);

        for (int i = 0; i < communities.size(); i++) {
            System.out.println(communities.get(i));
        }

        return communities;
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

    @GetMapping("/communitySearch")
    public List<Community> searchByCommunityNameAndDesc(@RequestParam String name) {
        return communityServiceImpl.searchByCommunityNameAndDesc(name, name);
    }

}
