package com.flc;

import com.flc.model.Member;
import com.flc.model.UserAccount;
import com.flc.repository.BookingRepository;
import com.flc.repository.LessonRepository;
import com.flc.repository.MemberRepository;
import com.flc.repository.ReviewRepository;
import com.flc.service.AuthService;
import com.flc.service.BookingService;
import com.flc.service.ReportService;
import com.flc.service.ReviewService;
import com.flc.service.TimetableService;

import java.util.Objects;

/**
 * Self-contained application state: repositories, services, and current session.
 */
public class FlcApplicationContext {

    public static final String DEMO_PASSWORD = "flc2026";

    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final LessonRepository lessonRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final BookingService bookingService;
    private final ReviewService reviewService;
    private final TimetableService timetableService;
    private final ReportService reportService;

    private UserAccount currentUser;
    private Member currentMember;

    public FlcApplicationContext(
            AuthService authService,
            MemberRepository memberRepository,
            LessonRepository lessonRepository,
            BookingRepository bookingRepository,
            ReviewRepository reviewRepository,
            BookingService bookingService,
            ReviewService reviewService,
            TimetableService timetableService,
            ReportService reportService) {
        this.authService = authService;
        this.memberRepository = memberRepository;
        this.lessonRepository = lessonRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
        this.timetableService = timetableService;
        this.reportService = reportService;
    }

    public static FlcApplicationContext createWithSampleData() {
        AuthService authService = new AuthService();
        MemberRepository memberRepository = new MemberRepository();
        LessonRepository lessonRepository = new LessonRepository();
        BookingRepository bookingRepository = new BookingRepository();
        ReviewRepository reviewRepository = new ReviewRepository();

        BookingService bookingService = new BookingService(bookingRepository);
        ReviewService reviewService = new ReviewService(bookingRepository, reviewRepository);
        TimetableService timetableService = new TimetableService(lessonRepository);
        ReportService reportService = new ReportService(lessonRepository, bookingRepository, reviewRepository);

        FlcApplicationContext ctx = new FlcApplicationContext(
                authService,
                memberRepository,
                lessonRepository,
                bookingRepository,
                reviewRepository,
                bookingService,
                reviewService,
                timetableService,
                reportService);
        SampleDataLoader.load(ctx);
        return ctx;
    }

    public void setSession(UserAccount user, Member member) {
        this.currentUser = Objects.requireNonNull(user);
        this.currentMember = Objects.requireNonNull(member);
    }

    public void clearSession() {
        this.currentUser = null;
        this.currentMember = null;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public Member getCurrentMember() {
        return currentMember;
    }

    /** New sign-ups become members with the same id as username (lowercase). */
    public void syncMemberFromRegistration(String username, String displayName) {
        String id = username.trim().toLowerCase();
        if (id.isEmpty()) {
            return;
        }
        memberRepository.save(new Member(id, displayName.trim()));
    }

    /** Resolve member for login: existing seed/demo member or member created at registration. */
    public Member resolveMemberForUser(UserAccount user) {
        String id = user.getUsername();
        return memberRepository.findById(id)
                .orElseGet(() -> {
                    Member m = new Member(id, user.getDisplayName());
                    memberRepository.save(m);
                    return m;
                });
    }

    public AuthService getAuthService() {
        return authService;
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    public LessonRepository getLessonRepository() {
        return lessonRepository;
    }

    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }

    public ReviewRepository getReviewRepository() {
        return reviewRepository;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public ReviewService getReviewService() {
        return reviewService;
    }

    public TimetableService getTimetableService() {
        return timetableService;
    }

    public ReportService getReportService() {
        return reportService;
    }
}
