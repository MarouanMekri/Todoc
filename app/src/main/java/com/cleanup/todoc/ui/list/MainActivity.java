package com.cleanup.todoc.ui.list;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.ui.ViewModelFactory;
import com.cleanup.todoc.ui.utils.SortMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeleteTaskListener {
    /**
     * List of all current projects of the application
     */
    @NonNull
    private final List<Project> projects = new ArrayList<>();
    /**
     * List of all current tasks of the application
     */
    @NonNull
    private final List<Task> tasks = new ArrayList<>();
    /**
     * The adapter which handles the list of tasks
     */
    private final TasksAdapter adapter = new TasksAdapter(tasks, projects, this);
    /**
     * ViewModel
     */
    private MainViewModel viewModel;
    /**
     * The sort method to be used to display tasks
     */
    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;
    /**
     * UI components
     */
    private RecyclerView listTasks;
    private TextView lblNoTasks;
    private AlertDialog dialog = null;
    private EditText dialogEditText = null;
    private Spinner dialogSpinner = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI components casting
        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);

        // ViewModel initialization
        ViewModelProvider.Factory factory = new ViewModelFactory(getApplicationContext());
        viewModel = factory.create(MainViewModel.class);

        // RecyclerView initialization
        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);

        // Observe tasks then update UI & main list
        viewModel.getTasks().observe(this, list -> {
            tasks.clear();
            tasks.addAll(list);
            updateTasks();
        });

        // Observe projects then update main list
        viewModel.getProjects().observe(this, list -> {
            projects.clear();
            projects.addAll(list);
            adapter.updateProjects(projects);
        });

        // Add task button
        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_alphabetical:
                sortMethod = SortMethod.ALPHABETICAL;
                break;
            case R.id.filter_alphabetical_inverted:
                sortMethod = SortMethod.ALPHABETICAL_INVERTED;
                break;
            case R.id.filter_oldest_first:
                sortMethod = SortMethod.OLD_FIRST;
                break;
            case R.id.filter_recent_first:
                sortMethod = SortMethod.RECENT_FIRST;
                break;
        }
        updateTasks();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Implementation of onDeleteTaskListener interface
     *
     * @param task : task to be deleted
     */
    @Override
    public void onDeleteTask(Task task) {
        viewModel.deleteTask(task);
    }

    /**
     * Called after user confirmation that task form is completed. Create new Task
     *
     * @param dialogInterface : Dialog created before in getAddTaskDialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();
            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }
            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );
                // Create new task && update UI then close dialog
                viewModel.insertTask(task);
                updateTasks();
                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else {
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Displays Task Dialog
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();
        dialog.show();
        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);
        populateDialogSpinner();
    }

    /**
     * Updates the list of tasks and background elements in the UI
     */
    private void updateTasks() {
        // Show background image and message
        if (tasks.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            // Hide background image and message
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            // Sort tasks
            viewModel.taskSort(sortMethod, tasks);
        }
        // Update adapter with latest tasks list
        adapter.updateTasks(tasks);
    }

    /**
     * Called to create an Alertdialog
     *
     * @return dialog : AlertDialog to show in showAddTaskDialog
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);
        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });
        dialog = alertBuilder.create();
        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onPositiveButtonClick(dialog));
        });
        return dialog;
    }

    /**
     * <p>Sets the data of the Spinner with projects to associate to a new task</p>
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projects);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogSpinner.setAdapter(arrayAdapter);
    }
}
