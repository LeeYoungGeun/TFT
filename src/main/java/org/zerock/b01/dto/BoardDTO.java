package org.zerock.b01.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;


}
