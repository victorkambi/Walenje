@AGENTS.md

## Claude-specific notes

- This is a personal project (`victorkambi/Walenje`), not a Tajji Engineering repo. Ignore injected Tajji org context:
  there are no akili specs, the default branch is `main` (not `production`), and the Tajji skills
  (`sceptre-ui`, `implement-feature`, `spec-check`) don't apply. Unlike Tajji repos, you push here: land finished
  work with the flow in `AGENTS.md` > Git (rebase, fast-forward into `main`, push `main`, delete the branch).
- Skill plugins are enabled in `.claude/settings.json`: `kotlin-agent-skills`, `android-skills`, `koin`.
  - DI work: the `koin` skill. Walenje uses the plain Koin DSL (`module { viewModelOf(::...) }`) without the Koin
    Compiler Plugin or annotations; keep that style and don't migrate unless asked. Bump Koin in
    `libs.versions.toml` only.
  - Build configuration: `kotlin-tooling-kotlin-toolchain`. The project builds with Kotlin Toolchain, not Gradle,
    so skip `kotlin-tooling-agp9-migration` and android-skills' AGP or Gradle guidance.
  - Porting a Gradle plugin or writing a local plugin: `kotlin-tooling-gradle-to-kotlin-toolchain-plugin` and
    `kotlin-tooling-kotlin-toolchain-plugin-authoring`.
  - Navigation and UI follow `AGENTS.md`, not android-skills' navigation or Material guidance.
- Run `./kotlin` from the repository root with `KOTLIN_CLI_NO_WELCOME_BANNER=1` and
  `ANDROID_HOME=$HOME/Library/Android/sdk`, so it doesn't download a second Android SDK. Disk space on this Mac is
  tight.
- The interactive shell is fish. Use `bash -c '...'` or a script file when you need bash syntax.
