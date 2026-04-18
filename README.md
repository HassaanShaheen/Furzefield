# Furzefield Leisure Centre (FLC)

Desktop **JavaFX** application for members to browse a class timetable, **sign up / sign in**, **book** and **change** lesson bookings, **submit reviews** for lessons they attended, and view **reports** on attendance, ratings, and income. All business data is held **in memory** for the running session (nothing is written to disk).

---

## Requirements

| Tool | Notes |
|------|--------|
| **JDK** | **Java 26** (matches `maven.compiler.source` / `target` in `pom.xml`). |
| **Maven** | 3.8+ recommended. |
| **JavaFX** | Pulled in via Maven (`javafx-controls`, `javafx-fxml` 21.x). The `javafx-maven-plugin` runs the app with the correct module/runtime setup. |

---

## Quick start

From the project root (`pom.xml` directory):

```bash
mvn clean test
```

Run the GUI:

```bash
mvn javafx:run
```

The app opens on the **Sign in** screen. Use a demo account (below) or **Create account** to register a new member.

---

## Demo accounts

Ten members are created at startup with the same password:

| Username | Password | Display name (example) |
|----------|----------|---------------------------|
| `m01` … `m10` | `flc2026` | Alice Ahmed, Ben Brown, … (see `SampleDataLoader`) |

After **Create account**, you sign in with the username and password you chose. New users get a **member record** aligned with their username and can book lessons like the demo users.

---

## What is loaded at startup?

`SampleDataLoader` (called from `FlcApplicationContext.createWithSampleData()`) seeds:

- **10 members** and matching **auth** records (demo users).
- **48 lessons**: 8 **weekends** × (Saturday + Sunday) × 3 **time slots** (Morning, Afternoon, Evening).  
  Lesson IDs look like `W2-SATURDAY-AFTERNOON` (weekend + day + slot).
- **Exercise types** (Yoga, Zumba, Aquacise, Box Fit, Body Blitz) with **fixed prices** per type on each lesson.

**No bookings and no reviews** are pre-created. Everyone starts with an empty diary; you add data by using the app.

---

## Member features (after login)

| Screen | Purpose |
|--------|--------|
| **Dashboard** | Tiles to open each feature; **Back** / navigation preserves window size where possible. |
| **Timetable** | Browse all lessons; filter by **day** and/or **exercise type**. |
| **Book a lesson** | Table of lessons you **can** still book: **not full** (max 4 members per lesson), **not already booked** by you, and respecting **time conflict** rules (see below). Full lessons do **not** appear. |
| **Change booking** | Your bookings in one table; choose a **target** lesson in the second table. Targets **hide** full lessons and invalid moves (same lesson, duplicate lesson, etc.). **Apply** asks for **confirmation** before the service runs. |
| **Add review** | Pick a lesson you are **booked on** and have **not** reviewed yet; rating 1–5 and comment. A **“Your reviews”** table lists everything you have submitted this session. |
| **Reports** | Two text reports: (1) per-lesson **bookings**, **review count**, **average rating** across **all weekends** in the timetable; (2) **income by exercise type** over the same range. Output is also printed to the **console**. |

Styling lives in `src/main/resources/styles/app.css`.

---

## Booking rules (business logic)

Implemented in `BookingService` and reflected in the UI via `LessonListFilters` where relevant.

1. **Capacity** — At most **4** members per lesson (`MAX_MEMBERS_PER_LESSON`).
2. **One booking per lesson** — You cannot book the same lesson twice.
3. **Time conflict** — You cannot hold two lessons in the **same weekend**, **same calendar day**, and **same time band** (e.g. two Saturday-morning slots **in the same weekend**). **Different weekends** are allowed for the same day + slot pattern.
4. **Change booking** — Validates ownership, target not full, no time conflict with your **other** bookings (the booking being changed is excluded from the conflict check), and you are not already on the target lesson under another booking.

If an action fails, messages appear in the form **banner** and, where configured, a **dialog**.

---

## Architecture (high level)

```
com.flc
├── MainApp.java                 # JavaFX entry; builds context + StageNavigator
├── FlcApplicationContext.java   # Wires repositories, services, session (current user/member)
├── SampleDataLoader.java        # Seeds members, auth, lessons only
├── navigation
│   └── StageNavigator.java      # Scenes: login, signup, dashboard, features
├── model                        # Lesson, Booking, Member, Review, UserAccount, enums
├── repository                   # In-memory lists (bookings, lessons, members, reviews)
├── service                      # Auth, Booking, Review, Timetable, Report
├── controller                   # FXML controllers
└── ui.fx                        # Shared tables, feedback, alerts, lesson filters, refresh helpers
```

**FXML** views are under `src/main/resources/view/`. Controllers are loaded by `StageNavigator` and receive `init(...)` with the navigator and `FlcApplicationContext`.

---

## Reports

`ReportService` builds:

1. **Report 1** — For every lesson in the timetable (all weekends present in data), shows booked headcount, number of reviews, and average rating (`n/a` if no reviews).
2. **Report 2** — Sums **price × bookings** per exercise type over the same lessons, then highlights the highest-income type (ties listed).

Reports use the **same in-memory** bookings and reviews as the rest of the app.

---

## Tests

```bash
mvn test
```

Tests live under `src/test/java` (e.g. `BookingServiceRulesTest`, `ReportServiceTest`, `FlcDataRequirementsTest`, `AuthServiceTest`, `MainAppTest` for critical resources).

---

## Limitations (by design)

- **In-memory only** — Closing the app clears bookings, reviews, and any members created only in memory during the session (demo seed members return on next launch).
- **Single JVM** — No multi-user server; one desktop instance, one shared context.
- **Java 26** — Older JDKs may not compile without changing `pom.xml`.

---

## Troubleshooting

| Issue | What to check |
|--------|----------------|
| `javafx:run` fails | JDK version, run from directory containing `pom.xml`, network for Maven deps. |
| Empty book / change lists | Normal if every visible lesson is full or conflicts with your bookings; try another weekend/slot or **Timetable** to see all lessons. |
| Report missing a booking | Reports include **all** weekends in the lesson list; refresh **Reports** after changing data. |

---

## Licence / coursework

This project is structured for learning and demonstration (leisure-centre domain, layered packages, JavaFX UI). Adjust licence or attribution here if you redistribute the code.
