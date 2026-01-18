package com.lucasteixeira.bank.mappers;


import com.lucasteixeira.bank.dtos.UserDTO;
import com.lucasteixeira.bank.entities.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    UserDTO toDTO(UserEntity userEntity);

    UserEntity toEntity(UserDTO userDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateUsuario(UserDTO userDTO, @MappingTarget UserEntity userEntity);

}
