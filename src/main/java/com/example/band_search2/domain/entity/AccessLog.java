package com.example.band_search2.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Document(indexName = "accesslogs", useServerConfiguration = true)
@Mapping(mappingPath = "logs/mapping.json")
@Setting(settingPath = "logs/setting.json")
public class AccessLog {
    @Id
    private String id;

    @Field(type = FieldType.Search_As_You_Type)
    private String name;

}
