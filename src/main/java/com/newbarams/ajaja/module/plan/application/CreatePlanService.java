package com.newbarams.ajaja.module.plan.application;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.plan.repository.PlanRepository;
import com.newbarams.ajaja.module.remind.application.CreateRemindService;
import com.newbarams.ajaja.module.tag.application.TagService;
import com.newbarams.ajaja.module.tag.domain.Tag;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePlanService {
	private final PlanRepository planRepository;
	private final TagService tagService;
	private final CreateRemindService createRemindService;

	public PlanResponse.Create create(PlanRequest.Create request) {
		Set<Tag> tags = tagService.getTags(request.tags());

		Plan plan = PlanMapper.toEntity(request, 1L, tags);
		Plan savedPlan = planRepository.save(plan);

		createRemindService.createRemind(plan.getUserId(), plan.getId(), plan.getMessages(), plan.getInfo());

		return PlanMapper.toResponse(savedPlan);
	}
}
