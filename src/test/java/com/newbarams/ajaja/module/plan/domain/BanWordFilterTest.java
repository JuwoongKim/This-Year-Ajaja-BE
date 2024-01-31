package com.newbarams.ajaja.module.plan.domain;

import static org.assertj.core.api.Assertions.*;

import org.ahocorasick.trie.Emit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.newbarams.ajaja.module.plan.dto.BanWordValidationResult;

class BanWordFilterTest {
	BanWordFilter banWordFilter = new BanWordFilter();

	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("문장에 비속어가 포함되었을 경우, 비속어 단어와 위치를 반환한다.")
	void badWordFilter_Success() {
		// given
		Emit expected = new Emit(0, 1, "ㅁㅊ");
		Emit expected2 = new Emit(3, 4, "ㅈㄹ");

		// when
		BanWordValidationResult result = banWordFilter.validate("ㅁㅊ ㅈㄹ하네");

		// then
		assertThat(result.getBadWordResults()).hasSize(2);  // 비속어 개수
		assertThat(result.getBadWordResults().get(0)).isEqualByComparingTo(expected);
		assertThat(result.getBadWordResults().get(1)).isEqualByComparingTo(expected2);
	}
}
