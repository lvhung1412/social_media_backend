package com.example.SocialMedia.service.inter;

import com.example.SocialMedia.dto.request.CommentRequestDTO;
import com.example.SocialMedia.dto.request.ReactionRequestDTO;
import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.ListResponse;
import com.example.SocialMedia.entity.User;

public interface CommentService {
    DataResponse<?> createComment(User user, CommentRequestDTO commentRequestDTO);

    DataResponse<?> updateComment(String id, String content);

    DataResponse<?> hideComment(String id);

    DataResponse<?> replyComment(String commentId, User user, String content);

    DataResponse<?> reactionComment(String commentId, ReactionRequestDTO reactionRequestDTO, User user);

    DataResponse<?> unReactionComment(String commentId, User user);

    ListResponse<?> getAllCommentByPostAndPageSize(String postId, Integer size);

    ListResponse<?> getAllCommentByFather(String fatherId);

    ListResponse<?> getListCommentOfPage(Integer page);
    DataResponse<?> disableComment(String id);
    DataResponse<?> enableComment(String id);
}
