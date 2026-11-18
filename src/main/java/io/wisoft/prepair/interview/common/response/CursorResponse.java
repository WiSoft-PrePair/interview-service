package io.wisoft.prepair.interview.common.response;


import java.time.Instant;
import java.util.List;

public record CursorResponse<T>(
        List<T> contents,
        Instant nextCursor,
        boolean hasNext
) {
    public static <T> CursorResponse<T> of(List<T> contents, Instant nextCursor, boolean hasNext) {
        return new CursorResponse<>(contents, nextCursor, hasNext);
    }
}
