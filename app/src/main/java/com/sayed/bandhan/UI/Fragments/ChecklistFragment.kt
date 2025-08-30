package com.sayed.bandhan.UI.Fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sayed.bandhan.Data.AppDatabase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sayed.bandhan.Data.Task
import com.sayed.bandhan.R
import com.sayed.bandhan.databinding.FragmentChecklistBinding
import com.sayed.bandhan.utils.ChecklistAdapter
import kotlinx.coroutines.launch


class ChecklistFragment : Fragment() {

    private var _binding: FragmentChecklistBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private lateinit var taskAdapter: ChecklistAdapter
    private var isInitialLoad = true
    private var previousTaskCount = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())
        setupRecyclerView()
        observeTasks()
        setupListeners()
    }


    private fun setupRecyclerView() {
        taskAdapter = ChecklistAdapter(
            onTaskChecked = { task, isChecked ->
                task.isCompleted = isChecked
                lifecycleScope.launch {
                    db.taskDao().updateTask(task)
                }
            },
            onTaskEdit = { taskToEdit ->
                showEditTaskDialog(taskToEdit)
            }
        )
        binding.recyclerChecklist.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }
    private fun showEditTaskDialog(taskToEdit: Task) {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_update_task, null)
        val input = view.findViewById<EditText>(R.id.etTaskDescription)
        input.setText(taskToEdit.description)

        builder.setView(view)
            .setTitle("Edit Task")
            .setPositiveButton("Save") { dialog, _ ->
                val updatedDescription = input.text.toString().trim()
                if (updatedDescription.isNotEmpty()) {
                    // Update the task in the database
                    val updatedTask = taskToEdit.copy(description = updatedDescription)
                    lifecycleScope.launch {
                        db.taskDao().updateTask(updatedTask)
                    }
                } else {
                    Toast.makeText(context, "Task cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun observeTasks() {
        // Collect tasks from the database and submit to the adapter
        lifecycleScope.launch {
            db.taskDao().getAllTasks().collect { tasks ->
                val isNewTaskAdded = tasks.size > previousTaskCount
                taskAdapter.submitList(tasks) {
                    if (isInitialLoad) {

                        binding.recyclerChecklist.scrollToPosition(0)
                        isInitialLoad = false
                    } else if (isNewTaskAdded) {
                        binding.recyclerChecklist.smoothScrollToPosition(tasks.size - 1)
                    }
                    previousTaskCount = tasks.size
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_task, null)
        val input = view.findViewById<EditText>(R.id.etTaskDescription)

        builder.setView(view)
            .setTitle("Add New Task")
            .setPositiveButton("Add") { dialog, _ ->
                val taskDescription = input.text.toString().trim()
                if (taskDescription.isNotEmpty()) {
                    val newTask = Task(description = taskDescription)
                    lifecycleScope.launch {
                        db.taskDao().insertTask(newTask)
                    }
                } else {
                    Toast.makeText(context, "Task cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}