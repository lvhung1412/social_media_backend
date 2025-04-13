package com.example.SocialMedia.service.impl;

import com.example.SocialMedia.repository.CommentRepository;
import com.example.SocialMedia.repository.PostRepository;
import com.example.SocialMedia.repository.ReactionCommentRepository;
import com.example.SocialMedia.repository.ReactionRepository;
import com.example.SocialMedia.utils.ServiceUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentServiceImpl {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReactionRepository reactionRepository;

    @Autowired
    ReactionCommentRepository reactionCommentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ServiceUtils serviceUtils;
}
