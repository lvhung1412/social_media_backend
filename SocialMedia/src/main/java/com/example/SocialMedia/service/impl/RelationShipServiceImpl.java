package com.example.SocialMedia.service.impl;

import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.ListResponse;
import com.example.SocialMedia.dto.response.RelationShipResponseDTO;
import com.example.SocialMedia.entity.RelationShip;
import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.entity.enums.RelationEnum;
import com.example.SocialMedia.entity.keys.RelationShipKey;
import com.example.SocialMedia.exceptions.InvalidValueException;
import com.example.SocialMedia.repository.RelationShipRepository;
import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.service.inter.RelationShipService;
import com.example.SocialMedia.utils.ServiceUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RelationShipServiceImpl implements RelationShipService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RelationShipRepository relationShipRepository;

    @Autowired
    ServiceUtils serviceUtils;

    @Override
    public ListResponse<?> getAllRelationShipByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Can't find user " + username)
        );

        return serviceUtils.convertToListResponse(
                relationShipRepository.findAllByUserFromOrUserToAndStatus(user, user, RelationEnum.FRIEND.toString()),
                RelationShipResponseDTO.class
        );
    }

    @Override
    public ListResponse<?> getAllRelationShipByUser(User user) {
        return serviceUtils.convertToListResponse(
                relationShipRepository.findAllByUserFromOrUserToAndStatus(user, user, RelationEnum.FRIEND.toString()),
                RelationShipResponseDTO.class
        );
    }

    @Override
    public ListResponse<?> getAllInvitationFriend(User user) {
        return serviceUtils.convertToListResponse(
                relationShipRepository.findAllByUserToAndStatus(user, RelationEnum.WAITING.toString()),
                RelationShipResponseDTO.class
        );
    }

    @Override
    public ListResponse<?> getAllFriend(User user) {
        List<RelationShip> list = relationShipRepository.findAllByUserFromOrUserToAndStatus(user,user, RelationEnum.FRIEND.toString());
        List<RelationShip> result = new ArrayList<>();
        for(RelationShip relationShip: list){
            if(relationShip.getStatus().equals( RelationEnum.FRIEND.toString())){
                result.add(relationShip);
            }
        }
        return serviceUtils.convertToListResponse(
                result,
                RelationShipResponseDTO.class
        );
    }

    @Override
    public DataResponse<?> getRelationShipById(User user, String username) {
        Optional<RelationShip> relationShip =  relationShipRepository.findById(new RelationShipKey(user.getUsername(), username));
        //check relationship user1 ->  user 2
        if(relationShip.isPresent()){
            return serviceUtils.convertToDataResponse(relationShip.get(), RelationShipResponseDTO.class);
        }
        //check relationship user2 ->  user 1
        relationShip =  relationShipRepository.findById(new RelationShipKey(username, user.getUsername()));
        if(relationShip.isPresent()){
            return serviceUtils.convertToDataResponse(relationShip.get(), RelationShipResponseDTO.class);
        }
        return new DataResponse<>();
    }

    @Override
    public DataResponse createRelationShip(User userFrom, String usernameTo) {
        User userTo = userRepository.findUserByUsername(usernameTo)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user " + usernameTo));

        RelationShip relationShip = new RelationShip();
        relationShip.setUserFrom(userFrom);
        relationShip.setUserTo(userTo);
        relationShip.setStatus(RelationEnum.WAITING.toString());
        return serviceUtils.convertToDataResponse(relationShipRepository.save(relationShip), RelationShipResponseDTO.class) ;
    }

    @Override
    public DataResponse<?> updateRelationShip(User userFrom, String usernameTo, String status) {
        User userTo = userRepository.findUserByUsername(usernameTo)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user " + usernameTo));
        Optional<RelationShip> relationShipOptional = relationShipRepository.findById(new RelationShipKey(userFrom.getUsername(), usernameTo));
        RelationShip relationShip = new RelationShip();
        //check relationship user1 ->  user 2
        if(relationShipOptional.isPresent()){
            relationShip = relationShipOptional.get();
        }else {
            //check relationship user1 ->  user 2
            relationShipOptional = relationShipRepository.findById(new RelationShipKey(usernameTo,userFrom.getUsername()));
            if(relationShipOptional.isPresent()){
                relationShip = relationShipOptional.get();
            }else {
                throw new ResourceNotFoundException("Can't find relationship");
            }
        }
        relationShip.setStatus(status);
        return serviceUtils.convertToDataResponse(relationShipRepository.save(relationShip), RelationShipResponseDTO.class) ;
    }

    @Override
    public DataResponse<?> deleteRelationShip(User user, String friend) {
        User userTo = userRepository.findUserByUsername(friend)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user " + friend));
        Optional<RelationShip> relationShip=relationShipRepository.findById(new RelationShipKey(user.getUsername(), friend));
        if(relationShip.isPresent()){
            relationShipRepository.delete(relationShip.get());
        }else{
            relationShip=relationShipRepository.findById(new RelationShipKey(friend, user.getUsername()));
            if(relationShip.isPresent()){
                relationShipRepository.delete(relationShip.get());
            }else{
                throw new InvalidValueException("Can't delete relationship!");
            }
        }

        DataResponse<Object> dataResponse = new DataResponse<>();
        dataResponse.setMessage("Delete Relationship successful!");
        return dataResponse;
    }
}
