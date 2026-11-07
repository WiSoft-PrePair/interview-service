package io.wisoft.prepair.interview.external.member.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Frequency {
    @JsonProperty("every") EVERY,
    @JsonProperty("weekly") WEEKLY
}
