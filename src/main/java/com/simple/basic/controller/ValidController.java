package com.simple.basic.controller;

import com.simple.basic.command.MemberVO;
import com.simple.basic.command.ValidVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/valid")
public class ValidController {

    @GetMapping("/view")
    public String view(Model model) {
        // view.html에서 ${vo.name}을 받아야 하는데, 이 화면에 진입할 때 vo가 없으면 에러가 난다 -> ${null.name}이 되는 것)
        // vo를 선언해줘야 다시 valid/view의 화면에서 오류가 나지 않음
        model.addAttribute("vo", new ValidVO());
        return "valid/view";
    }

    @PostMapping("/actionForm")
    public String actionForm(@Valid @ModelAttribute("vo") ValidVO vo, BindingResult binding ) {
        // @ModelAttribute("vo")는 유효성 검사를 하고 그 값을 가지고 화면에 넘긴다
        // @valid는 유효성 검사를 하겠다는 뜻
        // 만약 유효성 검사에 통과하지 못하면, 통과하지 못한 멤버변수 내역이 BindingResult에 저장됨

        if(binding.hasErrors()) { // 내역이 있으면 true, 없으면 false

//            System.out.println("유효성 검사 실패");
//            List<FieldError> list = binding.getFieldErrors(); // 유효성 검사에 실패한 목록
//            for(FieldError error : list) {
//                System.out.println(error.getField()); // 유효성 검사에 실패한 필드명 확인 가능
//                System.out.println(error.getDefaultMessage()); // 유효성 검사에 실패한 메세지 값 확인 가능
//            }
            return "valid/view"; // 다시 원래 화면으로
        }

        //처리
        System.out.println(vo.toString());
        return "valid/result";
    }

    @GetMapping("/quiz01")
    public String quiz01(Model model) {
        model.addAttribute("vo", new MemberVO());
        return "valid/quiz01";
    }

    @PostMapping("/quizForm")
    public String quizForm(@Valid @ModelAttribute("vo") MemberVO vo, BindingResult binding ) {

        if(binding.hasErrors()) { // 내역이 있으면 true, 없으면 false
            return "valid/quiz01";
        }
        return "valid/quiz01_result";
    }

}

