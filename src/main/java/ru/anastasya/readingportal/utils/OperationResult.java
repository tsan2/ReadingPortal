package ru.anastasya.readingportal.utils;

public class OperationResult {

    private final Long idCreated;
    private final boolean success;
    private final String warningMessage;

    public OperationResult(boolean success){
        this.idCreated = null;
        this.success = success;
        this.warningMessage = null;
    }

    public OperationResult(Long idCreated, boolean success, String warningMessage){
        this.idCreated = idCreated;
        this.success = success;
        this.warningMessage = warningMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public Long getIdCreated() {
        return idCreated;
    }
}
