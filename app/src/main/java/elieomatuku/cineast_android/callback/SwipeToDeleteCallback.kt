package elieomatuku.cineast_android.callback


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import elieomatuku.cineast_android.adapter.MovieAdapter
import android.graphics.drawable.ColorDrawable
import elieomatuku.cineast_android.R
import timber.log.Timber


class SwipeToDeleteCallback(val adapter: MovieAdapter): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val background: ColorDrawable? = ColorDrawable(Color.RED)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }


    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 5

        val paint = Paint()
        paint.setColor(Color.WHITE)
        paint.setTextSize(25f)
        paint.setTextAlign(Paint.Align.CENTER)

        val inbox = itemView.context.resources.getString(R.string.delete_label)

        if (dX < 0) { // Swiping to the right
            background?.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom)
            Timber.d("x value: ${itemView.right + dX}")

        } else { // view is unSwiped
            background?.setBounds(0, 0, 0, 0)
        }

        background?.draw(c)

        if (dX < 0) {
            c.drawText(inbox, itemView.right + dX + (100), (itemView.top+ itemView.height / 2).toFloat(), paint);

        } else {
            c.drawText(inbox, 0f, 0f, paint);
        }
    }
}


