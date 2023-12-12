package com.newbarams.ajaja.module.user.application.service;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class RetrieveUserService {
	private final UserRepository userRepository;

	public User loadExistById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new AjajaException(USER_NOT_FOUND));
	}
}
