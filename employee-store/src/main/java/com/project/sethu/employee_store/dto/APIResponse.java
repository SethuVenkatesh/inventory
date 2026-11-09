package com.project.sethu.employee_store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Map<String, String> fieldErrors;
    private int status;
    private LocalDateTime timestamp;

    // ── Private constructor ──────────────────────────────
    private APIResponse(boolean success, String message, T data,
                        int status, Map<String, String> fieldErrors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
        this.fieldErrors = fieldErrors;
        this.timestamp = LocalDateTime.now();
    }

    // ── Success factories ────────────────────────────────
    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(true, "Success", data, 200, null);
    }

    public static <T> APIResponse<T> success(String message, T data) {
        return new APIResponse<>(true, message, data, 200, null);
    }

    public static <T> APIResponse<T> created(T data) {
        return new APIResponse<>(true, "Created successfully", data, 201, null);
    }

    public static <T> APIResponse<T> deleted() {
        return new APIResponse<>(true, "Deleted successfully", null, 200, null);
    }

    // ── Error factories ──────────────────────────────────
    public static <T> APIResponse<T> error(int status, String message) {
        return new APIResponse<>(false, message, null, status, null);
    }

    public static <T> APIResponse<T> validationError(Map<String, String> fieldErrors) {
        return new APIResponse<>(false, "Validation failed", null, 400, fieldErrors);
    }

    // ── Getters ──────────────────────────────────────────
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public Map<String, String> getFieldErrors() { return fieldErrors; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
