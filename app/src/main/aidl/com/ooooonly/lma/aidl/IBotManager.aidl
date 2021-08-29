// IBotManager.aidl
package com.ooooonly.lma.aidl;

import com.ooooonly.lma.model.parcel.mirai.ParcelableBot;

interface IBotManager {
    List<ParcelableBot> getBotList();
    void loginBot(long botId);
    void addBot(in ParcelableBot bot);
}