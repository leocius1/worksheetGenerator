# Szintmérő – project guide for Claude

## What this is
A tool for a teacher (the owner of this repo, Leo) to track students' level-assessment results
**per topic**, visualize their progress over time, and generate **personalized worksheets + cheat
sheets** ("feladatlap" + "puska") with the Claude API that focus on each student's weak topics
and stretch their strong ones. Subject-agnostic (any subject, any grade), multiple classes.

## How to work with me (important)
- **I am learning.** I have 2–3 years of Java + GCP experience, but Spring Boot web, Firestore,
  streams/Optional and frontend are new or rusty. The goal is that *I* can build this, not that
  the code gets written fast.
- **I write the core code myself**: repositories, controllers, services, progress calculation,
  the Claude prompt/parsing. Explain, give hints, show *parallel* examples, and review what I
  wrote — point out bugs with a concrete scenario and let me fix them.
- **You may write boilerplate directly**: `pom.xml`, imports, config, `.gitignore`, test data,
  PowerShell test commands. Ask if unsure which category something is.
- One step at a time; don't run ahead to later steps. Keep explanations short and concrete.
- I may write in Hungarian or English; answer in the language I used.
- Before I commit, remind me to commit after each step that works.

## Environment
- Windows, IntelliJ IDEA, terminal is **PowerShell** → use `Invoke-RestMethod`, not `curl`
  with `\"` escaping. Multi-line JSON: `$body = @' ... '@`.
- Spring Boot **4.1.1** (Jackson 3; web starter is `spring-boot-starter-webmvc`), Maven,
  `java.version` 21 in the pom, but the local JDK is **24** → switch to an LTS JDK (21 or 25)
  before deploying.
- Root package: `hu.szintmero.szintmero`. App runs on `http://localhost:8080`.
- "Unable to connect" = the app is not running → check the Run tab first.

## Roadmap
0. Data model ✅
1. Spring Boot + REST API, in-memory storage ✅ (Topic + SchoolTest endpoints work)
2. **Firestore** ← current step
   - 2a: personal GCP project, Firestore Native mode `(default)` DB in an EU region,
     `gcloud auth application-default login` (ADC, no key files) — in progress
   - 2b: replace the `ConcurrentHashMap` in the repositories with Firestore; controllers must
     not change (same repository method signatures)
3. Progress calculation (service + unit tests): per student, per topic, % over time
4. Claude API: build prompt from a student's profile, request strict JSON, parse it
5. Simple web frontend
6. Printable worksheet / cheat sheet / answer key
7. Deploy to Cloud Run (+ auth, budget alert, secrets in Secret Manager)

## Data model (decided in step 0 — keep it)
```java
SchoolClass(String id, String name, String level)        // name "7.A" (for humans); level "7. évfolyam" (for the Claude prompt)
Student(String id, String name, String classId)          // reference stored on the "many" side
Topic(String id, String name, String subject, String description) // description tells Claude what the topic covers
SchoolTest(String id, String name, String classId, LocalDate date,
           List<MeasuredTopic> topics, List<Score> scores)
MeasuredTopic(String topicId, double maxScore)            // max belongs to (test, topic), not to each score
Score(String studentId, String topicId, double achievedScore)  // double: half points allowed
```
Key decisions and why:
- **Scores are embedded in the SchoolTest document**, not separate documents: they are always
  read together; Firestore bills per document read (10 tests = 10 reads instead of ~1500).
- **Each relationship is stored once**, on the "many" side (`Student.classId`,
  `Score.studentId`); the other direction is a query. No `studentIds`/`testIds` lists.
- References are **ids**, never embedded copies of other entities.
- `date` = when the test was **written** (entered by the teacher), never `LocalDate.now()`.
- Ids are UUID strings. `SchoolTest.createNew(...)` assigns the id; the controller calls it on
  POST; `repository.save()` only stores (upsert) and requires a non-null id.
  (Open: `createNew(SchoolTest)` currently keeps an existing id — consider renaming or switching
  to a field-based factory + a `CreateSchoolTestRequest` DTO without id.)
- Planned: POST = always create, PUT `/{id}` = update (404 if missing). Missing resources return
  404 via `ResponseStatusException(HttpStatus.NOT_FOUND)`, not a bare `orElseThrow()` (500).

## Security / privacy
- **Never** put API keys (Claude API key, etc.) in code or in git. Locally: environment
  variable; on Cloud Run: Secret Manager.
- Student data stays in an EU-region Firestore; don't send student names to the Claude API —
  only topics, percentages and grade level.

## Costs (rough, for context)
Claude API ≈ $0.02–0.09 per worksheet depending on model (Sonnet ≈ $0.045). Firestore and Cloud Run
stay within free tiers at this scale (50k reads / 20k writes per day; 2M requests per month).
