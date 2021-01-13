package com.timelysoft.kainarapp.utils

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.timelysoft.kainarapp.service.model2.response2.StreetResponse
import java.util.*
import kotlin.collections.ArrayList

class AutoCompleteAdapter(
    context: Context,
    @LayoutRes resource: Int,
    @IdRes textViewResourceId: Int = 0,
    internal var items: List<StreetResponse> = listOf()
) : ArrayAdapter<StreetResponse>(context, resource, textViewResourceId, items) {


    internal var tempItems: MutableList<StreetResponse> = mutableListOf()
    internal var suggestions: MutableList<StreetResponse> = mutableListOf()

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private var filter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                suggestions.clear()
                tempItems.forEach {
                    if (it.toString().toLowerCase(Locale.getDefault()).contains(
                            constraint.toString().toLowerCase(
                                Locale.getDefault()
                            )
                        )
                    ) {
                        suggestions.add(it)
                    }
                }
                try {
                    suggestions.add(
                        StreetResponse(
                            cityName = "Bishkek", id = -1, name = "Указать свой адрес..."
                        )
                    )
                }catch (e: Exception){

                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            try {
                val filterList = results.values as? List<*>
                if (results.count > 0) {
                    clear()
                    filterList?.forEach {
                        add(it as StreetResponse?)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
    init {
        tempItems = items.toMutableList()
        suggestions = ArrayList()
    }


    override fun getFilter(): Filter {
        return filter
    }
}