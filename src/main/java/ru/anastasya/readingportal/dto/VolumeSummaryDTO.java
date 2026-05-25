package ru.anastasya.readingportal.dto;

public record VolumeSummaryDTO(
        Long id,
        String title,
        int volumeMainNumber,
        int volumeSubNumber) {
}
