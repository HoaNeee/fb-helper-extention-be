package com.hoane.fbhelper.fbhelperextentionbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment_posts")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String contents;
    private Integer max_comment_per_post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
