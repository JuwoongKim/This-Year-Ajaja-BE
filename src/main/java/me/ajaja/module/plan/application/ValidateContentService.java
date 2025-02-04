package me.ajaja.module.plan.application;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.domain.BanWordFilter;
import me.ajaja.module.plan.dto.BanWordValidationResult;
import me.ajaja.module.plan.dto.PlanRequest;

@Service
@RequiredArgsConstructor
public class ValidateContentService {
	public BanWordValidationResult check(PlanRequest.CheckBanWord request) {
		BanWordValidationResult.Common titleResult = getResult(request.getTitle());
		BanWordValidationResult.Common descriptionResult = getResult(request.getDescription());

		return new BanWordValidationResult(titleResult, descriptionResult);
	}

	private BanWordValidationResult.Common getResult(String origin) {
		List<String> result = BanWordFilter.validate(origin);

		if (result.isEmpty()) {
			return new BanWordValidationResult.Common(false, Collections.emptyList());
		}

		return new BanWordValidationResult.Common(true, result);
	}
}
