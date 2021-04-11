package io.flaterlab.kyrgyzdaamy.ui.food

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class CustomPositionItemDecoration(private val dividerDrawable: Drawable) :
    RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        rect: Rect,
        view: View,
        parent: RecyclerView,
        s: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
            .let { if (it == RecyclerView.NO_POSITION) return else it }
        rect.right =
            if (position % 2 == 0) 2
            else dividerDrawable.intrinsicWidth
    }


    private fun convertDpToPx(context: Context): Float {
        return  context.resources.displayMetrics.density
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val leftWithMargin: Float = convertDpToPx(parent.context)
        val right: Int = parent.width

        val childCount: Int = parent.childCount
        for (i in 0 until childCount-1) {
            val child: View = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, dividerDrawable.bounds)
            val bottom: Int =
                dividerDrawable.bounds.bottom + ViewCompat.getTranslationY(child).roundToInt()

            val top: Int = bottom - dividerDrawable.intrinsicHeight
            dividerDrawable.setBounds(leftWithMargin.toInt(), top, right, bottom)
            dividerDrawable.draw(canvas)
        }
        canvas.restore()

    }

}