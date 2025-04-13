package com.example.SocialMedia.config;

import com.example.SocialMedia.dto.response.*;
import com.example.SocialMedia.entity.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        //Mapper list
        var listCommentOfPost = generateListConverter(Comment.class, CommentOfPostResponseDTO.class, modelMapper);
        var listFileOfPost = generateListConverter(File.class, FileOfPostResponseDTO.class, modelMapper);
        var listReactionOfPost = generateListConverter(ReactionPost.class, ReactionOfPostResponseDTO.class, modelMapper);
        var listReactionOfComment = generateListConverter(ReactionComment.class, ReactionOfCommentResponseDTO.class, modelMapper);

        //Mapper Post -> PostResponseDTO
        modelMapper.createTypeMap(Post.class, PostResponseDTO.class).addMappings(m -> {
            m.using(listFileOfPost).map(Post::getFiles, PostResponseDTO::setFiles);
            m.map(Post::getId, PostResponseDTO::setId);
            m.map(Post::getUser, PostResponseDTO::setUser);
            m.map(Post::getPost, PostResponseDTO::setPost);
            m.map(Post::getCreateDate, PostResponseDTO::setCreateDate);
            m.map(Post::getValue, PostResponseDTO::setValue);
            m.map(Post::getStatus, PostResponseDTO::setStatus);
        });
        //Mapper Comment -> CommentResponseDTO
        modelMapper.createTypeMap(Comment.class, CommentResponseDTO.class).addMappings(m -> {
            m.using(listReactionOfComment).map(Comment::getReactions, CommentResponseDTO::setReactions);
            m.map(Comment::getId, CommentResponseDTO::setId);
            m.map(Comment::getUser, CommentResponseDTO::setUser);
            m.map(Comment::getPost, CommentResponseDTO::setPost);
            m.map(Comment::getCreateDate, CommentResponseDTO::setCreateDate);
            m.map(Comment::getType, CommentResponseDTO::setType);
            m.map(Comment::getValue, CommentResponseDTO::setValue);
            m.map(Comment::getStatus, CommentResponseDTO::setStatus);
            m.map(Comment::getComment, CommentResponseDTO::setComment);
        });
        //Mapper Comment -> CommentOfPostResponseDTO
        modelMapper.createTypeMap(Comment.class, CommentOfPostResponseDTO.class).addMappings(m -> {
            m.using(listReactionOfComment).map(Comment::getReactions, CommentOfPostResponseDTO::setReactions);
            m.map(Comment::getId, CommentOfPostResponseDTO::setId);
            m.map(Comment::getUser, CommentOfPostResponseDTO::setUser);
            m.map(Comment::getCreateDate, CommentOfPostResponseDTO::setCreateDate);
            m.map(Comment::getValue, CommentOfPostResponseDTO::setValue);
            m.map(Comment::getType, CommentOfPostResponseDTO::setType);
            m.map(Comment::getStatus, CommentOfPostResponseDTO::setStatus);
        });
        //Mapper Comment -> CommentFatherResponseDTO
        modelMapper.createTypeMap(Comment.class, CommentFatherResponseDTO.class).addMappings( m -> {
            m.map(Comment::getId, CommentFatherResponseDTO::setId);
            m.map(Comment::getUser, CommentFatherResponseDTO::setUser);
            m.map(Comment::getValue, CommentFatherResponseDTO::setValue);
            m.map(Comment::getCreateDate, CommentFatherResponseDTO::setCreateDate);
            m.map(Comment::getType, CommentFatherResponseDTO::setType);
            m.map(Comment::getStatus, CommentFatherResponseDTO::setStatus);
        });

        return modelMapper;
    }

    private <T,V> Converter<List<T>, List<V>> generateListConverter(Class<T> src, Class<V> dst, ModelMapper mapper) {
        return c -> {
            if (c.getSource() == null)
                return null;
            else
                return c.getSource().stream().map(m -> mapper.map(m, dst)).toList();
        };
    }
}
