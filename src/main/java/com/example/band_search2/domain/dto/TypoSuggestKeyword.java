package com.example.band_search2.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class TypoSuggestKeyword {
    private String key;
    private float score;
}
