package com.newbarams.ajaja.module.feedback.domain;

import java.time.Instant;
import java.util.List;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.infra.model.AchieveInfo;
import com.newbarams.ajaja.module.feedback.infra.model.FeedbackInfo;

public interface FeedbackQueryRepository {
	List<Feedback> findAllFeedbackByPlanId(Long planId);

	boolean findByPlanIdAndPeriod(Long feedbackId, TimeValue time, Instant instant);

	List<FeedbackInfo> findFeedbackInfosByPlanId(Long planId);

	List<AchieveInfo> findAchievesByUserIdAndYear(Long userId, int year);
}
