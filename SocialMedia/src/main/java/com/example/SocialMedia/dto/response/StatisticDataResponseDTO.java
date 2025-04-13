package com.example.SocialMedia.dto.response;

import com.example.SocialMedia.repository.model.ModelStatistic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDataResponseDTO {
    private Long countUser;
    private Long countPost;
    private Long countComment;
    private List<ModelStatistic> countUserOfMonth;
    private List<ModelStatistic> countPostOfMonth;
    private List<ModelStatistic> countCommentOfMonth;
}
