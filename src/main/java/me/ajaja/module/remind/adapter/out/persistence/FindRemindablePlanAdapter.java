package me.ajaja.module.remind.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.plan.infra.PlanQueryRepository;
import me.ajaja.module.remind.application.port.out.FindRemindablePlanPort;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.mapper.RemindMapper;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindRemindablePlanAdapter implements FindRemindablePlanPort {
	private final PlanQueryRepository planQueryRepository;
	private final RemindMapper mapper;

	@Override
	public List<Remind> findAllRemindablePlan(String remindTime, String remindType, TimeValue time) {
		return planQueryRepository.findAllRemindablePlan(remindTime, remindType, time).stream()
			.map(info -> mapper.toDomain(info, time))
			.toList();
	}
}
