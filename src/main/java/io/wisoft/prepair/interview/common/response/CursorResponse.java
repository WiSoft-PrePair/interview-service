package io.wisoft.prepair.interview.common.response;


import java.time.LocalDate;
import java.util.List;

public record CursorResponse<T>(
        List<T> contents,
        LocalDate nextCursor,
        boolean hasNext
) {
    public static <T> CursorResponse<T> of(List<T> contents, LocalDate nextCursor, boolean hasNext) {
        return new CursorResponse<>(contents, nextCursor, hasNext);
    }
}
