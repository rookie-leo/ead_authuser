package com.ead.authuser.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePageDto<T> extends PageImpl<T> {

    private final PageMetadata page;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePageDto(
            @JsonProperty("content") List<T> content,
            @JsonProperty("page") PageMetadata page
    ) {
        super(content, PageRequest.of(page.number(), page.size()), page.totalElements());
        this.page = page;
    }

    public PageMetadata getPage() {
        return page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PageMetadata(int size, long totalElements, int totalPages, int number) {
        @JsonCreator
        public PageMetadata(
                @JsonProperty("size") int size,
                @JsonProperty("totalElements") long totalElements,
                @JsonProperty("totalPages") int totalPages,
                @JsonProperty("number") int number) {
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.number = number;
        }
    }
}
