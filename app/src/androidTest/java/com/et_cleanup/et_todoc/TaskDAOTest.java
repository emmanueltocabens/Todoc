package com.et_cleanup.et_todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;

import com.et_cleanup.et_todoc.data.TodocDataBase;
import com.et_cleanup.et_todoc.model.Project;
import com.et_cleanup.et_todoc.model.Task;
import com.et_cleanup.et_todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class TaskDAOTest {

    @Rule
    public TestWatcher instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TodocDataBase database;

    private Task DEMO_TASK_1;
    private Task DEMO_TASK_2;
    private Task DEMO_TASK_3;

    private final RoomDatabase.Callback prepopulate = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                database.projectDAO().insertAll(
                        new Project("Projet Tartampion", 0xFFEADAD1, 0),
                        new Project("Projet Lucidia", 0xFFB4CDBA, 1),
                        new Project("Projet Circus", 0xFFA3CED2, 2)
                );
            });

        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3,4){
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //
        }
    };

    @Before
    public void initDb() throws InterruptedException {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                TodocDataBase.class)
                .allowMainThreadQueries()
                .addCallback(prepopulate)
                .addMigrations(MIGRATION_3_4)
                .build();
        clearTasks();
    }

    @After
    public void closeDb() throws InterruptedException {
        clearTasks();
        database.close();
    }

    @Test
    public void insert_and_delete(){

    }

    @Test
    public void getProjects() throws InterruptedException {
        List<Project> actual = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects());
    }



    @Test
    public void test_insertAndDelete_task() throws InterruptedException {
        clearTasks();
        DEMO_TASK_1 = getNewTask(0);
        DEMO_TASK_2 = getNewTask(1);
        DEMO_TASK_3 = getNewTask(1);

        database.taskDAO().insert(DEMO_TASK_1);
        database.taskDAO().insert(DEMO_TASK_2);
        List<Task> list = LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks());
        //assertEquals(DEMO_TASK_1.getId(),LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks()).get(0).getId());

        assertTrue(list.contains(DEMO_TASK_1));
        assertTrue(list.contains(DEMO_TASK_2));
        assertTrue(list.containsAll(Arrays.asList(DEMO_TASK_1,DEMO_TASK_2)));

        database.taskDAO().delete(DEMO_TASK_1);
        assertFalse(LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks()).contains(DEMO_TASK_1));
        database.taskDAO().delete(DEMO_TASK_2);

    }

    @Test
    public void test_getTaskList() throws InterruptedException {
        List<Task> expected = Arrays.asList(DEMO_TASK_1,DEMO_TASK_2,DEMO_TASK_3);
        database.taskDAO().insert(DEMO_TASK_1);
        database.taskDAO().insert(DEMO_TASK_2);
        database.taskDAO().insert(DEMO_TASK_3);
        List<Task> result = LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks());
        assertTrue(result.containsAll(expected));
    }

    @Test
    public void test_projects() throws InterruptedException {
        final Task task1 = new Task(0, "task 1", new Date().getTime());
        final Task task2 = new Task(1, "task 2", new Date().getTime());
        final Task task3 = new Task(2, "task 3", new Date().getTime());
        final Task task4 = new Task(3, "task 4", new Date().getTime());
        List<Project> projectList = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects());
        assertEquals("Projet Tartampion", task1.getProject(projectList).getName());
        assertEquals("Projet Lucidia", task2.getProject(projectList).getName());
        assertEquals("Projet Circus", task3.getProject(projectList).getName());
        assertNull(task4.getProject(projectList));
    }

    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(0, "aaa", 123);
        final Task task2 = new Task(1, "zzz", 124);
        final Task task3 = new Task(2, "hhh", 125);

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
        final Task task1 = new Task(0, "aaa", 123);
        final Task task2 = new Task(1, "zzz", 124);
        final Task task3 = new Task(2, "hhh", 125);

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
        final Task task1 = new Task(0, "aaa", 123);
        final Task task2 = new Task(1, "zzz", 124);
        final Task task3 = new Task(2, "hhh", 125);

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
        final Task task1 = new Task(0, "aaa", 123);
        final Task task2 = new Task(1, "zzz", 124);
        final Task task3 = new Task(2, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }

    @Test
    public void clearTasks() throws InterruptedException {
        List<Task> taskList = LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks());
        for(Task tmp : taskList){
            database.taskDAO().delete(tmp);
        }
    }

    public static Task getNewTask(long projectId){
        return new Task(projectId,"DEMO TASK",new Date().getTime());
    }
}
