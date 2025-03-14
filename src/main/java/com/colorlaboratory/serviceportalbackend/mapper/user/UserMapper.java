package com.colorlaboratory.serviceportalbackend.mapper.user;

import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);
}
