package springboot.giftledger.auth.service;

import springboot.giftledger.auth.dto.LoginResultDto;
import springboot.giftledger.dto.MemberDto;

public interface RegisterService {
    LoginResultDto register(MemberDto memberDto);
}
