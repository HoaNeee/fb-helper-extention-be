package com.hoane.fbhelper.fbhelperextentionbe.entity;

import com.hoane.fbhelper.fbhelperextentionbe.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "data_group_posts")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGroupPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String title_match;
    @Column(columnDefinition = "NVARCHAR(255)") // Use NVARCHAR for Unicode support
    private String name;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT") //TEXT will be used to store large text data
    private List<String> contents;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> images;

    private Integer priority;

    private Integer from_member;

    private Integer to_member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public DataGroupPost(String title_match, String name, List<String> contents, List<String> images, Integer priority, Integer from_member, Integer to_member) {
        this.title_match = title_match;
        this.name = name;
        this.contents = contents;
        this.images = images;
        this.priority = priority;
        this.from_member = from_member;
        this.to_member = to_member;
    }
}
