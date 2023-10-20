package com.example.band_search2.service;

import com.example.band_search2.domain.dto.RealTimeSearchKeyword;
import com.example.band_search2.impl.SearchServiceImpl;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchService implements SearchServiceImpl {
    private final RestHighLevelClient elasticsearchClient;
    @Override
    public List<RealTimeSearchKeyword> getRecentTop5Keywords() throws IOException {
        // 실검 담을  List 준비
        List<RealTimeSearchKeyword> realTimeSearchKeywords = new ArrayList<>();

        // 최근 1시간..
        // 시간을 비교할때 Long으로 넘겨줘야함..
        // 기본적으로 dynamic finder가 long으로 비교한다고 한다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime truncatedNow = now.truncatedTo(ChronoUnit.HOURS);
        long startMillis = truncatedNow.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMillis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Elasticsearch 질의 생성
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // timestamp 기준 최근 1시간 전 까지..
        sourceBuilder.query(QueryBuilders.rangeQuery("@timestamp").gte(startMillis).lte(endMillis));

        // log 중 queryString 기준으로 count 집계 후 top 5 추출..
        sourceBuilder.aggregation(AggregationBuilders.terms("duplicate_messages")
                .field("queryString.keyword")
                .size(5));

        // application 으로 시작하는 index 에서 검색..
        SearchRequest searchRequest = new SearchRequest("application*");
        searchRequest.source(sourceBuilder);

        // 검색 결과
        SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        // 검색 결과에서 집계결과만 빼옴..
        Terms duplicateMessages = response.getAggregations().get("duplicate_messages");

        if (duplicateMessages != null) {
            // 집계결과를 DTO로 변환..
            for (Terms.Bucket bucket : duplicateMessages.getBuckets()) {
                String key = bucket.getKeyAsString();

                // "?name=" 제거..
                String encodedValue = key.replace("?name=", "");

                try {
                    // decode..
                    String decodedValue = URLDecoder.decode(encodedValue, "UTF-8");
                    long docCount = bucket.getDocCount();

                    RealTimeSearchKeyword realTimeSearchKeyword = new RealTimeSearchKeyword(decodedValue, docCount);
                    realTimeSearchKeywords.add(realTimeSearchKeyword);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }

        return realTimeSearchKeywords;
    }
}