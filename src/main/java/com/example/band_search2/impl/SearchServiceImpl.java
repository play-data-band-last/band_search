package com.example.band_search2.impl;

import com.example.band_search2.domain.dto.RealTimeSearchKeyword;
import com.example.band_search2.domain.dto.TypoSuggestKeyword;

import java.io.IOException;
import java.util.List;

public interface SearchServiceImpl {
    List<RealTimeSearchKeyword> getRecentTop5Keywords() throws IOException;
    List<RealTimeSearchKeyword> getSuggestKeywords(String name) throws IOException;
    List<TypoSuggestKeyword> getTypoSuggest(String string) throws IOException;
}
