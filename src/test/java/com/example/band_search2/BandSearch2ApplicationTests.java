package com.example.band_search2;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
class BandSearch2ApplicationTests {

    @Test
    void contextLoads() {
    }

//    @Test
//    void directConnectionTest() throws JsonProcessingException {
//        String str = "{\n" +
//                "  \"query\": {\n" +
//                "    \"bool\": {\n" +
//                "      \"must\": [\n" +
//                "        {\n" +
//                "          \"match\": {\n" +
//                "            \"requestURI\": \"/name\"\n" +
//                "          }\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"range\": {\n" +
//                "            \"date\": {\n" +
//                "              \"gte\": \"2023-10-18T09:38:00\",\n" +
//                "              \"lte\": \"2023-10-18T11:00:00\"\n" +
//                "            }\n" +
//                "          }\n" +
//                "        }\n" +
//                "      ]\n" +
//                "    }\n" +
//                "  },\n" +
//                "  \"_source\": [\"date\", \"requestURI\", \"queryString\"]\n" +
//                "}";
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpEntity<String> request = new HttpEntity<String>(str, httpHeaders);
//
//        String personResultAsJsonStr =
//                restTemplate.postForObject("http://localhost:9200/application-accesslog-2023-10-18/_search", request, String.class);
//        System.out.println("personResultAsJsonStr = " + personResultAsJsonStr);
//    }

}
