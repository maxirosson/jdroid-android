package com.jdroid.android.google.playgames;

import com.google.android.gms.games.event.Event;

import java.util.List;

public interface EventsListener {

	void onEventsLoaded(List<Event> events);
}
