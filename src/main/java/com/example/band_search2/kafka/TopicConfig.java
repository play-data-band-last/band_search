package com.example.band_search2.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TopicConfig {
    public final static String communitySearch = "communitySearch";

    public final static String communitySearchDLT = "communitySearch.DLT";

    @Bean
    public NewTopic communitySearch() {
        return new NewTopic(communitySearch, 1, (short)1);
    }

    @Bean
    public NewTopic topicDLT() {
        return new NewTopic(communitySearchDLT, 1, (short) 1);
    }

}
