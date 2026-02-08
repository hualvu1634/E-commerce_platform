package e_commerce.project.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import e_commerce.project.exception.ErrorField;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ApiResponse {
    private LocalDateTime timestamp;
    private String message;
    private Integer code;     
    private List<ErrorField> details; 
}