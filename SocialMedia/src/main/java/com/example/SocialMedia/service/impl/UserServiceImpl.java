package com.example.SocialMedia.service.impl;

import com.example.SocialMedia.dto.request.*;
import com.example.SocialMedia.dto.response.*;
import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.entity.enums.GenderEnum;
import com.example.SocialMedia.entity.enums.RelationEnum;
import com.example.SocialMedia.entity.enums.RoleEnum;
import com.example.SocialMedia.entity.enums.StatusEnum;
import com.example.SocialMedia.exceptions.InvalidValueException;
import com.example.SocialMedia.exceptions.ResourceAlreadyExistsException;
import com.example.SocialMedia.repository.*;
import com.example.SocialMedia.repository.model.ModelCommonFriend;
import com.example.SocialMedia.service.inter.ImageService;
import com.example.SocialMedia.service.inter.UserService;
import com.example.SocialMedia.utils.JwtTokenUtil;
import com.example.SocialMedia.utils.ServiceUtils;
import jakarta.transaction.Transactional;
import org.apache.mahout.cf.taste.common.TasteException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    ImageService imageService;

    @Autowired
    ServiceUtils serviceUtils;

    @Autowired
    ModelMapper mapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    RelationShipRepository relationShipRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public DataResponse<?> saveUser(RegisterUserRequestDTO registerUserRequestDTO) {
        User user = mapper.map(registerUserRequestDTO, User.class);

        // check username exist
        Optional<User> userCheckUsername = userRepository.findUserByUsername(registerUserRequestDTO.getUsername());
        if(userCheckUsername.isPresent()){
            throw new ResourceAlreadyExistsException("Username user existed");
        }

        // check user phone exist
        Optional<User> userCheckPhone = userRepository.findUserByPhone(registerUserRequestDTO.getPhone());
        if (userCheckPhone.isPresent()) {
            throw new ResourceAlreadyExistsException("Phone user existed");
        }

        // Check email user existed
        Optional<User> userCheckEmail = userRepository.findUserByEmail(registerUserRequestDTO.getEmail());
        if (userCheckEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("Email user existed");
        }

        encodePassword(user);

        // check role already exist
        user.setRole(roleRepository.findRoleByName(RoleEnum.CUSTOMER.toString()).get());
        user.setEnable(true);
        user.setGender(GenderEnum.MALE.toString());
        user.setAvatar("https://storage.googleapis.com/leaf-5c2c4.appspot.com/39f94986-d898-49dd-b9eb-5ff979857ab9png");

        String randomCodeVerify = generateVerifyCode();
        user.setVerificationCode(randomCodeVerify);
        return serviceUtils.convertToDataResponse(userRepository.save(user), UserRepository.class);
    }

    @Override
    public DataResponse<?> updateUser(User user, UserUpdateRequestDTO userUpdateRequestDTO) {
        // check user
        if(userUpdateRequestDTO.getPhone() != null){
            if(!user.getPhone().equals(userUpdateRequestDTO.getPhone())){
                Optional<User> userOptional = userRepository.findUserByPhone(userUpdateRequestDTO.getPhone());
                if(userOptional.isPresent()){
                    throw new ResourceNotFoundException("Phone has already!");
                }
                user.setPhone(userUpdateRequestDTO.getPhone());
            }
        }

        // check name
        if (userUpdateRequestDTO.getName() != null){
            if(!user.getName().equals(userUpdateRequestDTO.getName())){
                user.setName(userUpdateRequestDTO.getName());
            }
        }

        //check birthday
        if(userUpdateRequestDTO.getBirthday() != null){
            if(user.getBirthday() != userUpdateRequestDTO.getBirthday()){
                user.setBirthday(userUpdateRequestDTO.getBirthday());
            }
        }

        //check bio
        if(userUpdateRequestDTO.getBio() != null){
            user.setBio(userUpdateRequestDTO.getBio());
        }

        //check gender
        if(userUpdateRequestDTO.getGender() != null){
            if(!user.getGender().equals(userUpdateRequestDTO.getGender())){
                user.setGender(userUpdateRequestDTO.getGender());
            }
        }

        //check nickname
        if(userUpdateRequestDTO.getNickname() != null){
            user.setNickname(userUpdateRequestDTO.getNickname());
        }

        //Check Security
        if(userUpdateRequestDTO.getSecurity() != null){
            user.setSecurity(userUpdateRequestDTO.getSecurity());
        }

        // new Token
        String token = jwtTokenUtil.generateToken(user);

        return new DataResponse<>( new LoginResponseDTO<>(token, mapper.map( userRepository.save(user), UserResponseDTO.class )));
    }

    @Override
    public DataResponse<?> changePassword(String username, ChangePasswordRequestDTO changePasswordRequestDTO) {
        User userExists = userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + username));
        String oldPassword = changePasswordRequestDTO.getOldPassword();
        String newPassword = changePasswordRequestDTO.getNewPassword();
        String verification = changePasswordRequestDTO.getVerificationCode();

        // check verify
        if(userExists.getVerificationCode().equals(verification)) {
            if(userExists.getPassword().equals(oldPassword)){
                userExists.setPassword(newPassword);
                encodePassword(userExists);
            }else {
                throw new ResourceNotFoundException("Old password no match");
            }
        }else {
            throw new ResourceNotFoundException("Verify failed!");
        }

        return serviceUtils.convertToDataResponse(userRepository.save(userExists), UserResponseDTO.class);
    }

    @Override
    public DataResponse<?> getUserById(String username, User user) {
        User searchUser = userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + username));
        UserResponseDTO userResponseDTO = serviceUtils.convertToResponseDTO(searchUser, UserResponseDTO.class);
        List<ModelCommonFriend> commonFriendList = userRepository.getCommonFriend(username, user.getUsername());
        System.out.println(commonFriendList);
        for(ModelCommonFriend item : commonFriendList){
            userResponseDTO.setCountCommonFriend(item.getCommon());
        }
        userResponseDTO.setCountFriend(
                relationShipRepository.findAllByUserFromOrUserToAndStatus(
                        searchUser,
                        searchUser,
                        RelationEnum.FRIEND.toString()
                ).size()
        );
        return new DataResponse<>(userResponseDTO);
    }

    @Override
    public DataResponse<?> verifyUser(VerifyRequestDTO verifyRequestDTO) {
        User getUser = userRepository.findUserByVerificationCodeAndEmail(verifyRequestDTO.getCode(), verifyRequestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Verify code is incorrect"));
        getUser.setEnable(true);
        getUser.setVerificationCode(generateVerifyCode());

        return serviceUtils.convertToDataResponse(userRepository.save(getUser), UserResponseDTO.class);
    }

    @Override
    public DataResponse<?> sendVerifyCode(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find user " + email));
        try{
            sendVerificationEmail(user, email);
        }catch (Exception e){
            throw new InvalidValueException("Can't not connect to your email");
        }

        DataResponse dataResponse = new DataResponse();
        dataResponse.setMessage("The verify code is sent your email!");
        return dataResponse;
    }

    @Override
    public DataResponse<?> changeAvatar(User user, MultipartFile avatar) {
        try{
            String fileName = imageService.save(avatar);

            String imageUrl = imageService.getImageUrl(fileName);

            user.setAvatar(imageUrl);

            return serviceUtils.convertToDataResponse(userRepository.save(user), UserResponseDTO.class);

        }catch (Exception e){
            throw new InvalidValueException("Can't upload file");
        }
    }

    @Override
    public DataResponse<?> changeEmail(User user, String email) {
        if(email != null){
            if(!user.getEmail().equals(email)){
                Optional<User> userOptional = userRepository.findUserByEmail(email);

                if(userOptional.isPresent()){
                    throw new ResourceNotFoundException("Email has already");
                }
                user.setEmail(email);
            }
        }
        return serviceUtils.convertToDataResponse(userRepository.save(user), UserResponseDTO.class);
    }

    @Override
    public DataResponse<?> forgotPassword(User user, ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        if(user.getVerificationCode().equals(forgotPasswordRequestDTO.getVerifyCode())){
            if(forgotPasswordRequestDTO.getNewPassword() != null){
                user.setPassword(forgotPasswordRequestDTO.getNewPassword());
                encodePassword(user);
            }
        }else{
            throw new InvalidValueException("Verify email failed!");
        }

        return serviceUtils.convertToDataResponse(userRepository.save(user), UserResponseDTO.class);
    }

    @Override
    public ListResponse<?> searchUser(String name, User user) {
        return serviceUtils.convertToListResponse(userRepository.searchByName(name, user.getUsername()), SearchUserResponseDTO.class) ;
    }

    @Override
    public ListResponse<?> getRecommendFriend(User user) {
        try {
            return new ListResponse<>(serviceUtils.recommendFriend(user));
        } catch (TasteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ListResponse<?> searchFriend(User user, String name) {
        return serviceUtils.convertToListResponse( userRepository.searchFriendByName(name, user.getUsername()), SearchUserResponseDTO.class) ;
    }

    @Override
    public DataResponse<?> countFriend(User user) {
        return new DataResponse<>(relationShipRepository.findAllByUserFromOrUserToAndStatus(user, user, RelationEnum.FRIEND.toString()).size());
    }

    @Override
    public ListResponse<?> getListFriendWithPage(User user, Integer size) {
        return serviceUtils.convertToListResponse(
                relationShipRepository.findAllByUserFromOrUserToAndStatus(
                        user,
                        user,
                        RelationEnum.FRIEND.toString(),
                        PageRequest.of(size-1, 10).withSort(Sort.by("createDate").descending())
                ).getContent(),
                FriendResponseDTO.class);
    }

    @Override
    public ListResponse<?> getListPost(User user) {
        return serviceUtils.convertToListResponse(postRepository.findAllByUserAndStatus(user, StatusEnum.ENABLE.toString(), Sort.by("createDate").descending()), PostOfUserResponseDTO.class);
    }

    @Override
    public ListResponse<?> getListUserOfPage(Integer page) {
        List<User> listUser = userRepository.findAll(
                PageRequest.of(page-1, 7).withSort(Sort.by("createDate").descending())
        ).getContent();
        return new ListResponse<>(listUser);
    }

    @Override
    public DataResponse<?> disableUser(String username) {
        User user = userRepository.findById(username).orElseThrow(
                () -> new ResourceNotFoundException("Can't find user " + username)
        );

        user.setEnable(false);

        return serviceUtils.convertToDataResponse(userRepository.save(user), UserResponseDTO.class);
    }

    @Override
    public DataResponse<?> enableUser(String username) {
        User user = userRepository.findById(username).orElseThrow(
                () -> new ResourceNotFoundException("Can't find user " + username)
        );

        user.setEnable(true);

        return serviceUtils.convertToDataResponse(userRepository.save(user), UserResponseDTO.class);
    }

    @Override
    public DataResponse<?> getStatisticData() {
        StatisticDataResponseDTO statisticDataResponseDTO = new StatisticDataResponseDTO();
        statisticDataResponseDTO.setCountUser(userRepository.count());
        statisticDataResponseDTO.setCountPost(postRepository.count());
        statisticDataResponseDTO.setCountComment(commentRepository.count());
        statisticDataResponseDTO.setCountUserOfMonth(userRepository.countUserEachMonth());
        statisticDataResponseDTO.setCountPostOfMonth(userRepository.countPostEachMonth());
        statisticDataResponseDTO.setCountCommentOfMonth(userRepository.countCommentEachMonth());
        return new DataResponse<>(statisticDataResponseDTO);
    }

    @Override
    public ListResponse<?> uploadFileOfMessage(MultipartFile[] files) {
        List<String> listUrl = new ArrayList<>();

        for(MultipartFile item : files){
            String url = uploadFile(item);
            if(url != null){
                listUrl.add(url);
            }
        }
        return new ListResponse<>(listUrl);
    }

    String uploadFile(MultipartFile file){
        try{
            //read file in files
            if(file != null){
                //Upload file
                String fileName = imageService.save(file);

                return imageService.getImageUrl(fileName);
            }

            return  null;
        }catch (Exception e){
            throw new InvalidValueException("Can't upload file : " + e.getMessage());
        }
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private String generateVerifyCode(){
        Random random = new Random();
        int number1 = 100 + random.nextInt(999);
        int number2 = 100 + random.nextInt(999);
        int number3 = 100 + random.nextInt(999);

        return number1 + "-" + number2 + "-" + number3;
    }

    private void sendVerificationEmail(User user, String siteUrl)
            throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "Social Media App";
//        String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();
        String mailContent = "<p>Dear " + user.getName() + ",<p><br>"
                + "We send to verify code of your registration email:<br>"
                + "<h3> Verify code: \"" + user.getVerificationCode() + "\"</h3>"
                + "Thank you,<br>" + "Social Media App";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("laihung1412@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
