package com.lucasteixeira.bank.services;

import com.lucasteixeira.bank.dtos.UserDTO;
import com.lucasteixeira.bank.entities.UserEntity;
import com.lucasteixeira.bank.exceptions.ConflitException;
import com.lucasteixeira.bank.mappers.UserMapper;
import com.lucasteixeira.bank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {

        String cpfLimpo = userDTO.getCpf().replaceAll("\\D", "");
        userDTO.setCpf(cpfLimpo);

        if (userRepository.existsByCpf(userDTO.getCpf())) {
            throw new ConflitException("Cpf já cadastrado: " + userDTO.getCpf());
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ConflitException("Email já cadastrado: " + userDTO.getEmail());
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity =userRepository.save(userEntity);
        return userMapper.toDTO(userEntity);
    }
}