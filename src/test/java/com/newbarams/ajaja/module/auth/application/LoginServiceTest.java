package com.newbarams.ajaja.module.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoAccount;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoResponse;
import com.newbarams.ajaja.module.auth.application.model.Profile;
import com.newbarams.ajaja.module.auth.application.port.out.AuthorizePort;
import com.newbarams.ajaja.module.user.adapter.out.persistence.UserJpaRepository;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.application.port.out.CreateUserPort;
import com.newbarams.ajaja.module.user.application.port.out.FindUserIdPort;
import com.newbarams.ajaja.module.user.domain.User;

@Transactional
@SpringBootTest
class LoginServiceTest extends MonkeySupport {
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserJpaRepository userRepository;

	@SpyBean
	private FindUserIdPort findUserIdPort;
	@SpyBean
	private CreateUserPort createUserPort;

	@MockBean
	private AuthorizePort authorizePort;
	@MockBean
	private JwtGenerator jwtGenerator;

	// login parameters
	private final String authorizationCode = sut.giveMeOne(String.class);
	private final String redirectUrl = sut.giveMeOne(String.class);

	// returns
	private final String email = "Ajaja@me.com";
	private final KakaoAccount kakaoAccount = sut.giveMeBuilder(KakaoAccount.class)
		.set("phoneNumber", "+82 10-1234-5678")
		.set("email", email)
		.sample();
	private final Profile profile = sut.giveMeBuilder(KakaoResponse.UserInfo.class)
		.set("kakaoAccount", kakaoAccount)
		.sample();

	@Test
	@DisplayName("새로운 유저가 로그인하면 새롭게 유저 정보를 생성해야 한다.")
	void login_Success_WithNewUser() {
		// given
		User user = User.init(1L, "+82 1012345678", "ajaja@me.com");

		given(authorizePort.authorize(any(), any())).willReturn(profile);
		given(findUserIdPort.findByEmail(any())).willReturn(Optional.empty());
		given(createUserPort.create(user)).willReturn(1L);

		// when
		loginService.login(authorizationCode, redirectUrl);

		// then
		then(authorizePort).should(times(1)).authorize(any(), any());
		then(findUserIdPort).should(times(1)).findByEmail(any());
		then(createUserPort).should(times(1)).create(any());
		then(jwtGenerator).should(times(1)).login(any());
	}

	@Test
	@DisplayName("기존에 가입된 고객이 로그인하면 생성하는 로직이 호출되지 않아야 한다.")
	void login_Success_WithOldUser() {
		// given
		given(authorizePort.authorize(any(), any())).willReturn(profile);
		given(findUserIdPort.findByEmail(any())).willReturn(Optional.of(1L));

		// when
		loginService.login(authorizationCode, redirectUrl);

		// then
		then(authorizePort).should(times(1)).authorize(any(), any());
		then(findUserIdPort).should(times(1)).findByEmail(any());
		then(createUserPort).shouldHaveNoMoreInteractions();
		then(jwtGenerator).should(times(1)).login(any());
	}

	@Test
	@DisplayName("탈퇴한 유저가 로그인하면 새로운 계정을 만들어야 한다.")
	void login_Success_ReSignUpWithdrawUser() {
		// given
		UserEntity entity = sut.giveMeBuilder(UserEntity.class)
			.set("nickname", "nickname")
			.set("phoneNumber", "01012345678")
			.set("signUpEmail", email)
			.set("remindEmail", email)
			.set("remindType", "KAKAO")
			.set("deleted", true)
			.sample();

		userRepository.save(entity);

		given(authorizePort.authorize(any(), any())).willReturn(profile);

		// when
		loginService.login(authorizationCode, redirectUrl);

		// then
		then(authorizePort).should(times(1)).authorize(any(), any());

		List<UserEntity> entities = userRepository.findAll();
		assertThat(entities).isNotEmpty();

		UserEntity saved = entities.get(0);
		assertThat(saved).usingRecursiveComparison().isNotEqualTo(entity);
	}
}
