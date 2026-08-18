# Gradle wrapper — not generated yet

This sandbox has no network access to `services.gradle.org`, so the
Gradle wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`) could not be
generated here. Run this once on your own machine, from `android/`:

```bash
gradle wrapper --gradle-version 8.9
```

Then commit the generated `gradlew`, `gradlew.bat`, and
`gradle/wrapper/gradle-wrapper.{jar,properties}` files. Flagged in
`docs/HANDOVER.md` Section 6 as a Slice 1b known gap.
