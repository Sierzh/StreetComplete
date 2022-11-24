package de.westnordost.streetcomplete.osm.cycleway_separate

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.osm.cycleway_separate.SeparateCycleway.*
import de.westnordost.streetcomplete.view.image_select.Item

fun SeparateCycleway.asItem(isLeftHandTraffic: Boolean) =
    Item(this, getIconResId(isLeftHandTraffic), titleResId)

private val SeparateCycleway.titleResId: Int get() = when (this) {
    NONE ->           R.string.separate_cycleway_no
    ALLOWED ->        R.string.separate_cycleway_allowed
    NON_SEGREGATED -> R.string.separate_cycleway_non_segregated
    SEGREGATED ->     R.string.separate_cycleway_segregated
    EXCLUSIVE ->      R.string.separate_cycleway_exclusive
    WITH_SIDEWALK ->  R.string.separate_cycleway_with_sidewalk
}

private fun SeparateCycleway.getIconResId(isLeftHandTraffic: Boolean): Int = when (this) {
    NONE ->           R.drawable.ic_separate_cycleway_no
    ALLOWED ->        R.drawable.ic_separate_cycleway_allowed
    NON_SEGREGATED -> R.drawable.ic_separate_cycleway_not_segregated
    SEGREGATED ->     if (isLeftHandTraffic) R.drawable.ic_separate_cycleway_segregated_l
                      else                   R.drawable.ic_separate_cycleway_segregated
    EXCLUSIVE ->      R.drawable.ic_separate_cycleway_exclusive
    WITH_SIDEWALK ->  if (isLeftHandTraffic) R.drawable.ic_separate_cycleway_with_sidewalk_l
                      else                   R.drawable.ic_separate_cycleway_with_sidewalk
}
