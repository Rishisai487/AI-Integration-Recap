package com.aiintegration.aiintegrationrecap.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;
    @Column(length = 5000)
    private String message;
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
}
