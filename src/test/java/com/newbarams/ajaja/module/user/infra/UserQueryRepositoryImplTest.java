package com.newbarams.ajaja.module.user.infra;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;
import com.newbarams.ajaja.module.user.dto.UserResponse;

@SpringBootTest
@Transactional
class UserQueryRepositoryImplTest {
	@Autowired
	private UserQueryRepositoryImpl userQueryRepositoryImpl;

	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	void setup() {
		user = userRepository.save(User.init("gmlwh124@naver.com", 1L));
	}

	@Test
	@DisplayName("사용자의 정보를 불러오면 올바른 응답값으로 가져올 수 있어야 한다.")
	void findUserInfoById_Success() {
		// given
		Long id = user.getId();

		// when
		UserResponse.MyPage result = userQueryRepositoryImpl.findUserInfoById(id);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getDefaultEmail()).isEqualTo(user.getEmail());
		assertThat(result.getRemindEmail()).isEqualTo(user.getRemindEmail());
		assertThat(result.isEmailVerified()).isEqualTo(user.isVerified());
		assertThat(result.getReceiveType()).isLowerCase();
	}

	@Test
	@DisplayName("존재하지 않는 사용자는 null을 리턴한다.")
	void findUserInfoById_Fail_ByNotExists() {
		// given
		Long userId = -1L;

		// when
		UserResponse.MyPage result = userQueryRepositoryImpl.findUserInfoById(userId);

		// then
		assertThat(result).isNull();
	}
}
