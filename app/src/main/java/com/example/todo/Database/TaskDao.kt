package com.example.todo.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.Model.TodoModel

@Dao
interface TaskDao {
    @Insert
    fun insertTask(taskEntity: TaskEntity)

    @Query("DELETE FROM Tasks WHERE 1=1")
    fun deleteAllTasks()

    @Query("DELETE FROM Tasks WHERE id = :taskid")
    fun deleteTask(taskid: Int)

    @Query("SELECT * FROM Tasks ORDER BY id DESC")
    fun getAllTasks():MutableList<TaskEntity>

    @Query("SELECT * FROM Tasks WHERE id = :taskid")
    fun getTaskFromId(taskid: Int): TaskEntity

    @Query("UPDATE Tasks SET task_desc = :taskDesc WHERE id = :taskId")
    fun updateTask(taskId: Int, taskDesc: String)

    @Query("UPDATE Tasks SET task_status = :status WHERE id = :taskId")
    fun updateStatus(taskId: Int, status: Boolean)

    @Query("SELECT * FROM tasks ORDER BY id DESC LIMIT 1")
    fun getLastTask(): TaskEntity
}