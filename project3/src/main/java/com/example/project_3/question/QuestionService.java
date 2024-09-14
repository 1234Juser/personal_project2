package com.example.project_3.question;

import com.example.project_3.user.SiteUser;
import java.util.Optional;
import com.example.project_3.DataNotFoundException;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;


@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    } //질문목록을 보여주는 메서드, 페이징 기능추가
    
    public Question getQuestion(Integer id) {  
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found"); //상세페이지 서비스
        }
    }
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject); //메소드호출, 필드이름(첫글자는대문자), 매개변수타입: 필드타입
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    } // 제목 내용을 입력받아 질문으로 저장하는 메서드
    
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
}