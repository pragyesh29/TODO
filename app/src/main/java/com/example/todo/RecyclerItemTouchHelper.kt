package com.example.todo

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.Adapter.TodoAdapter

class RecyclerItemTouchHelper(private val adapter: TodoAdapter): ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
){
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Used to get position of item on which I am working upon
        val position = viewHolder.absoluteAdapterPosition
        if(direction == ItemTouchHelper.LEFT){
            val builder = AlertDialog.Builder(adapter.context)
            builder.setTitle("Delete Task")
            builder.setTitle("Are you sure you want to delete task?")
            builder.setPositiveButton("Yes") { _, _ ->
                adapter.deleteItem(position)
            }
            builder.setNegativeButton("No", ){ _, _ ->
                    adapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
            }
            builder.create().show()
        }else{
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val icon: Drawable
        val background: ColorDrawable

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20
        if(dX > 0){
            // When you will swipe to right i.e. for edit
            icon = ContextCompat.getDrawable(adapter.context, R.drawable.baseline_edit_note_24)!!
            background = ColorDrawable(ContextCompat.getColor(adapter.context, R.color.blue_theme))
        }else{
            icon = ContextCompat.getDrawable(adapter.context, R.drawable.baseline_remove_circle_24)!!
            background = ColorDrawable(ContextCompat.getColor(adapter.context, R.color.red))
        }

        val iconMargin = (itemView.height - icon.intrinsicHeight)/2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight)/2
        val iconBottom = iconTop + icon.intrinsicHeight

        if(dX > 0){
            // When you will swipe right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom)
        }else if(dX < 0){
            // When you will swipe to left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset, itemView.top, itemView.right, itemView.bottom)
        }else{
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
        icon.draw(c)
    }

}