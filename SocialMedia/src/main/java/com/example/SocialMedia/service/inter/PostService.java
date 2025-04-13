package com.example.SocialMedia.service.inter;

import com.example.SocialMedia.dto.request.PostRequestDTO;
import com.example.SocialMedia.dto.request.ReactionRequestDTO;
import com.example.SocialMedia.dto.request.UpdatePostRequestDTO;
import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.ListResponse;
import com.example.SocialMedia.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    DataResponse<?> createPost(User user, PostRequestDTO postRequestDTO, MultipartFile[] files);

    DataResponse<?> uploadFilePost(String postId, MultipartFile[] files, Integer[] types);

    DataResponse<?> updatePost(String id, UpdatePostRequestDTO postRequestDTO, User user);

    DataResponse<?> deletePost(String id);

    DataResponse<?> getPostById(String id, User user);

    DataResponse<?> hiddenPost(String id);

    DataResponse<?> changeSecurityPost(String postId, String security, User user);

    ListResponse<?> getListPostOfUser(User user);

    ListResponse<?> getAllPostOfUser(User user);

    ListResponse<?> getNewFeedPost(Integer page, User user);

    ListResponse<?> getListPostOfUserName(String username, User user);

    ListResponse<?> getListReactionByPostAndPageSize(String postId, Integer size);

    DataResponse<?> sharePost(String postId, User user);

    DataResponse<?> reactionPost(String postId, ReactionRequestDTO reactionRequestDTO, User user);

    DataResponse<?> unReactionPost(String postId, User user);

    ListResponse<?> getListPostOfPage(Integer page);

    DataResponse<?> disablePost(String id);

    DataResponse<?> enablePost(String id);
}
