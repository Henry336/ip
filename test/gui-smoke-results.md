# Week 6 GUI smoke evidence

15 September 2026, Windows, Microsoft OpenJDK 25.0.4, JavaFX 17.0.7.
Harness: `test/GuiSmoke.java`, using classes/resources from `build/libs/ari.jar`
and a new temporary working directory, never personal task data.

| Check | Result |
|---|---|
| Whitespace-only Send disabled | PASS |
| TextField action submits valid tasks and list | PASS |
| Invalid `bye extra` does not close | PASS |
| Error uses explicit `Please check:` text and contrasting color | PASS |
| Input focus restored after submission | PASS |
| 420 x 480 outer window, 404 x 441 scene | PASS; input visible, error readable |
| 900 x 720 outer window, 884 x 681 scene | PASS; long text wraps |
| Accepted tasks present in temporary data file before exit | PASS |
| Valid bye ends the process without harness-forced exit | PASS |

Snapshots: generated `build/gui-smoke/proof/Ui.png`, `minimum-error.png`,
`wide-long.png`. The first resizing attempt captured before a JavaFX pulse;
the harness now waits between phases and asserts actual scene dimensions.
The corrected snapshots were visually inspected.

An initial sandbox run could not read the JDK security configuration. The
successful run used normal host access. JavaFX emits unnamed-module, native
access and deprecated Unsafe warnings on Java 25; these did not stop startup,
interaction, persistence or exit. No dependency upgrade was made in this pass.

This is not a macOS/Linux GUI compatibility claim or an assistive-technology
audit. Real mouse interaction and teammate cross-platform checks remain useful
manual release checks; this harness exercises the TextField action handlers.

## Reproduce

Build the JAR, then compile the harness with Java 25:

```text
javac -cp build/libs/ari.jar -d build/gui-smoke test/GuiSmoke.java
```

From a NEW empty temporary working directory, run Java 25 with an absolute
classpath containing both `build/gui-smoke` and `build/libs/ari.jar`:

```text
java -ea -cp <classes-and-jar-classpath> ari.smoke.GuiSmoke <snapshot-directory>
```

Use `;` between classpath entries on Windows and `:` on Unix. The harness
refuses a working directory already containing `data`. It performs no remote
writes and exits through Ari's successful bye behavior.
