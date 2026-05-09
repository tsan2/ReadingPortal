package ru.anastasya.readingportal.dto;

public record ChangeVolumeNumberDTO(Long id, double volumeNumber, Long currentUserId, Integer version) {
}
