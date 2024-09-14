package com.example.project_3.question;

import java.util.Set;
import jakarta.persistence.ManyToMany;
import java.time.LocalDateTime;
import jakarta.persistence.ManyToOne;
import com.example.project_3.user.SiteUser; 
import java.util.List;

import com.example.project_3.answer.Answer;

import jakarta.persistence.CascadeType; 
import jakarta.persistence.Column; 
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.OneToMany; 

import lombok.Getter; 
import lombok.Setter; 

@Getter 
@Setter 
@Entity 
public class Question { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id; 

    @Column(length = 200) 
    private String subject; 

    @Column(columnDefinition = "TEXT") 
    private String content; 

    private LocalDateTime createDate; 

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) 
    private List<Answer> answerList; 
    
    @ManyToOne
    private SiteUser author;
    
    private LocalDateTime modifyDate;
    
    @ManyToMany
    Set<SiteUser> voter; //중복되지않고 순서가없는 set
}