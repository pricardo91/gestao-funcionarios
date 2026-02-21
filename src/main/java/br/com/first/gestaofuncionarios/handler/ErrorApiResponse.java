package br.com.first.gestaofuncionarios.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorApiResponse {

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private Integer status;
    private String error;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String path;
}
