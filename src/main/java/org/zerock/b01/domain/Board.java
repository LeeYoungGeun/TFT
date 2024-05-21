package org.zerock.b01.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String writer;

    @Column(length = 2000, nullable = false)
    private String content;

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String filename){
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(filename)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void ClearImages(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }

}
