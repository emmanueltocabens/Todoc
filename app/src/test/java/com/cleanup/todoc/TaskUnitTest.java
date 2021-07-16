package com.cleanup.todoc;


import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.data.TodocDataBase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
@RunWith(JUnit4.class)
public class TaskUnitTest {

    private TodocDataBase database;

    private final Task DEMO_TASK_1 = new Task(1,"DEMO TASK 1",new Date().getTime());
    private final Task DEMO_TASK_2 = new Task(2,"DEMO TASK 2",new Date().getTime());
    private final Task DEMO_TASK_3 = new Task(3,"DEMO TASK 3",new Date().getTime());

    @Before
    public void initDb() throws InterruptedException {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                TodocDataBase.class)
                .allowMainThreadQueries()
                .build();
        clearTasks();
    }

    @After
    public void closeDb() throws InterruptedException {
        clearTasks();
        database.close();
    }

    @Test
    public void test_insertAndDelete_task() throws InterruptedException {
        database.taskDAO().insert(DEMO_TASK_1);
        database.taskDAO().insert(DEMO_TASK_2);
        assertTrue(LiveDataTestUtil.getValue(database.taskDAO().getAllTasks()).containsAll(Arrays.asList(DEMO_TASK_1,DEMO_TASK_2)));
        database.taskDAO().delete(DEMO_TASK_1);
        assertTrue(LiveDataTestUtil.getValue(database.taskDAO().getAllTasks()).contains(DEMO_TASK_2));
        assertFalse(LiveDataTestUtil.getValue(database.taskDAO().getAllTasks()).contains(DEMO_TASK_1));
    }

    @Test
    public void test_getTaskList() throws InterruptedException {
        List<Task> expected = Arrays.asList(DEMO_TASK_1,DEMO_TASK_2,DEMO_TASK_3);
        database.taskDAO().insert(DEMO_TASK_1);
        database.taskDAO().insert(DEMO_TASK_2);
        database.taskDAO().insert(DEMO_TASK_3);
        List<Task> result = LiveDataTestUtil.getValue(database.taskDAO().getAllTasks());
        assertTrue(result.containsAll(expected));
    }

    @Test
    public void test_projects() throws InterruptedException {
        final Task task1 = new Task(1, "task 1", new Date().getTime());
        final Task task2 = new Task(2, "task 2", new Date().getTime());
        final Task task3 = new Task(3, "task 3", new Date().getTime());
        final Task task4 = new Task(4, "task 4", new Date().getTime());
        List<Project> projectList = LiveDataTestUtil.getValue(database.projectDAO().getAllProjects());
        assertEquals("Projet Tartampion", task1.getProject(projectList).getName());
        assertEquals("Projet Lucidia", task2.getProject(projectList).getName());
        assertEquals("Projet Circus", task3.getProject(projectList).getName());
        assertNull(task4.getProject(projectList));
    }

    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(1, "aaa", 123);
        final Task task2 = new Task(2, "zzz", 124);
        final Task task3 = new Task(3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskAZComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    @Test
    public void test_za_comparator() {
        final Task task1 = new Task(1, "aaa", 123);
        final Task task2 = new Task(2, "zzz", 124);
        final Task task3 = new Task(3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskZAComparator());

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_recent_comparator() {
        final Task task1 = new Task(1, "aaa", 123);
        final Task task2 = new Task(2, "zzz", 124);
        final Task task3 = new Task(3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskRecentComparator());

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_old_comparator() {
        final Task task1 = new Task(1, "aaa", 123);
        final Task task2 = new Task(2, "zzz", 124);
        final Task task3 = new Task(3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }

    public void clearTasks() throws InterruptedException {
        List<Task> taskList = LiveDataTestUtil.getValue(database.taskDAO().getAllTasks());
        for(Task tmp : taskList){
            database.taskDAO().delete(tmp);
        }
    }
}