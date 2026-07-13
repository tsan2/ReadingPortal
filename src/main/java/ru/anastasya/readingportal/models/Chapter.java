package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
@Table(name = "chapters")
@Entity
public class Chapter {


    @Id
    @SequenceGenerator(name = "chapter_seq", sequenceName = "chapter_sequence", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_seq")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
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

    public Chapter(String title, int chapterMainNumber, int chapterSubNumber){
        this.title = title;
        this.chapterMainNumber = chapterMainNumber;
        this.chapterSubNumber = chapterSubNumber;
    }
}
