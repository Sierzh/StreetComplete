package de.westnordost.streetcomplete.data.osm.changes

interface ElementEditsSource {
    /** Interface to be notified of new or updated OSM elements */
    interface Listener {
        fun onAddedEdit(edit: ElementEdit)
        fun onSyncedEdit(edit: ElementEdit)
        fun onDeletedEdit(edit: ElementEdit)
    }

    /** Count of unsynced edits that count towards the statistics. That is, reverts of edits
     *  count negative */
    fun getPositiveUnsyncedCount(): Int

    /** Count of unsynced a.k.a to-be-uploaded edits */
    fun getUnsyncedCount(): Int

    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
}
