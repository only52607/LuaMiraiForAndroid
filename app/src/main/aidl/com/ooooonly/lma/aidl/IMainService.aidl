// IMainService.aidl
package com.ooooonly.lma.aidl;

import com.ooooonly.lma.aidl.ILuaScriptManager;
import com.ooooonly.lma.aidl.IBotManager;

// Declare any non-default types here with import statements

interface IMainService {
    ILuaScriptManager getLuaScriptManager();

    IBotManager getBotManager();
}