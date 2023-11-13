package com.example.band_search2.domain.request;

import com.example.band_search2.domain.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunitySearchRequest {
    private Long ownerId;
    private String name;
    private String location;
    private String category;
    private String interest;
    private String description;
    private String profileImage;
    private String communityUUID;
    private Long communityId;

    public Community toEntity(){
        return Community.builder()
                .id(UUID.randomUUID().toString())
                .ownerId(ownerId)
                .name(name)
                .location(location)
                .category(category)
                .interest(interest)
                .description(description)
                .profileImage(profileImage)
                .communityUUID(UUID.randomUUID().toString())
                .communityId(communityId)
                .build();
    }
}
