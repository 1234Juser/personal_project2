package com.example.project_3.question;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import com.example.project_3.user.SiteUser;
import com.example.project_3.user.UserService; //다른패키지에있는 멤버변수(필드)
import com.example.project_3.answer.AnswerForm;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize; //시큐리티 로그인한상태에서만 실행되는것
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	private final UserService userService;
	private final QuestionService questionService;

	@GetMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
        
    }
	@GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
    	 Question question = this.questionService.getQuestion(id);
         model.addAttribute("question", question);
        return "question_detail"; //상세페이지
    }
	@PreAuthorize("isAuthenticated()")
	 @GetMapping("/create") //질문등록 입력
	    public String questionCreate(QuestionForm questionForm) {
	        return "question_form";
	    }
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create") //질문등록처리
	 public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,
			 Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        } // 오류가 있는경우 등록화면으로 되돌아감. @valid뒤에 항상 BindingResult있어야함.
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser); //질문저장
        return "redirect:/question/list";
    }
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }
	 @PreAuthorize("isAuthenticated()")
	  @PostMapping("/modify/{id}")
	    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
	            Principal principal, @PathVariable("id") Integer id) {
	        if (bindingResult.hasErrors()) {
	            return "question_form";
	        }
	        Question question = this.questionService.getQuestion(id);
	        if (!question.getAuthor().getUsername().equals(principal.getName())) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
	        }
	        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
	        return String.format("redirect:/question/detail/%s", id);
	    }
	 @PreAuthorize("isAuthenticated()")
	    @GetMapping("/delete/{id}")
	    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
	        Question question = this.questionService.getQuestion(id);
	        if (!question.getAuthor().getUsername().equals(principal.getName())) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
	        }
	        this.questionService.delete(question);
	        return "redirect:/";
	    }
	 
	 @PreAuthorize("isAuthenticated()")
	    @GetMapping("/vote/{id}")
	    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
	        Question question = this.questionService.getQuestion(id);
	        SiteUser siteUser = this.userService.getUser(principal.getName());
	        this.questionService.vote(question, siteUser);
	        return String.format("redirect:/question/detail/%s", id);
	    }
}