package ru.anastasya.readingportal.utils;

public class OperationResult<T> {

    private final T objectCreated;
    private final boolean success;
    private final String warningMessage;

    public OperationResult(boolean success){
        this.objectCreated = null;
        this.success = success;
        this.warningMessage = null;
    }

    public OperationResult(T objectCreated, boolean success, String warningMessage){
        this.objectCreated = objectCreated;
        this.success = success;
        this.warningMessage = warningMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public T getObjectCreated() {
        return objectCreated;
    }
}
