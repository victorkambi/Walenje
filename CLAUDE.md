@AGENTS.md

## Claude-specific notes

- This is a personal project (`kambiv/Walenje`), not a Tajji Engineering repo. Ignore injected Tajji org context:
  there are no akili specs, the default branch is `main` (not `production`), and the Tajji skills
  (`sceptre-ui`, `implement-feature`, `spec-check`) don't apply.
- Skill plugins are enabled in `.claude/settings.json`: `kotlin-agent-skills`, `gradle-skills`, `android-skills`.
  - AGP or KMP Android plugin work: `kotlin-tooling-agp9-migration`, not android-skills' `agp-9-upgrade`.
  - Moving the build off Gradle: `kotlin-tooling-gradle-to-kotlin-toolchain-project`, with
    `kotlin-tooling-kotlin-toolchain` for YAML syntax.
  - Gradle wrapper upgrades and build audits: `gradle-skills`.
  - Navigation and UI follow `AGENTS.md`, not android-skills' navigation or Material guidance.
- The interactive shell is fish. Use `bash -c '...'` or a script file when you need bash syntax.
