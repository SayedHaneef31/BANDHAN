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
        taskAdapter = ChecklistAdapter { task, isChecked ->
            // This is the callback for when a checkbox is toggled
            task.isCompleted = isChecked
            lifecycleScope.launch {
                db.taskDao().updateTask(task)
            }
        }
        binding.recyclerChecklist.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }

    private fun observeTasks() {
        // Collect tasks from the database and submit to the adapter
        lifecycleScope.launch {
            db.taskDao().getAllTasks().collect { tasks ->
                taskAdapter.submitList(tasks)
            }
        }
    }

    private fun setupListeners() {
        binding.btnAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
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