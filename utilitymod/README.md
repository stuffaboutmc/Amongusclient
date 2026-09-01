# UtilityMod (1.8.9 Forge)

A from-scratch client-side utility mod with a draggable, categorized ClickGUI
(Movement / Render / Combat / Misc), in the style of Augustus — original code,
not a copy of that project's source.

## What's included
- **ClickGUI**: draggable per-category panels, click to toggle a module,
  click the `+` to expand sliders/toggles, drag sliders to adjust.
- **Movement**: Sprint, Speed, Fly, NoFall, Step, AutoWalk
- **Render**: Fullbright, Zoom (hold C), ArmorHUD, Keystrokes, Notifications
- **Misc**: FastPlace (flag), AutoTool, NoRain (flag), Timer
- **Combat**: empty category shell — intentionally no KillAura/reach/etc.
  included. I left this out because those modules are built specifically to
  give an unfair, hard-to-detect edge against other real players and get
  accounts banned on most servers. Drop your own Module subclasses into
  `module/modules/combat/` and register them in `ModuleManager.init()` if you
  want to extend that category.
- **Default GUI keybind**: Right Shift (change `EventHandler.GUI_OPEN_KEY`)

A couple of modules (`FastPlace`, `NoRain`) are left as bare toggle flags with
a comment on where to wire them in — fully implementing them needs either an
ASM/Mixin hook or a specific packet-timing hook depending on exactly how you
want them to behave, which is easy to add once the rest is compiling for you.

## Easiest way to build it: let GitHub do it (recommended)

This project now includes `.github/workflows/build.yml`, which builds the mod
jar on GitHub's own servers — fast connection, plenty of RAM, no local Java/
Gradle setup needed on your end at all.

1. Create a free account at github.com if you don't have one.
2. Create a new **empty** repository (no README, no .gitignore — just empty).
3. Upload this whole folder's contents to it. Easiest way: on the repo page,
   click "Add file" → "Upload files", then drag in everything from this
   project (including the hidden `.github` folder — if your file picker
   hides dotfiles, use GitHub Desktop or `git` on the command line instead,
   since the browser uploader sometimes won't show hidden folders).
4. Once pushed, click the **Actions** tab on your repo. A workflow run
   should start automatically (or click "Run workflow" if it doesn't).
5. Wait for it to finish — usually 10-20 minutes since it's decompiling
   Minecraft from scratch, same as the local process, just on a much faster
   connection.
6. When it's done (green checkmark), click into that run, scroll to
   **Artifacts**, and download `utilitymod-jar` — that's your compiled mod,
   ready to drop into `.minecraft/mods`.

If it fails, click into the failed step to see the error — same kind of
errors as building locally (an outdated maven URL, etc.), just paste me
whatever it says.

## Building it locally instead (if you'd rather not use GitHub)

1. Install **JDK 8** (Forge 1.8.9 requires Java 8, not newer).
2. Download the **Forge 1.8.9 MDK**: search "Forge 1.8.9 MDK" on
   files.minecraftforge.net, recommended build `11.15.1.2318-1.8.9`.
3. Extract the MDK, then copy everything from this project's `src/` folder
   into the MDK's `src/` folder (merge the folders — same structure).
4. Replace the MDK's `build.gradle` with the one in this project (or merge
   the `minecraft { ... }` block into yours if you customized it).
5. Copy `mcmod.info` into `src/main/resources/` if it isn't already there.
6. From the MDK root:
   ```
   ./gradlew setupDecompWorkspace   (first time only, downloads mappings)
   ./gradlew build
   ```
7. Your compiled jar will be in `build/libs/utilitymod-1.0.0.jar`. Drop it
   into `.minecraft/mods` for a Forge 1.8.9 install.

To test without a full build: `./gradlew runClient` launches Minecraft
straight from the source using the MDK's dev environment.

## Notes on the code
- `module/Module.java` is the base class — `onTick()` fires every client tick
  while a module is enabled; `onEnable()`/`onDisable()` fire once on toggle.
- `module/ModuleManager.java` is where every module gets registered — add new
  ones there.
- `gui/ClickGui.java` is the whole GUI in one screen class; `gui/component/`
  has the draggable `Frame` (category panel) and `ModuleComponent` (per-module
  row + settings).
- `event/HudRenderer.java` draws ArmorHUD, Keystrokes, notifications, and the
  top-left "active modules" watermark list.

If a couple of method names don't line up exactly with the mappings version
you end up using (Forge occasionally renames things between MCP mapping
snapshots), they're almost always one-line fixes — the compiler error will
point straight at it.
