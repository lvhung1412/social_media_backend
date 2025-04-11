package com.example.SocialMedia.service.impl;

import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.LoginResponseDTO;
import com.example.SocialMedia.dto.response.UserResponseDTO;
import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.entity.enums.RoleEnum;
import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.service.inter.LoginService;
import com.example.SocialMedia.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.Optional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenUtil jwtUtil;

    @Override
    public DataResponse<?> authenticateWithUsernamePassword(String username, String password, RoleEnum requestedRole) throws LoginException {
        //check user
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = (User) auth.getPrincipal();
        //create token
        String token = generateToken(user.getUsername());

        String r = user.getRole().getName();
        //check role
        if (requestedRole.equals(RoleEnum.CUSTOMER) && requestedRole.toString().equals(r)) { // buyer
            return new DataResponse<>(new LoginResponseDTO<>(
                    token,
                    mapper.map(user, UserResponseDTO.class))
            );
        }
        else if (requestedRole.equals(RoleEnum.ADMIN) && !r.equals(RoleEnum.CUSTOMER.toString())) {
            return new DataResponse<>(new LoginResponseDTO<>(
                    token,
                    mapper.map(user, UserResponseDTO.class))
            );
        }
        else {
            throw new LoginException("Username or password is invalid for this system");
        }
    }

    public String generateToken(String username) {
        Optional<User> user =userRepository.findUserByUsername(username);
        return user.map(value -> jwtUtil.generateToken(value)).orElse(null);

    }

}
