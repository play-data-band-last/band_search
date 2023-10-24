package com.example.band_search2.service;

import com.example.band_search2.domain.dto.RealTimeSearchKeyword;
import com.example.band_search2.domain.dto.TypoSuggestKeyword;
import com.example.band_search2.domain.entity.Community;
import com.example.band_search2.impl.SearchServiceImpl;
import com.example.band_search2.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchService implements SearchServiceImpl {
    private final RestHighLevelClient elasticsearchClient;
    private final CommunityRepository communityRepository;

    @Override
    public List<TypoSuggestKeyword> getTypoSuggest(String name) throws IOException {
        List<TypoSuggestKeyword> suggestKeywords = new ArrayList<>();

        // Bool 쿼리 생성
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("pecial_flag_name", name))
                .should(new FuzzyQueryBuilder("pecial_flag_name.keyword", name)
                        .fuzziness(Fuzziness.TWO));

        // 검색 요청 생성
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("accesslogs*");
        searchRequest.source(sourceBuilder);

        // Elasticsearch에 요청을 보내고 결과 받기
        SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        // 검색 결과에서 DTO로 변환하고 리스트에 추가 (score를 가져와서 RealTimeSearchKeyword 객체에 저장)
        List<TypoSuggestKeyword> allSuggestions = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            String key = hit.getSourceAsMap().get("pecial_flag_name").toString();
            float score = hit.getScore(); // score 가져오기
            TypoSuggestKeyword typoSuggestKeyword = new TypoSuggestKeyword(key, score);
            allSuggestions.add(typoSuggestKeyword);
        }

        // 가장 높은 score를 가진 요소만 추출
        float maxScore = allSuggestions.stream()
                .map(TypoSuggestKeyword::getScore)
                .max(Float::compareTo)
                .orElse(0f);

        // 모든 요소의 score가 최대 score와 같은지 확인하고 같으면 반환
        if (allSuggestions.stream().allMatch(suggestion -> suggestion.getScore() == maxScore)) {
            return new ArrayList<>();
        } else {
            // score가 모두 같지 않으면 가장 높은 score를 가진 요소만 반환
            List<TypoSuggestKeyword> maxScoreSuggestions = new ArrayList<>();
            for (TypoSuggestKeyword suggestion : allSuggestions) {
                if (suggestion.getScore() == maxScore) {
                    maxScoreSuggestions.add(suggestion);
                }
            }
            return maxScoreSuggestions;
        }
    }

    @Override
    public List<RealTimeSearchKeyword> getSuggestKeywords(String name) throws IOException {
        // 추천 검색어 담을  List 준비
        List<RealTimeSearchKeyword> suggestKeywords = new ArrayList<>();

        // Elasticsearch 질의 생성
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // log 중 queryString 기준으로 count 집계 후 top 5 추출..( 가중치 역할 )
        sourceBuilder.aggregation(AggregationBuilders.terms("duplicate_messages")
                .field("pecial_flag_name.keyword")
                .size(5));

        // multi_match 쿼리 생성
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(name)
                .field("name")
                .field("name._2gram")
                .field("name._3gram")
                .fuzziness(name.length() < 2 ? "auto" : 2);

        // Bool 쿼리로 조합하고 min_score 추가
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(multiMatchQueryBuilder);

        // 검색 결과에서 최소 점수를 지정
        sourceBuilder.query(boolQueryBuilder)
                .minScore(0.1F);

        sourceBuilder.query(boolQueryBuilder);

        // application 으로 시작하는 index 에서 검색..
        SearchRequest searchRequest = new SearchRequest("accesslogs*");
        searchRequest.source(sourceBuilder);

        // 검색 결과
        SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        // 검색 결과에서 집계결과만 빼옴..
        Terms duplicateMessages = response.getAggregations().get("duplicate_messages");

        if (duplicateMessages != null) {
            // 집계결과를 DTO로 변환..
            for (Terms.Bucket bucket : duplicateMessages.getBuckets()) {
                String key = bucket.getKeyAsString();

                long docCount = bucket.getDocCount();

                RealTimeSearchKeyword realTimeSearchKeyword = new RealTimeSearchKeyword(key, docCount);
                suggestKeywords.add(realTimeSearchKeyword);
            }
        }

        // 커뮤니티 이름으로도 검색..
        List<Community> allByName = communityRepository.findAllByName(name);

        for (Community community : allByName) {
            RealTimeSearchKeyword realTimeSearchKeyword = new RealTimeSearchKeyword(community.getName(), 1L);

            suggestKeywords.add(realTimeSearchKeyword);
        }

        return suggestKeywords;
    }


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
                .field("pecial_flag_name.keyword")
                .size(5));

        // application 으로 시작하는 index 에서 검색..
        SearchRequest searchRequest = new SearchRequest("accesslogs*");
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

                long docCount = bucket.getDocCount();

                RealTimeSearchKeyword realTimeSearchKeyword = new RealTimeSearchKeyword(encodedValue, docCount);
                realTimeSearchKeywords.add(realTimeSearchKeyword);
            }
        }


        return realTimeSearchKeywords;
    }
}