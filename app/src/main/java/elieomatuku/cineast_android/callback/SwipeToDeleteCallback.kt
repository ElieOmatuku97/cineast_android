package elieomatuku.cineast_android.callback

import android.graphics.Canvas
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import elieomatuku.cineast_android.adapter.MovieAdapter
import android.graphics.drawable.ColorDrawable








class SwipeToDeleteCallback(val adapter: MovieAdapter): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val background: ColorDrawable? = ColorDrawable(Color.RED)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 5//20

        if (dX < 0) { // Swiping to the right
            background?.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom)

        } else { // view is unSwiped
            background?.setBounds(0, 0, 0, 0)
        }
        background?.draw(c)
    }
}


