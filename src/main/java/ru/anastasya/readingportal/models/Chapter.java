package ru.anastasya.readingportal.models;

public class Chapter {

    private Long id;
    private String title;
    private String content;
    private int chapterMainNumber;
    private int chapterSubNumber;
    private Long volumeId;

    public Chapter() {
    }

    public Chapter(String title, int chapterMainNumber, int chapterSubNumber, Long volumeId) {
        this.id = null;
        this.title = title;
        this.content = null;
        this.chapterMainNumber = chapterMainNumber;
        this.chapterSubNumber = chapterSubNumber;
        this.volumeId = volumeId;
    }

    public Chapter(String title, String content, int chapterMainNumber, int chapterSubNumber, Long volumeId) {
        this.id = null;
        this.title = title;
        this.content = content;
        this.chapterMainNumber = chapterMainNumber;
        this.chapterSubNumber = chapterSubNumber;
        this.volumeId = volumeId;
    }

    public Chapter(Long id, String title, int chapterMainNumber, int chapterSubNumber, Long volumeId) {
        this.id = id;
        this.title = title;
        this.content = null;
        this.chapterMainNumber = chapterMainNumber;
        this.chapterSubNumber = chapterSubNumber;
        this.volumeId = volumeId;
    }

    public Chapter(Long id, String title, String content, int chapterMainNumber, int chapterSubNumber, Long volumeId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.chapterMainNumber = chapterMainNumber;
        this.chapterSubNumber = chapterSubNumber;
        this.volumeId = volumeId;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", chapter_main_number=" + chapterMainNumber +
                ", chapter_sub_number=" + chapterSubNumber +
                ", volume_id=" + volumeId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChapterMainNumber() {
        return chapterMainNumber;
    }

    public void setChapterMainNumber(int chapterMainNumber) {
        this.chapterMainNumber = chapterMainNumber;
    }

    public int getChapterSubNumber() {
        return chapterSubNumber;
    }

    public void setChapterSubNumber(int chapterSubNumber) {
        this.chapterSubNumber = chapterSubNumber;
    }

    public Long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Long volumeId) {
        this.volumeId = volumeId;
    }
}
