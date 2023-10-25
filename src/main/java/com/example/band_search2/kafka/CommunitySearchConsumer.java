package com.example.band_search2.kafka;

import com.example.band_search2.domain.entity.Community;
import com.example.band_search2.domain.request.CommunityRequest;
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
    public void listen(CommunityRequest communityRequest) {
        System.out.println("consumer : " + communityRequest);

        communityService.save(communityRequest.toEntity());
    }

    @KafkaListener(topics = TopicConfig.communitySearchDLT)
    public void dltListen(byte[] in) {
        log.info("dlt : " + new String(in));
    }



}
