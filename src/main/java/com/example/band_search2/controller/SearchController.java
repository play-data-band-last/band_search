package com.example.band_search2.controller;

import com.example.band_search2.domain.dto.RealTimeSearchKeyword;
import com.example.band_search2.domain.entity.AccessLog;
import com.example.band_search2.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SearchController {
    private final SearchService customLogService;

    // 실시간 검색 - 최근 1시간 까지의 검색 중 가장많이 검색된 keyword top 5..
    @GetMapping("/realTimeKeyword")
    public List<RealTimeSearchKeyword> getRealTimeKeywordTop5() throws IOException {
        return customLogService.getRecentTop5Keywords();
    }

    @GetMapping("/suggestKeywords")
    public List<RealTimeSearchKeyword> getSuggestKeywords(@RequestParam String name) throws IOException {
        return customLogService.getSuggestKeywords(name);
    }
}
