package de.westnordost.streetcomplete.quests.surface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.AImageListQuestAnswerFragment
import de.westnordost.streetcomplete.util.TextChangedWatcher
import de.westnordost.streetcomplete.view.Item

class DetailRoadSurfaceForm  : AImageListQuestAnswerFragment<String, DetailSurfaceAnswer>() {

    override val items: List<Item<String>> get() =
        //if (osmElement!!.tags["surface"] == "paved")
        (PAVED_SURFACES + UNPAVED_SURFACES + GROUND_SURFACES).toItems() +
        Item("paved", R.drawable.panorama_surface_paved, R.string.quest_surface_value_paved, null, listOf()) +
        Item("unpaved", R.drawable.panorama_surface_unpaved, R.string.quest_surface_value_unpaved, null, listOf()) +
        Item("ground", R.drawable.panorama_surface_ground, R.string.quest_surface_value_ground, null, listOf())

    override val itemsPerRow = 3

    private var isInExplanationMode = false
    private var switchToExplanationLayout = null
    private var explanationInput: EditText? = null

    private fun setLayout(layoutResourceId: Int) {
        val view = setContentView(layoutResourceId)

        explanationInput = view.findViewById(R.id.explanationInput)
        explanationInput?.addTextChangedListener(TextChangedWatcher { checkIsFormComplete() })
    }

    private val explanation: String get() = explanationInput?.text?.toString().orEmpty().trim()

    override fun isFormComplete(): Boolean {
        return if(isInExplanationMode) {
            explanation.isNotEmpty()
        } else {
            super.isFormComplete()
        }
    }

    override fun onClickOk() {
        // we need to handle fact that we may be in a separate layout
        // that is used to input explanation why surface may not be
        // specified more accurately than just paved/unpaved/ground
        if(isInExplanationMode) {
            // clicked in an explanation mode, therefore
            // user has ready answer prepared that we many use
            applyAnswer(DetailingWhyOnlyGeneric(selectedGenericSurfaceValue, explanation))
        } else {
            // use regular onClickOk call chain
            // used in typical ImageList quest
            super.onClickOk()
        }
    }

    override fun onClickOk(selectedItems: List<String>) {
        // must not happen in isInExplanationMode
        // this onClickOk is called when user is selecting images from
        // list of surfaces

        // this calls comes from onClickOk() in this class,
        // through onClickOk() AImageListQuestAnswerFragment
        // that calls onClickOk with parameters - that is
        // overloaded here

        val value = selectedItems.single()
        if(value == "paved" || value == "unpaved" || value == "ground") {
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.quest_surface_detailed_answer_impossible_confirmation)
                .setPositiveButton(R.string.quest_generic_confirmation_yes) {
                    _, _ -> switchToExplanationLayout(value)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
            return
        }
        applyAnswer(SurfaceAnswer(value))
    }

    private fun switchToExplanationLayout(String value) {
        selectedGenericSurfaceValue = value
        isInExplanationMode = true
        setLayout(R.layout.quest_surface_detailed_answer_impossible)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        isInExplanationMode = savedInstanceState?.getBoolean(IS_IN_EXPLANATION_MODE) ?: false
        setLayout(if (isInExplanationMode) R.layout.quest_surface_detailed_answer_impossible else R.layout.quest_generic_list)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_IN_EXPLANATION_MODE, isInExplanationMode)
    }

    companion object {
        private const val IS_IN_EXPLANATION_MODE = "is_in_explanation_mode"
    }

}
