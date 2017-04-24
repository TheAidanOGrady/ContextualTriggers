package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * The upcoming event trigger is a trigger for when the user has an event upcoming, indicating a
 * possibility for fitness activity by walking to the location this event is held.
 *
 * @author Aidan O'Grady
 */
public class UpcomingEventTrigger extends SimpleTrigger {
    /**
     * The number of minutes the next event should start from now to trigger.
     */
    private static final int MINUTES_THRESHOLD = 90;

    /**
     * Notification id.
     */
    private static final int NOTIFICATION_ID = 5;

    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Upcoming Event";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT = "You have an event at %s, why not walk to %s?";

    /**
     * The name of the trigger.
     */
    private String mName;

    /**
     * Android context for handling intents etc.
     */
    private Context mContext;

    /**
     * The data source holder
     */
    private ContextAPI mContextHolder;

    /**
     * The next event in the user's calendar.
     */
    private CalendarEvent mNextEvent;

    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name    the name of the service for this trigger
     * @param context the Android context for calling intents etc
     * @param holder  the data source holder for accessing data
     */
    UpcomingEventTrigger(String name, Context context, ContextAPI holder) {
        super(name, context, holder);
        this.mName = name;
        this.mContext = context;
        this.mContextHolder = holder;
    }

    @Override
    public void notifyUser() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mNextEvent.getStartTime());
        DateFormat sdf = SimpleDateFormat.getTimeInstance();
        String time = sdf.format(cal);
        String location = mNextEvent.getLocation();

        String notificationTitle = String.format(NOTIFICATION_TEXT, time, location);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle(NOTIFICATION_TITLE) // to do
                        .setContentText(notificationTitle);
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());

    }

    @Override
    public Boolean isTriggered() {
        List<CalendarEvent> today = mContextHolder.getTodaysEvents();

        // Basic checking
        if (today == null || today.isEmpty()) {
            return false;
        }

        mNextEvent = today.get(0);

        long now = System.currentTimeMillis();
        long start = mNextEvent.getStartTime();

        // Get the difference between times in minutes, shouldn't be negative but making sure.
        long diff = TimeUnit.MILLISECONDS.toMinutes(Math.abs(start - now));
        return diff <= MINUTES_THRESHOLD;
    }
}
