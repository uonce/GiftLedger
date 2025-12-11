package springboot.giftledger.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.giftledger.auth.dto.LoginResultDto;
import springboot.giftledger.dto.MemberDto;
import springboot.giftledger.entity.Member;
import springboot.giftledger.enums.Role;
import springboot.giftledger.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterServiceImpl implements RegisterService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResultDto register(MemberDto memberDto) {
        log.info(" 화원 가입 시작 ");

        try{
            if(memberRepository.existsByEmail(memberDto.getEmail())){
                log.warn("이메일 중복");
                throw new Exception("이메일 중복");
            }
            log.info(" 비밀번호 암호화 ");
            String encodedPassword = passwordEncoder.encode(memberDto.getPassword());

            log.info(" member entity 생성");
            Member member = Member.builder()
                    .email(memberDto.getEmail())
                    .password(encodedPassword)
                    .name(memberDto.getName())
                    .ages(memberDto.getAges())
                    .role(Role.ROLE_USER)
                    .build();

            memberRepository.save(member);
            log.info(" 회원 가입 성공 ");

            return LoginResultDto.builder()
                    .result("success")
                    .token(null)
                    .build();

        } catch (Exception e) {

            log.error("fail register email: {}, password: {}"
                    , memberDto.getEmail()
                    , memberDto.getPassword());

            log.error("에러 상세 정보",e);


            return LoginResultDto.builder()
                    .result("fail")
                    .token(null)
                    .build();
        }
    }
}
