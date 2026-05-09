package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Volume {

    @Id
    @SequenceGenerator(name = "volume_seq", sequenceName = "volume_sequence", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "volume_seq")
    private Long id;
    private String title;
    @Column(name = "volume_main_number")
    private int volumeMainNumber;
    @Column(name = "volume_sub_number")
    private int volumeSubNumber;
    @Column(name = "is_default")
    private boolean isDefault;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    @Version
    private Integer version;

    public Volume(String title, int volumeMainNumber, int volumeSubNumber, boolean isDefault) {
        this.title = title;
        this.volumeMainNumber = volumeMainNumber;
        this.volumeSubNumber = volumeSubNumber;
        this.isDefault = isDefault;
    }
}
