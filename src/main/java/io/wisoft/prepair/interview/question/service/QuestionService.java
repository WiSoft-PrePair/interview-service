package io.wisoft.prepair.interview.question.service;

import io.wisoft.prepair.interview.common.response.CursorResponse;
import io.wisoft.prepair.interview.question.dto.TagCount;
import io.wisoft.prepair.interview.question.dto.request.QuestionCursorRequest;
import io.wisoft.prepair.interview.question.dto.response.QuestionPreview;
import io.wisoft.prepair.interview.question.dto.response.TagCountResponse;
import io.wisoft.prepair.interview.question.entity.Question;
import io.wisoft.prepair.interview.question.entity.enums.QuestionTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionReader questionReader;

    public CursorResponse<QuestionPreview> getQuestions(UUID memberId, QuestionCursorRequest request) {
        Slice<Question> questions = questionReader
                .getQuestions(
                        memberId,
                        request.tag(),
                        normalize(request.keyword()),
                        request.cursor(),
                        PageRequest.of(0, request.size())
                );

        List<QuestionPreview> contents = questions.getContent().stream()
                .map(QuestionPreview::from)
                .toList();

        LocalDate nextCursor = questions.hasNext()
                ? questions.getContent().getLast().getAssignedDate()
                : null;

        return CursorResponse.of(contents, nextCursor, questions.hasNext());
    }

    // 검색어: 대소문자·공백 무시, LIKE 특수문자(\ % _)는 글자 그대로 찾도록 이스케이프
    private String normalize(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "")
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    public TagCountResponse getTagCounts(UUID memberId) {
        List<TagCount> tagCounts = questionReader.countByTag(memberId);

        Map<QuestionTag, Long> counts = new EnumMap<>(QuestionTag.class);

        for (QuestionTag tag : QuestionTag.values()) {
            counts.put(tag, 0L);
        }

        tagCounts.forEach(tagCount ->
                counts.put(tagCount.tag(), tagCount.count())
        );

        long total = tagCounts.stream()
                .mapToLong(TagCount::count)
                .sum();

        return TagCountResponse.of(total, counts);
    }
}
