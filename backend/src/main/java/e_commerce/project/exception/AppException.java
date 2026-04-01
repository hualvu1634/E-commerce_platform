package e_commerce.project.exception;

import e_commerce.project.enumerate.ErrorCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); 
        this.errorCode = errorCode;    
    }
}