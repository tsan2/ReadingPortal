package ru.anastasya.readingportal.dto;

import java.util.List;

public class PageResponseDTO<T> {

    private List<T> data;
    private long totalCount;
    private int size;
    private int page;

    public PageResponseDTO(List<T> data, long totalCount, int size, int page) {
        this.data = data;
        this.totalCount = totalCount;
        this.size = size;
        this.page = page;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
