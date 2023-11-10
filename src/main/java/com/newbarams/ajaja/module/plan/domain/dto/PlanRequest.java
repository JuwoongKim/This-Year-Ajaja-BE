package com.newbarams.ajaja.module.plan.domain.dto;

import java.util.List;

public class PlanRequest {

	public record Create(
		String title,
		String description,

		int remindTotalPeriod,
		int remindTerm,
		int remindDate,
		String remindTime,

		boolean isPublic,

		List<String> tags,

		List<String> messages
	) {
	}

	public record Update(
		String title,
		String description,

		int remindTotalPeriod,
		int remindTerm,
		int remindDate,
		String remindTime,

		boolean isPublic,
		boolean isRemindable,

		List<String> tags,

		List<String> messages
	) {
	}
}
