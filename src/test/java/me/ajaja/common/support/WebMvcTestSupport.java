package me.ajaja.common.support;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.apache.commons.codec.CharEncoding.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.ajaja.common.annotation.ApiTest;
import me.ajaja.common.annotation.ParameterizedApiTest;
import me.ajaja.global.security.jwt.JwtGenerator;
import me.ajaja.global.security.jwt.JwtParser;
import me.ajaja.module.ajaja.application.SwitchAjajaService;
import me.ajaja.module.auth.application.port.in.LoginUseCase;
import me.ajaja.module.auth.application.port.in.ReissueTokenUseCase;
import me.ajaja.module.feedback.application.LoadFeedbackInfoService;
import me.ajaja.module.feedback.application.LoadTotalAchieveService;
import me.ajaja.module.feedback.application.UpdateFeedbackService;
import me.ajaja.module.plan.application.CreatePlanService;
import me.ajaja.module.plan.application.DeletePlanService;
import me.ajaja.module.plan.application.LoadPlanService;
import me.ajaja.module.plan.application.UpdatePlanService;
import me.ajaja.module.plan.application.ValidateContentService;
import me.ajaja.module.remind.application.port.in.GetPlanInfoUseCase;
import me.ajaja.module.remind.application.port.in.GetRemindInfoUseCase;
import me.ajaja.module.remind.application.port.in.SendTestRemindUseCase;
import me.ajaja.module.remind.application.port.in.UpdateRemindInfoUseCase;
import me.ajaja.module.remind.application.port.out.FindPlanRemindQuery;
import me.ajaja.module.user.application.port.in.ChangeRemindTypeUseCase;
import me.ajaja.module.user.application.port.in.LogoutUseCase;
import me.ajaja.module.user.application.port.in.RefreshNicknameUseCase;
import me.ajaja.module.user.application.port.in.SendVerificationEmailUseCase;
import me.ajaja.module.user.application.port.in.VerifyCertificationUseCase;
import me.ajaja.module.user.application.port.in.WithdrawUseCase;
import me.ajaja.module.user.application.port.out.GetMyPageQuery;

/**
 * Supports Cached Context On WebMvcTest with Monkey <br>
 * When Authentication is required USE @ApiTest, @ParameterizedApiTest
 * @see ApiTest
 * @see ParameterizedApiTest
 * @author hejow
 */
@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
public abstract class WebMvcTestSupport extends MonkeySupport {
	private static final String ANY_END_POINT = "/**";

	protected static final String USER_END_POINT = "/users";
	protected static final String PLAN_END_POINT = "/plans";
	protected static final String FEEDBACK_END_POINT = "/feedbacks";
	protected static final String REMIND_END_POINT = "/reminds";
	protected static final String BEARER_TOKEN = "Bearer eyJhbGxMiJ9.eyJzWpvdyJ9.avFKonhbIIhEg8H1dycQkhQ";

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;

	@BeforeEach
	void setup(
		WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation
	) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.alwaysDo(print())
			.apply(documentationConfiguration(restDocumentation))
			.addFilter(new CharacterEncodingFilter(UTF_8, true))
			.defaultRequest(post(ANY_END_POINT).with(csrf().asHeader()))
			.defaultRequest(get(ANY_END_POINT).with(csrf().asHeader()))
			.defaultRequest(put(ANY_END_POINT).with(csrf().asHeader()))
			.defaultRequest(delete(ANY_END_POINT).with(csrf().asHeader()))
			.build();
	}

	protected static Stream<Arguments> authenticationFailResults() {
		return Stream.of(
			Arguments.of(INVALID_BEARER_TOKEN, "invalid-bearer-token"),
			Arguments.of(INVALID_SIGNATURE, "bad-signature"),
			Arguments.of(INVALID_TOKEN, "malformed-jwt"),
			Arguments.of(EXPIRED_TOKEN, "expired-jwt"),
			Arguments.of(UNSUPPORTED_TOKEN, "unsupported-jwt"),
			Arguments.of(EMPTY_TOKEN, "empty-jwt")
		);
	}

	/**
	 * Caching Mock Beans
	 */
	@MockBean
	protected JwtGenerator jwtGenerator; // mock login
	@MockBean
	protected JwtParser jwtParser; // todo: delete after authentication aop applied

	// Auth
	@MockBean
	protected LoginUseCase loginUseCase;
	@MockBean
	protected ReissueTokenUseCase reissueTokenUseCase;

	// User
	@MockBean
	protected ChangeRemindTypeUseCase changeRemindTypeUseCase;
	@MockBean
	protected LogoutUseCase logoutUseCase;
	@MockBean
	protected RefreshNicknameUseCase refreshNicknameUseCase;
	@MockBean
	protected SendVerificationEmailUseCase sendVerificationEmailUseCase;
	@MockBean
	protected VerifyCertificationUseCase verifyCertificationUseCase;
	@MockBean
	protected WithdrawUseCase withdrawUseCase;
	@MockBean
	protected GetMyPageQuery getMyPageQuery;

	// Plan
	@MockBean
	protected CreatePlanService createPlanService;
	@MockBean
	protected LoadPlanService getPlanService;
	@MockBean
	protected DeletePlanService deletePlanService;
	@MockBean
	protected UpdatePlanService updatePlanService;
	@MockBean
	protected SwitchAjajaService switchAjajaService;
	@MockBean
	protected ValidateContentService validateContentService;

	// Feedback
	@MockBean
	protected UpdateFeedbackService updateFeedbackService;
	@MockBean
	protected LoadTotalAchieveService loadTotalAchieveService;
	@MockBean
	protected LoadFeedbackInfoService loadFeedbackInfoService;

	// Remind
	@MockBean
	protected GetPlanInfoUseCase getPlanInfoUseCase;
	@MockBean
	protected GetRemindInfoUseCase getRemindInfoUseCase;
	@MockBean
	protected UpdateRemindInfoUseCase updateRemindInfoUseCase;
	@MockBean
	protected FindPlanRemindQuery findPlanRemindQuery;
	@MockBean
	protected SendTestRemindUseCase sendTestRemindUseCase;
}
