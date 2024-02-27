package com.example.todo.Adapter

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.AddNewTask
import com.example.todo.Database.TaskDatabase
import com.example.todo.Database.TaskEntity
import com.example.todo.MainActivity
import com.example.todo.Model.TodoModel
import com.example.todo.R

class TodoAdapter(val context: MainActivity, val database: TaskDatabase): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private var todoList: MutableList<TaskEntity> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_task, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val singleItem = todoList[position]
        holder.taskCheckbox.text = singleItem.task
        holder.taskCheckbox.isChecked = singleItem.status

        holder.taskCheckbox.setOnClickListener{
            if(singleItem.status){
                database.bookDao().updateStatus(singleItem.id, false)
            }else{
                database.bookDao().updateStatus(singleItem.id, true)
            }
        }
    }

    fun setTaskList(newList: MutableList<TaskEntity>){
        todoList.clear()
        todoList.addAll(newList)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        val item = todoList[position]
        database.bookDao().deleteTask(item.id)
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int){
        val item = todoList[position]
        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("task", item.task)
        val frag = AddNewTask()
        frag.arguments = bundle
        frag.show(context.supportFragmentManager, "BottomSheetDialog")
    }

    class TodoViewHolder(view: View): RecyclerView.ViewHolder(view){
        val taskCheckbox = view.findViewById<CheckBox>(R.id.checkBox)
    }
}