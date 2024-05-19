package org.zerock.b01.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;

    @NotEmpty
    @Size(min = 5, max = 100)
    private String title;
    @NotEmpty
    private String writer;
    @NotEmpty

    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;


}
