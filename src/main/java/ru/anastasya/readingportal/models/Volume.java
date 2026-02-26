package ru.anastasya.readingportal.models;

public class Volume {

    private Long id;
    private String title;
    private int volumeMainNumber;
    private int volumeSubNumber;
    private Long bookId;
    private boolean is_default;

    public Volume() {

    }

    public Volume(String title, int volumeMainNumber, int volumeSubNumber, Long bookId) {
        this.id = null;
        this.title = title;
        this.volumeMainNumber = volumeMainNumber;
        this.volumeSubNumber = volumeSubNumber;
        this.bookId = bookId;
    }

    public Volume(String title, int volumeMainNumber, int volumeSubNumber, Long bookId, boolean is_default) {
        this.id = null;
        this.title = title;
        this.volumeMainNumber = volumeMainNumber;
        this.volumeSubNumber = volumeSubNumber;
        this.bookId = bookId;
        this.is_default = is_default;
    }

    public Volume(Long id, String title, int volumeMainNumber, int volumeSubNumber, Long bookId, boolean is_default) {
        this.id = id;
        this.title = title;
        this.volumeMainNumber = volumeMainNumber;
        this.volumeSubNumber = volumeSubNumber;
        this.bookId = bookId;
        this.is_default = is_default;
    }

    @Override
    public String toString() {
        return "Volume{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", volume_main_number=" + volumeMainNumber +
                ", volume_sub_number=" + volumeSubNumber +
                ", book_id=" + bookId +
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

    public int getVolumeMainNumber() {
        return volumeMainNumber;
    }

    public void setVolumeMainNumber(int volumeMainNumber) {
        this.volumeMainNumber = volumeMainNumber;
    }

    public int getVolumeSubNumber() {
        return volumeSubNumber;
    }

    public void setVolumeSubNumber(int volumeSubNumber) {
        this.volumeSubNumber = volumeSubNumber;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }
}
