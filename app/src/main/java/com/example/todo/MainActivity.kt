package com.example.todo

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.Adapter.TodoAdapter
import com.example.todo.Database.TaskDatabase
import com.example.todo.Database.TaskEntity
import com.example.todo.Model.TodoModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections

class MainActivity : AppCompatActivity(), DialogCloseListener{
    lateinit var todoList: MutableList<TaskEntity>
    lateinit var taskAdapter: TodoAdapter
    lateinit var database: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)

        val bottomSheetFragment =AddNewTask()
        fab.setOnClickListener{
            bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
        }

        todoList = ArrayList()
        database = TaskDatabase.getInstance(this)
        todoList = database.bookDao().getAllTasks()


        val recyclerview = findViewById<RecyclerView>(R.id.tasksRecyclerView)
        taskAdapter = TodoAdapter(this, database)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = taskAdapter
        taskAdapter.setTaskList(todoList)

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(taskAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerview)
    }

    override fun handleDialogClose(dialog: DialogInterface) {
//        TODO(If Item is inserted new just update the recyclerView's adapter to render it with notifyItemInserted() otherwise it means item is updated so just re-render entire tasks on screen with notifyItemChanged())
//        TODO(If item is inserted we are re-rendering entire taskList again because we don't have the position of the item which is updated otherwise we could have used notifyItemChanged(position) which was fast.)
        todoList = database.bookDao().getAllTasks()
        for (i in todoList){
            Log.d("${i.id} ${i.status}", "task")
        }
        taskAdapter.setTaskList(todoList)
    }
}