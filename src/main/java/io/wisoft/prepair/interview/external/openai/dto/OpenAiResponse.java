package io.wisoft.prepair.interview.external.openai.dto;

import java.util.List;

public record OpenAiResponse(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ) {
    }

    public String getContent() {
        return choices.get(0).message().content();
    }
}
