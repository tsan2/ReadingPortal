package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "chapters")
@Entity
public class Chapter {


    @Id
    @SequenceGenerator(name = "chapter_seq", sequenceName = "chapter_sequence", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_seq")
    private Long id;
    @Column(nullable = false)
    private String title;
    private String content;
    @Column(name = "chapter_main_number", nullable = false)
    private int chapterMainNumber;
    @Column(name = "chapter_sub_number")
    private int chapterSubNumber;
    @Version
    private Integer version;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volume_id")
    private Volume volume;
}
