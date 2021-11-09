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
import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.ui.ViewModelFactory;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    private SortMethod sortMethod = SortMethod.NONE;

    private TasksAdapter adapter;

    // UI components
    private RecyclerView listTasks;
    private TextView lblNoTasks;
    private AlertDialog dialog = null;
    private EditText dialogEditText = null;
    private Spinner dialogSpinner = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewModel initialization
        ViewModelProvider.Factory factory = new ViewModelFactory(new Repository(getApplication()));
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        // UI components links
        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);

        // RecyclerView initialization
        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Adapter instantiation & implement onDeleteTaskListener interface
        adapter = new TasksAdapter(viewModel.getAllTasks().getValue(), viewModel.getAllProjects(), task -> {
            viewModel.deleteTask(task);
            updateTasks();
        });

        // Observe tasks then update UI
        viewModel.getAllTasks().observe(this, list -> updateTasks());

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
                updateTasks();
                return true;
            case R.id.filter_alphabetical_inverted:
                sortMethod = SortMethod.ALPHABETICAL_INVERTED;
                updateTasks();
                return true;
            case R.id.filter_oldest_first:
                sortMethod = SortMethod.OLD_FIRST;
                updateTasks();
                return true;
            case R.id.filter_recent_first:
                sortMethod = SortMethod.RECENT_FIRST;
                updateTasks();
                return true;
            default:
                updateTasks();
                return super.onOptionsItemSelected(item);
        }
    }

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
                // TODO: Replace this by id of persisted task

                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                viewModel.insertTask(task);
                updateTasks();
                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();
        dialog.show();
        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);
        populateDialogSpinner();
    }

    private void updateTasks() {
        // If tasks list is empty show illusion
        if (viewModel.getAllTasks().getValue().size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            // Else show tasks list
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
        }
        // Update adapter with latest tasks list
        adapter.updateTasks(viewModel.getAllTasks().getValue());
        listTasks.setAdapter(adapter);
        // Sort tasks
        viewModel.taskSort(sortMethod, viewModel.getAllTasks().getValue());
    }

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

    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, viewModel.getAllProjects());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogSpinner.setAdapter(adapter);
    }
}
