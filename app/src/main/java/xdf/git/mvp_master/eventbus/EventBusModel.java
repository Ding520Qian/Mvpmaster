package xdf.git.mvp_master.eventbus;

import android.content.Intent;

public class EventBusModel {

    private Intent intent;
    private String type;

    public EventBusModel(Intent intent, String type) {
        this.intent = intent;
        this.type = type;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
