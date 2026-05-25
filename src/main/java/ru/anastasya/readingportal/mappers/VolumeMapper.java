package ru.anastasya.readingportal.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.anastasya.readingportal.dto.VolumeResponseDTO;
import ru.anastasya.readingportal.dto.VolumeSummaryDTO;
import ru.anastasya.readingportal.models.Volume;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VolumeMapper {

    VolumeResponseDTO toVolumeResponseDTO(Volume volume);
    List<VolumeSummaryDTO> toVolumeSummaryDTOs(List<Volume> volumes);
}
