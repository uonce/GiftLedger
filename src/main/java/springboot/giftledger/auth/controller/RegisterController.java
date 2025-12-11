package springboot.giftledger.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.giftledger.auth.dto.LoginResultDto;
import springboot.giftledger.auth.service.RegisterService;
import springboot.giftledger.dto.MemberDto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/users")
    public ResponseEntity<LoginResultDto> register(@RequestBody  MemberDto memberDto){
        log.info(" 회원 가입 요청 email={} name={} ", memberDto.getEmail(), memberDto.getName());

        LoginResultDto loginResultDto = registerService.register(memberDto);

        if("success".equals(loginResultDto.getResult())){
            return ResponseEntity.ok(loginResultDto);
        } else {
            return ResponseEntity.badRequest().body(loginResultDto);
        }
    }
}
