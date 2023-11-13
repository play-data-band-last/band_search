package com.example.band_search2.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Document(indexName = "sink.community.communities", useServerConfiguration = true)
@Mapping(mappingPath = "elastic/mapping.json")
@Setting(settingPath = "elastic/setting.json")
public class Community {
    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long ownerId;

    @Field(type = FieldType.Long)
    private Long communityId;

    @Field(type = FieldType.Search_As_You_Type)
    private String name;

    @Field(type = FieldType.Text)
    private String location;

    @Field(type = FieldType.Text)
    private String category;

    @Field(type = FieldType.Text)
    private String interest;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String profileImage;

    @Field(type = FieldType.Keyword)
    private String communityUUID;

//    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//    private OffsetDateTime date;
}
