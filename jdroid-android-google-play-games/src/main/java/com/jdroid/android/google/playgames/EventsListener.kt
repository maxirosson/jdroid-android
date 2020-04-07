package com.jdroid.android.google.playgames

import com.google.android.gms.games.event.Event

interface EventsListener {

    fun onEventsLoaded(events: List<Event>)
}
