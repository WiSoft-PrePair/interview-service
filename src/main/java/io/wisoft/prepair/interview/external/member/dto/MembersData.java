package io.wisoft.prepair.interview.external.member.dto;

import java.util.List;

public record MembersData(
        List<MemberProfile> members,
        int total,
        int limit,
        int offset
) {
}