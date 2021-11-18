package com.cleanup.todoc.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.ui.utils.SortMethod;
import com.cleanup.todoc.ui.utils.TaskComparators;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {

    /**
     * Repository to access TaskDao interface methods
     */
    private final TaskRepository taskRepository;
    /**
     * Executor to access Database in another thread than UI thread
     */
    private final Executor executor;
    /**
     * LiveData containing the list of existing Tasks
     */
    private final LiveData<List<Task>> tasks;
    /**
     * LiveData containing the list of existing Projects
     */
    private final LiveData<List<Project>> projects;

    public MainViewModel(final TaskRepository taskRepository, final ProjectRepository projectRepository, final Executor executor) {
        this.taskRepository = taskRepository;
        this.executor = executor;
        tasks = taskRepository.getAllTasks();
        projects = projectRepository.getAllProjects();
    }

    /**
     * Called to return Tasks list
     *
     * @return tasks : Tasks LiveData
     */
    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    /**
     * Called to return Projects list
     *
     * @return tasks : Projects LiveData
     */
    public LiveData<List<Project>> getProjects() {
        return projects;
    }

    /**
     * Called to insert new Task
     *
     * @param task : task to insert
     */
    public void insertTask(Task task) {
        executor.execute(() -> taskRepository.insertTask(task));
    }

    /**
     * Called to delete a task
     *
     * @param task : task to delete
     */
    public void deleteTask(Task task) {
        executor.execute(() -> taskRepository.deleteTask(task));
    }

    public void deleteAllTasks() {
        executor.execute(taskRepository::deleteAllTask);
    }

    /**
     * Called to sort tasks
     *
     * @param sortMethod : sort method
     * @param tasks      : tasks list to sort
     */
    public void taskSort(SortMethod sortMethod, List<Task> tasks) {
        switch (sortMethod) {
            case ALPHABETICAL:
                Collections.sort(tasks, new TaskComparators.TaskAZComparator());
                break;
            case ALPHABETICAL_INVERTED:
                Collections.sort(tasks, new TaskComparators.TaskZAComparator());
                break;
            case RECENT_FIRST:
                Collections.sort(tasks, new TaskComparators.TaskRecentComparator());
                break;
            case OLD_FIRST:
                Collections.sort(tasks, new TaskComparators.TaskOldComparator());
                break;
        }
    }
}
