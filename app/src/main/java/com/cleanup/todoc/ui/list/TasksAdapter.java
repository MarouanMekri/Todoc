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

    @NonNull
    private List<Task> tasks;
    public List<Project> projects;

    @NonNull
    private final DeleteTaskListener deleteTaskListener;

    public TasksAdapter(@NonNull final List<Task> tasks, @NonNull final List<Project> projects, @NonNull final DeleteTaskListener deleteTaskListener) {
        this.tasks = tasks;
        this.projects = projects;
        this.deleteTaskListener = deleteTaskListener;
    }

    public void updateTasks(@NonNull final List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
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

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView imgProject;
        private final AppCompatImageView imgDelete;

        private final TextView lblTaskName;
        private final TextView lblProjectName;

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

        void bind(Task task) {
            lblTaskName.setText(task.getName());
            imgDelete.setTag(task);
            imgProject.setSupportImageTintList(ColorStateList.valueOf(projects.get((int)task.projectId - 1).getColor()));
            lblProjectName.setText(projects.get((int)task.projectId - 1).getName());
        }
    }
}
