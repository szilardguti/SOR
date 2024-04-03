package hu.unideb.inf.segitsegosszesitorendszer.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse {
    private String exceptionMessage;
}
