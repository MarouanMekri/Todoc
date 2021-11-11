package com.cleanup.todoc.ui.list;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    /**
     * The list of tasks the adapter deals with
     */
    @NonNull
    private List<Task> tasks;

    /**
     * The list of projects the adapter deals with
     */
    @NonNull
    private List<Project> projects;

    /**
     * The listener for when a task needs to be deleted
     */
    @NonNull
    private final DeleteTaskListener deleteTaskListener;

    /**
     * Instantiates a new TasksAdapter.
     *
     * @param tasks the list of tasks the adapter deals with to set
     * @param projects the list of projects the adapter deals with to set
     * @param deleteTaskListener the listener who delete task
     */
    public TasksAdapter(@NonNull final List<Task> tasks,@NonNull final List<Project> projects, @NonNull final DeleteTaskListener deleteTaskListener) {
        this.tasks = tasks;
        this.projects = projects;
        this.deleteTaskListener = deleteTaskListener;
    }

    /**
     * Updates the list of tasks the adapter deals with.
     *
     * @param tasks the list of tasks the adapter deals with to set
     */
    public void updateTasks(@NonNull final List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    /**
     * Updates the list of projects the adapter deals with.
     *
     * @param projects the list of projects the adapter deals with to set
     */
    public void updateProjects(@NonNull final List<Project> projects) {
        this.projects = projects;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false), deleteTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /**
     * <p>ViewHolder for task items in the tasks list</p>
     */
    class TaskViewHolder extends RecyclerView.ViewHolder {

        /**
         * The circle icon showing the color of the project
         */
        private final AppCompatImageView imgProject;
        /**
         * The delete icon
         */
        private final AppCompatImageView imgDelete;

        /**
         * The TextView displaying the name of the task
         */
        private final TextView lblTaskName;
        /**
         * The TextView displaying the name of the project
         */
        private final TextView lblProjectName;

        /**
         * The delete listener interface
         */
        private final DeleteTaskListener deleteTaskListener;

        TaskViewHolder(@NonNull View itemView, @NonNull DeleteTaskListener deleteTaskListener) {
            super(itemView);

            this.deleteTaskListener = deleteTaskListener;

            imgProject = itemView.findViewById(R.id.img_project);
            lblTaskName = itemView.findViewById(R.id.lbl_task_name);
            lblProjectName = itemView.findViewById(R.id.lbl_project_name);
            imgDelete = itemView.findViewById(R.id.img_delete);

            imgDelete.setOnClickListener(view -> {
                final Object tag = view.getTag();
                if (tag instanceof Task) {
                    TaskViewHolder.this.deleteTaskListener.onDeleteTask((Task) tag);
                }
            });
        }

        /**
         * Binds a task to the item view.
         *
         * @param task : the task to bind in the item view
         */
        void bind(Task task) {
            lblTaskName.setText(task.getName());
            imgDelete.setTag(task);

            final Project project = getAssociatedProject(task.projectId);

            if (project != null) {
                imgProject.setSupportImageTintList(ColorStateList.valueOf(project.getColor()));
                lblProjectName.setText(project.getName());
            } else {
                imgProject.setVisibility(View.INVISIBLE);
                lblProjectName.setText("");
            }
        }

        /**
         * Get Project of an associated Task, by searching in Projects List using project id
         * @param projectId : id of the Project to search
         * @return : Associated Project
         */
        private Project getAssociatedProject(long projectId) {
            boolean found = false;
            int index = 0;
            Project project = null;

            while (!found && index < projects.size()) {
                if (projects.get(index).getId() == projectId) {
                    found = true;
                    project = projects.get(index);
                } else {
                    index++;
                }
            }
            return project;
        }
    }
}
