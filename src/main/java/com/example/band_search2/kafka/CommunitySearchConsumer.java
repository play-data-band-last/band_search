package com.example.band_search2.kafka;

import com.example.band_search2.domain.request.CommunitySearchRequest;
import com.example.band_search2.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunitySearchConsumer {
    private final CommunityService communityService;

    @KafkaListener(topics = TopicConfig.communitySearch)
    public void listen(CommunitySearchRequest communitySearchRequest) {
        System.out.println("consumer : " + communitySearchRequest);

        CommunitySearchRequest c = CommunitySearchRequest.builder()
                .ownerId(communitySearchRequest.getOwnerId())
                .name(communitySearchRequest.getName())
                .location(communitySearchRequest.getLocation())
                .category(communitySearchRequest.getCategory())
                .interest(communitySearchRequest.getInterest())
                .description(communitySearchRequest.getDescription())
                .profileImage(communitySearchRequest.getProfileImage())
                .build();

        communityService.save(c.toEntity());
    }

    @KafkaListener(topics = TopicConfig.communitySearchDLT)
    public void dltListen(byte[] in) {
        log.info("dlt : " + new String(in));
    }



}
