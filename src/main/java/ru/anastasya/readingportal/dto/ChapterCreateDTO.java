package ru.anastasya.readingportal.dto;

public class ChapterCreateDTO {

    private Long bookId;
    private String title;
    private String content;
    private int chapterMainNumber;
    private int chapterSubNumber;
    private Long volumeId;

    public ChapterCreateDTO(Long bookId, String title, String content, int chapterMainNumber, int chapterSubNumber) {
        this.bookId = bookId;
        this.title = title;
        this.content = content;
        this.chapterMainNumber = chapterMainNumber;
        this.chapterSubNumber = chapterSubNumber;
        this.volumeId = null;
    }

    public ChapterCreateDTO(Long bookId, String title, String content, int chapterMainNumber, int chapterSubNumber, Long volumeId) {
        this.bookId = bookId;
        this.title = title;
        this.content = content;
        this.chapterMainNumber = chapterMainNumber;
        this.chapterSubNumber = chapterSubNumber;
        this.volumeId = volumeId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

