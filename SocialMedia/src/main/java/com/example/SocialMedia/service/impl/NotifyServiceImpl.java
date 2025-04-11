package com.example.SocialMedia.service.impl;

import com.example.SocialMedia.dto.request.NotifyRequestDTO;
import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.ListResponse;
import com.example.SocialMedia.dto.response.NotifyResponseDTO;
import com.example.SocialMedia.entity.Notify;
import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.entity.enums.StatusEnum;
import com.example.SocialMedia.repository.NotifyRepository;
import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.service.inter.NotifyService;
import com.example.SocialMedia.utils.ServiceUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    ServiceUtils serviceUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotifyRepository notifyRepository;

    @Override
    public ListResponse<?> getAllNotifyByUser(User user) {
        return serviceUtils.convertToListResponse(notifyRepository.findAllByUser(user, Sort.by("createDate").descending()), NotifyResponseDTO.class);
    }

    @Override
    public DataResponse<?> createNotify(NotifyRequestDTO notifyRequestDTO) {
        Notify notify = new Notify(
                serviceUtils.GenerateID(),
                userRepository.findUserByUsername(notifyRequestDTO.getUsername()).orElseThrow(
                        () -> new ResourceNotFoundException("Can't find user "+notifyRequestDTO.getUsername())
                ),
                new Date(),
                notifyRequestDTO.getContent(),
                StatusEnum.WAITING.toString()
        );
        return serviceUtils.convertToDataResponse(
                notifyRepository.save(notify),
                NotifyResponseDTO.class
        );
    }

    @Override
    public ListResponse<?> seenNotify(String[] ids) {
        List<Notify> list = new ArrayList<>();
        for(String id : ids){
            Notify notify = notifyRepository.getReferenceById(id);
            notify.setStatus(StatusEnum.SEEN.toString());
            list.add(notifyRepository.save(notify));
        }
        return serviceUtils.convertToListResponse(list, NotifyResponseDTO.class);
    }
}
