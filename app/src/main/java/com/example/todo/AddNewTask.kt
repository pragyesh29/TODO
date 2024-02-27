package com.example.todo

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.todo.Adapter.TodoAdapter
import com.example.todo.Database.TaskDao
import com.example.todo.Database.TaskDatabase
import com.example.todo.Database.TaskEntity
import com.example.todo.Model.TodoModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask: BottomSheetDialogFragment(){
    private lateinit var newTaskText: EditText
    private lateinit var newTaskButton: Button
    private lateinit var taskDatabase: TaskDatabase

    var editOrInsertPerformed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_task, container, false)
        taskDatabase = TaskDatabase.getInstance(requireContext())
        newTaskText = view.findViewById(R.id.newTaskText)
        newTaskButton = view.findViewById(R.id.newTaskButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if BottomSheet is to be opened for updating a task or creating a new task. By default we are assuming task is to be created.
        var isUpdate = false

        // If task is needed to be updated then appropriate changes i.e. updated text Value is passed from fragment through Bundle passing
        val bundle = arguments

        newTaskText.requestFocus()

        // If bundle does have something means we need to update the task with updated value contained in bundle
        if(bundle != null){
            isUpdate = true
            val updatedTask = bundle.getString("task")
            newTaskText.setText(updatedTask)

            // If updatedTask has something then enable the save button
            if(!updatedTask.isNullOrEmpty()){
                // Enable the button and change the color too.
                newTaskButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_theme))
            }
        }

        // Add clickListener to editext i.e. newTaskText so that as soon as something is typed button gets enabled
        newTaskText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Triggered when something is changed maybe some character typed or deleted any sort of change
                // If noting is present in editext then disable
                if(s.toString() == ""){
                    newTaskButton.isEnabled = false
                    newTaskButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                    newTaskButton.setTextColor(Color.GRAY)
                }else{
                    newTaskButton.isEnabled = true
                    newTaskButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_theme))
                    newTaskButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }

        })

        val finalIsUpdate = isUpdate
        // Add clickListener to button
        newTaskButton.setOnClickListener{
            // Check if we are trying to create new task or updating a already created task
            val text = newTaskText.text.toString()
            if(text.isNotEmpty()){
                if(finalIsUpdate){
                    if(bundle != null){
                        taskDatabase.bookDao().updateTask(bundle.getInt("id"), text)
                        editOrInsertPerformed = true
                    }
                }else{
                    val newTask = TaskEntity(status = false, task = text)
                    taskDatabase.bookDao().insertTask(newTask)
                    editOrInsertPerformed = true
                }
                newTaskText.text.clear()
            }else{
                Toast.makeText(requireContext(), "Task cannot be empty",Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(editOrInsertPerformed){
            val activity = activity
            if(activity is DialogCloseListener){
                activity.handleDialogClose(dialog)
            }
        }
    }
}