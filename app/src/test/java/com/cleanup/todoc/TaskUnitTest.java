package com.cleanup.todoc;

import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.ui.utils.TaskComparators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TaskUnitTest {

    /**
     * - Initializes 4 Task objects
     * - Check values of each item
     */
    @Test
    public void test_task_objects() {
        // Create tasks objects
        final Task task1 = new Task(1, "task 1", new Date().getTime());
        final Task task2 = new Task(2, "task 2", new Date().getTime());
        final Task task3 = new Task(3, "task 3", new Date().getTime());
        final Task task4 = new Task(4, "task 4", new Date().getTime());

        // Compare IDs
        assertEquals(1, task1.getProjectId());
        assertEquals(2, task2.getProjectId());
        assertEquals(3, task3.getProjectId());
        assertEquals(4, task4.getProjectId());

        // Compare Names
        assertEquals("task 1", task1.getName());
        assertEquals("task 2", task2.getName());
        assertEquals("task 3", task3.getName());
        assertEquals("task 4", task4.getName());
    }

    /**
     * - Initializes 3 Task objects
     * - Initializes an arrayList containing theses Tasks
     * - Sort the arraylist using the TaskAZComparator method from TaskComparators class
     * - Check if sort is correctly done
     */
    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(1, "aaa", 123);
        final Task task2 = new Task(2, "zzz", 124);
        final Task task3 = new Task(3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new TaskComparators.TaskAZComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    /**
     * - Initializes 3 Task objects
     * - Initializes an arrayList containing theses Tasks
     * - Sort the arraylist using the TaskZAComparator method from TaskComparators class
     * - Check if sort is correctly done
     */
    @Test
    public void test_za_comparator() {
        final Task task1 = new Task(1, "aaa", 123);
        final Task task2 = new Task(2, "zzz", 124);
        final Task task3 = new Task(3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new TaskComparators.TaskZAComparator());

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    /**
     * - Initializes 3 Task objects
     * - Initializes an arrayList containing theses Tasks
     * - Sort the arraylist using the TaskRecentComparator method from TaskComparators class
     * - Check if sort is correctly done
     */
    @Test
    public void test_recent_comparator() {
        final Task task1 = new Task(1, "aaa", 123);
        final Task task2 = new Task(2, "zzz", 124);
        final Task task3 = new Task(3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new TaskComparators.TaskRecentComparator());

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    /**
     * - Initializes 3 Task objects
     * - Initializes an arrayList containing theses Tasks
     * - Sort the arraylist using the TaskOldComparator method from TaskComparators class
     * - Check if sort is correctly done
     */
    @Test
    public void test_old_comparator() {
        final Task task1 = new Task(1, "aaa", 123);
        final Task task2 = new Task(2, "zzz", 124);
        final Task task3 = new Task(3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new TaskComparators.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }
}