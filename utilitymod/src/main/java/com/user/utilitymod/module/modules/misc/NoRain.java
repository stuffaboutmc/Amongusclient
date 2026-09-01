package com.user.utilitymod.module.modules.misc;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

/**
 * Purely cosmetic client-side flag; checked by a mixin/ASM hook or a
 * render-event cancel on weather rendering if you wire it up further.
 * Left as a simple toggle here since weather rendering suppression
 * typically needs a render-event hook specific to your Forge version.
 */
public class NoRain extends Module {

    public NoRain() {
        super("NoRain", Category.MISC);
    }
}
