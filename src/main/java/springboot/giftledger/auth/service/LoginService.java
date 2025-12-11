package springboot.giftledger.auth.service;

import springboot.giftledger.auth.dto.LoginResultDto;

public interface LoginService {
    LoginResultDto login(String email, String password);
}
