package ru.anastasya.readingportal.utils;

public class OperationResult {

    private final boolean success;
    private final String warningMessage;

    public OperationResult(boolean success){
        this.success = success;
        this.warningMessage = null;
    }

    public OperationResult(boolean success, String warningMessage){
        this.success = success;
        this.warningMessage = warningMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getWarningMessage() {
        return warningMessage;
    }
}
