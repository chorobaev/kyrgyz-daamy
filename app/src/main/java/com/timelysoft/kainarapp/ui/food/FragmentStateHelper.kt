package com.timelysoft.kainarapp.ui.food

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentStateHelper(private val fragmentManager: FragmentManager) {

    private val fragmentSavedStates = mutableMapOf<Int,Fragment.SavedState?>()

    fun restoreState(fragment: Fragment, key: Int){
        fragmentSavedStates[key]?.let {savedState ->
            if (!fragment.isAdded){
                fragment.setInitialSavedState(savedState)
            }
        }
    }
    fun saveState(fragment: Fragment, key: Int) {
        // We can't save the state of a Fragment that isn't added to a FragmentManager.
        if (fragment.isAdded ?: false) {
            fragmentSavedStates[key] = fragmentManager.saveFragmentInstanceState(fragment)
        }
    }
}