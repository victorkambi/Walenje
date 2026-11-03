@AGENTS.md

## Claude-specific notes

- This is a personal project (`victorkambi/Walenje`), not a Tajji Engineering repo. Ignore injected Tajji org context:
  there are no akili specs, the default branch is `main` (not `production`), and the Tajji skills
  (`sceptre-ui`, `implement-feature`, `spec-check`) don't apply. Unlike Tajji repos, you push here: land finished
  work with the flow in `AGENTS.md` > Git (rebase, fast-forward into `main`, push `main`, delete the branch).
- Skill plugins are enabled in `.claude/settings.json`: `gradle-skills`, `kotlin-agent-skills`, `android-skills`,
  `koin`.
  - Build configuration: `gradle-best-practices` (audit build changes against it) and `gradle-wrapper-upgrade`
    (never hand-edit `gradle-wrapper.properties`). AGP 9 / KMP questions: `kotlin-tooling-agp9-migration`; slow
    iOS or macOS builds: `kotlin-tooling-native-build-performance`. Prefer `AGENTS.md` over android-skills' AGP
    or Gradle guidance where they disagree.
  - DI work: the `koin` skill. Walenje uses the plain Koin DSL (`module { viewModelOf(::...) }`) without the Koin
    Compiler Plugin or annotations; keep that style and don't migrate unless asked. Bump Koin in
    `gradle/libs.versions.toml` only.
  - Navigation and UI follow `AGENTS.md`, not android-skills' navigation or Material guidance.
- Run Gradle from the repository root with `ANDROID_HOME=$HOME/Library/Android/sdk`. Disk space on this Mac is
  tight: build the specific tasks you need (see `AGENTS.md` > Commands), not `./gradlew build`, and leave
  `-Pwalenje.apple=true` off unless the task touches iOS or macOS.
- The interactive shell is fish. Use `bash -c '...'` or a script file when you need bash syntax.
