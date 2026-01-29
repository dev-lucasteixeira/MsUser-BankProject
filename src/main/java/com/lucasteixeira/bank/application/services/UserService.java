package com.lucasteixeira.bank.application.services;

import com.lucasteixeira.bank.application.dtos.UserDTO;
import com.lucasteixeira.bank.application.enums.AccessEnum;
import com.lucasteixeira.bank.application.enums.ActivityEnum;
import com.lucasteixeira.bank.domain.entities.UserEntity;
import com.lucasteixeira.bank.infratructure.exceptions.ConflitException;
import com.lucasteixeira.bank.application.mappers.UserMapper;
import com.lucasteixeira.bank.domain.repositories.UserRepository;
import com.lucasteixeira.bank.infratructure.exceptions.ResourceNotFoundException;
import com.lucasteixeira.bank.infratructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
        userDTO.setAccess(AccessEnum.USER);
        userDTO.setActive(ActivityEnum.ACTIVE);
        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity =userRepository.save(userEntity);
        return userMapper.toDTO(userEntity);
    }

    public String loginUser(UserDTO userDTO){
        UserEntity user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }
        return "Bearer "+ jwtUtil.generateToken(userDTO.getEmail());
    }

    public UserDTO getUserInfoByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(message));

        return userMapper.toDTO(user);
    }

    @Transactional
    public String deleteAccount(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("Email não cadastrado: " + email);
        }

        userRepository.deleteByEmail(email);

        return "Conta deletada com sucesso: " + email;
    }


    public UserDTO updateUser(UserDTO userDTO, String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(message));

        if (userEntity.getName().equals(userDTO.getName())) {
            throw new ConflitException("Nome é igual ao atual");
        }

        if (Objects.equals(userEntity.getAge(), userDTO.getAge())) {
            throw new ConflitException("Idade é igual a atual");
        }

        if (userEntity.getMaritalStatus().equals(userDTO.getMaritalStatus())) {
            throw new ConflitException("Marital Status é igual ao atual");
        }

        userEntity.setName(userDTO.getName());
        userEntity.setAge(userDTO.getAge());
        userEntity.setMaritalStatus(userDTO.getMaritalStatus());

        return userMapper.toDTO(userRepository.save(userEntity));
    }

    public UserDTO updatePassword(UserDTO userDTO, String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(message));

        if (userEntity.getPassword().equals(userDTO.getPassword())) {
            throw new ConflitException("Senha está igual a atual");
        }

        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userMapper.toDTO(userRepository.save(userEntity));
    }

    public UserDTO updateActivity(UserDTO userDTO, String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(message));

        if (userEntity.getActive() == userDTO.getActive()) {
            throw new ConflitException("O usuário já está com este status!");
        }

        userEntity.setActive(userDTO.getActive());
        return userMapper.toDTO(userRepository.save(userEntity));
    }

    public UserDTO updateAccountPartial(UserDTO userDTO, String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(message));

        if (userDTO.getName() != null && userDTO.getName().equals(userEntity.getName())) {
            throw new ConflitException("Nome é igual ao atual");
        }

        if (userDTO.getAge() != null && Objects.equals(userDTO.getAge(), userEntity.getAge())) {
            throw new ConflitException("Idade é igual a atual");
        }

        if (userDTO.getMaritalStatus() != null && userDTO.getMaritalStatus().equals(userEntity.getMaritalStatus())) {
            throw new ConflitException("Marital Status é igual ao atual");
        }

        userMapper.updateUsuario(userDTO, userEntity);

        return userMapper.toDTO(userRepository.save(userEntity));
    }

    private final String message = "User not found";

}