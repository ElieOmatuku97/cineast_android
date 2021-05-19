package elieomatuku.cineast_android.utils

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorator(private val mDivider: Drawable?) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)

        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 1 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            mDivider?.let {
                val dividerTop = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + it.intrinsicHeight

                it.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                it.draw(canvas)
            }
        }
    }
}
