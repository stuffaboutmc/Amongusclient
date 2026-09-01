package com.user.utilitymod.module.modules.misc;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

/**
 * Flag checked by the item-use timing logic (hook this into
 * PlayerControllerMP#onPlayerRightClick if you want it fully wired up;
 * left as a togglable flag here for you to hook where you prefer).
 */
public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", Category.MISC);
    }
}
