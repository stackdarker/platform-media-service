package com.stackdarker.platform.media.exception;

import com.stackdarker.platform.media.api.error.ErrorItem;
import com.stackdarker.platform.media.api.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MediaObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMediaObjectNotFound(
            MediaObjectNotFoundException ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.NOT_FOUND,
                "MEDIA_NOT_FOUND",
                ex.getMessage(),
                request,
                List.of(new ErrorItem("NOT_FOUND", ex.getMessage()))
        );
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidContentType(
            InvalidContentTypeException ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "INVALID_CONTENT_TYPE",
                ex.getMessage(),
                request,
                List.of(new ErrorItem("INVALID_CONTENT_TYPE", ex.getMessage()))
        );
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleFileSizeLimit(
            FileSizeLimitExceededException ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "FILE_SIZE_EXCEEDED",
                ex.getMessage(),
                request,
                List.of(new ErrorItem("FILE_SIZE_EXCEEDED", ex.getMessage()))
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ErrorItem> items = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            items.add(new ErrorItem(
                    "VALIDATION_ERROR",
                    fe.getDefaultMessage(),
                    fe.getField(),
                    Map.of("rejectedValue", safeRejectedValue(fe.getRejectedValue()))
            ));
        }

        return build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "VALIDATION_FAILED",
                "One or more fields are invalid.",
                request,
                items
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "An unexpected error occurred.",
                request,
                List.of(new ErrorItem("INTERNAL_ERROR", "Unexpected error."))
        );
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String code,
            String message,
            HttpServletRequest request,
            List<ErrorItem> items
    ) {
        ErrorResponse body = new ErrorResponse();
        body.setStatus(status.value());
        body.setError(status.getReasonPhrase());
        body.setCode(code);
        body.setMessage(message);
        body.setPath(request.getRequestURI());
        body.setRequestId(getRequestId(request));
        body.setErrors(items);

        return ResponseEntity.status(status).body(body);
    }

    private String getRequestId(HttpServletRequest request) {
        String rid = request.getHeader("X-Request-Id");
        if (rid != null && !rid.isBlank()) return rid;
        Object attr = request.getAttribute("requestId");
        return attr == null ? null : attr.toString();
    }

    private Object safeRejectedValue(Object rejected) {
        if (rejected == null) return null;
        String s = rejected.toString();
        if (s.length() > 200) return s.substring(0, 200) + "...";
        return s;
    }
}
