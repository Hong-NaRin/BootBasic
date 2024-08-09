package com.simple.basic.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemoVO {

    @Size(min=5, message = "5글자 이상 내용을 작성해주세요")
    private String memo;
    @Pattern(regexp = "[0-9]{3}-[0-9]{4}-[0-9]{4}", message = "전화번호 형식은 000-0000-0000입니다")
    private String phone;
    @Pattern(regexp = "[0-9]{4}", message="비밀번호는 숫자 4자리만 입력 가능합니다")
    private String pw;
    private String secret = "n";
}
