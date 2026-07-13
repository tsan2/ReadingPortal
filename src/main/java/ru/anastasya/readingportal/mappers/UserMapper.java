package ru.anastasya.readingportal.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.anastasya.readingportal.dto.ProfileDTO;
import ru.anastasya.readingportal.dto.UserPublicInfoDTO;
import ru.anastasya.readingportal.dto.UserRegisterDTO;
import ru.anastasya.readingportal.dto.UserSummaryDTO;
import ru.anastasya.readingportal.models.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User fromUserRegisterDTO(UserRegisterDTO userRegisterDTO);
    ProfileDTO toProfileDTO(User user);
    UserSummaryDTO toUserSummaryDTO(User user);
    UserPublicInfoDTO toUserPublicInfoDTO(User user);

}
