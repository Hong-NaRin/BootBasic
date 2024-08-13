package com.simple.basic.controller;

import com.simple.basic.command.MemoVO;
import com.simple.basic.command.TestVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

// 구글 부메랑-soap & Rest client 사용

// RestController는 modelAttribute의 일반 컨트롤러와 서버가 다름 (일반 컨트롤러는 내 프로젝트에 포함된 파일에다 리턴)
// 일반 컨트롤러는 RestController처럼 바로 받을 수가 없음 (구글 부메랑)

/* --- @ResponseBody와 @RestController 차이점 --- */
// @ResponseBody - 컨트롤러 메서드의 반환 값을 뷰(View)로 해석하지 않고, HTTP 응답 본문에 그대로 출력 (특정 메서드에만 적용하고 싶을 때 사용)
// @RestController - 클래스 전체(모든 메서드)에 @ResponseBody를 적용하는 역할

@RestController // Controller + ResponseBody 합성어 (컨트롤러에서 응답을 클라이언트 요청이 들어온 곳으로 바꿈)
public class RestBasicController {

    @GetMapping("/hello")
    public String hello() {
        return "이게 뭐지?"; // 요청을 보낸 곳으로 응답하게 된다
    }

    @GetMapping("/hello2")
    public String[] hello2() {
        return new String[] {"홍", "길", "동"}; // 요청을 보낸 곳으로 응답하게 된다
    }

    /* -----------------------------------------------------------*/
    // get방식 요청받기 - 일반 컨트롤러에서 받는 형식과 똑같은 방법으로 가능함
    // http://localhost:8181/getData?num=1&name=홍길동 -> num과 name 가져옴

    // 1st
//    @GetMapping("/getData")
//    public String getData(TestVO vo) {
//        System.out.println(vo.toString());
//        return "getData";
//    }
    // 2nd
    @GetMapping("/getData")
    // http://localhost:8181/getData?num=1&name=홍길동처럼 ?로 넣을 때는 @RequestParam을 사용해서 HTTP 요청 파라미터 바인딩 -> 메서드의 num과 name 매개변수에 각각 바인딩
    public String getData(@RequestParam("num") int num,
                          @RequestParam("name") String name) {
        System.out.println(num + ", " + name);
        return "getData";
    }

    /* -----------------------------------------------------------*/
    // PathVariable 방식
    // http://localhost:8181/getData2/1/홍길동
    // 주소에 / / 를 넣을 때는 @PathVariable을 사용하며, 자신이 편한 주소 스타일에 따라 @RequestParam이나 @PathVariable 둘 중에 하나를 사용하면 됨
    @GetMapping("/getData2/{num}/{name}")
    public String getData2(@PathVariable("num") int num,
                           @PathVariable("name") String name) {
        System.out.println(num + ", " + name);
        return "success";
    }

    /* -----------------------------------------------------------*/
    // 반환을 JSON형식으로 하려면 Map타입이나 VO를 쓰면 된다 (list, 배열도 됨)
    // Jackson-databind 라이브러리가 필요함 (스프링부트에 기본 포함됨)

    // 1st
//    @GetMapping("/returnData")
//    public TestVO returnData() {
//        return new TestVO(1, "서버에서 반환", 20, "서울시");
//    }
    // 2nd
    @GetMapping("/returnData")
    public Map<String, Object> returnData() {
        Map<String, Object> map = new HashMap<>();
        map.put("num", 1);
        map.put("name", "홍길동");
        map.put("arr", Arrays.asList("a", "b", "c"));
        return map;
    }

    /* -----------------------------------------------------------*/
    // post 방식 - 소비자(사용자)와 제공자(서버) 이 둘의 데이터를 주고 받는 규약이 정확하게 지켜져야 함

    // form형식으로 데이터 전송할 때 - 소비자 데이터를 Form 형식으로 반드시 만들어서 보내야 함
    // http://localhost:8181/getForm
    @PostMapping("/getForm")
    public String getForm(TestVO vo) {
        System.out.println(vo.toString());
        return "success";
    }

    /* -----------------------------------------------------------*/
    // JSON 형식으로 데이터 전송할 때
    // @RequestBody - 클라이언트에 JSON 데이터를 자바 오브젝트로 변형해서 맵핑
    // @RequestBody 적으면 json 데이터, 없으면 기본 form 데이터
    // json 데이터는 웬만하면 map 타입 말고 vo 타입 -> map은 사용자측에서 잘못된 데이터 넣을 가능성이 있음

    // {"name" : "홍길동", "age" : 20, "addr" : "서울시"}
    // 1st
//    @PostMapping("/getJSON")
//    public String getJSON(@RequestBody TestVO vo) { // JSON과 Object 타입은 다르기 때문에 @RequestBody를 붙여야 매핑돼서 값이 출력됨
//        System.out.println(vo.toString());
//        return "success";
//    }

    // 2nd
    @PostMapping("/getJSON")
    public String getJSON( @RequestBody Map<String, Object> map) {
        System.out.println(map.toString());
        return "success";
    }

    /* -----------------------------------------------------------*/
    // @PutMapping(수정), @DeleteMapping (삭제) - Post방식과 거의 유사함

    // consumer - 반드시 이 타입으로 보내라
    // produces - 서버에서 제공하는 타입으로 보내준다 (내가 이 타입으로 줄게)
    // 기본값은 - "application/json"
    @PostMapping(value="/getResult", produces = "text/html", consumes = "text/plain") // 내가 html타입으로 주면 반드시 plain타입으로 보내라
    public String getResult(@RequestBody String str) {
        System.out.println(str);
        return "<h3>문자열</h3>";
    }

    /* -----------------------------------------------------------*/
    // 응답문서 명확하게 작성하기 ResponseEntity<데이터타입>
    @CrossOrigin({"http://127.0.0.1:5500", "http://localhost:5500"}) // 데이터 쉐어링 승인
    // @CrossOrigin("*") // 모든 서버에 대한 요청을 승인함 (위험할 수 있음)
    @PostMapping("/getEntity")
    public ResponseEntity<TestVO> getEntity(@RequestBody TestVO v) { // @RequestBody TestVO v 받아서 <TestVO> 반환
        System.out.println("받은 데이터:" + v.toString());
        TestVO vo = new TestVO(1, "홍길동", 20, "서울시");

        // 1st
        // ResponseEntity entity = new ResponseEntity(데이터, 상태값); -> 데이터와 상대값을 둘 다 보내거나
        // ResponseEntity entity = new ResponseEntity(HttpStatus.BAD_REQUEST); -> 상태값만 보낼 수도 있음
//        ResponseEntity entity = new ResponseEntity(vo, HttpStatus.BAD_REQUEST); // HttpStatus.ok를 제외하고는 전부 에러임
//        return entity;

        // 2nd
        HttpHeaders header = new HttpHeaders();
        header.add("Autorization", "Bearer JSON WEB TOKEN~"); // 키, 값
        header.add("Content-Type", "application/json"); // produce와 같은 표현  -> @RequestBody TestVO v로 보내겠다
        // header.add("Access-Control-Allow-Origin", "http://example.com");

        ResponseEntity entity = new ResponseEntity(vo, header, HttpStatus.OK); // 데이터, 헤더, 상태값 세개를 보낼 수도 있음
        return entity;
    }

    /* -----------------------------------------------------------*/
    /*
    요청주소 : /api/v1/getData
    메서드 : get
    요청 파라미터 : sno(숫자), name(문자)
    응답 파라미터 : MemoVO
    헤더에 담을 내용 HttpStatus.OK
    fetch API 사용해서 클라이언트에 요청 응답
     */

    @GetMapping("/api/v1/getData")
    public ResponseEntity<MemoVO> exampleData(@RequestParam("sno") int sno,
                                              @RequestParam("name") String name) {
        System.out.println(sno + ", " + name);

        return new ResponseEntity<MemoVO>(
                new MemoVO(1L, "홍길동", "1234", null, "Y")
                , HttpStatus.OK);
    }

    /*
    요청주소 : /api/v1/getInfo
    메서드 : post
    요청 파라미터 : MemoVO 타입
    응답 파라미터 : List<MemoVO>타입
    헤더에 담을 내용 HttpStatus.OK
    fetch API 사용해서 클라이언트에 요청 응답
    */

    @PostMapping("/api/v1/getInfo")
    public ResponseEntity<List<MemoVO>> getInfo(@RequestBody @Valid MemoVO vo, // @Valid를 넣어서 유효성 검사도 할 수 있음
                                                BindingResult binding) { // 실패하면 binding

        if(binding.hasErrors()) {
            System.out.println("유효성 검증에 실패함");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        // DB 들어갈 수 있음

        List<MemoVO> list = new ArrayList<>();
        list.add(vo);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
