package com.newbarams.ajaja.module.plan.infra;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;
import com.newbarams.ajaja.module.ajaja.domain.Ajaja;
import com.newbarams.ajaja.module.plan.domain.Message;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "plans")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlanEntity extends BaseEntity<PlanEntity> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_id")
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Integer achieveRate;

	@Column(nullable = false)
	private Integer iconNumber;

	@Column(nullable = false, length = 30)
	private String title;
	@Column(nullable = false, length = 300)
	private String description;

	@Column(nullable = false)
	private Integer remindTotalPeriod;
	@Column(nullable = false)
	private Integer remindTerm;
	@Column(nullable = false)
	private Integer remindDate;
	@Column(nullable = false)
	private String remindTime;

	@Column(nullable = false, name = "is_public")
	private boolean isPublic;
	@Column(nullable = false)
	private boolean canRemind;
	@Column(nullable = false)
	private boolean canAjaja;
	@Column(nullable = false, name = "is_deleted")
	private boolean deleted;

	@ElementCollection(fetch = FetchType.EAGER) // todo:메세지 로딩 오류로 인한 임시 코드 (나중에 지우기)
	@CollectionTable(name = "remind_messages", joinColumns = @JoinColumn(name = "plan_id"))
	@OrderBy("remindDate ASC")
	private List<Message> messages = new ArrayList<>();

	@Size
	@Where(clause = "is_canceled = false")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "target_id")
	private List<Ajaja> ajajas = new ArrayList<>();
}
