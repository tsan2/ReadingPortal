package ru.anastasya.readingportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VolumeResponseDTO{
    private Long id;
    private String title;
    private int volumeMainNumber;
    private int volumeSubNumber;
    private BookSummaryDTO book;
    private int version;
    private String warningMessage;

}
