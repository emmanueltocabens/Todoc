package com.et_cleanup.et_todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class TaskDAOTest {

    @Rule
    public TestWatcher instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TodocDataBase database;

    private final Task DEMO_TASK_1 = new Task(1,1,"DEMO1",new Date().getTime());
    private final Task DEMO_TASK_2 = new Task(2,2,"DEMO2",new Date().getTime());
    private final Task DEMO_TASK_3 = new Task(3,3,"DEMO3",new Date().getTime());

    private final RoomDatabase.Callback prepopulate = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> database.projectDAO().insertAll(
                    new Project("Projet Tartampion", 0xFFEADAD1),
                    new Project("Projet Lucidia", 0xFFB4CDBA),
                    new Project("Projet Circus", 0xFFA3CED2)
            ));

        }
    };

    /**
     * init db and prepopulate with projects
     */
    @Before
    public void initDb() throws InterruptedException {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                TodocDataBase.class)
                .allowMainThreadQueries()
                .addCallback(prepopulate)
                .build();
        Executors.newSingleThreadExecutor().awaitTermination(1000, TimeUnit.MILLISECONDS);
        clearTasks();
    }

    @After
    public void closeDb() throws InterruptedException {
        clearTasks();
        database.close();
    }

    /**
     * inserts 3 task, get a list of existing tasks, checks if the list is of the correct size, then deletes all added tasks
     */
    @Test
    public void test_task_insert_and_delete() throws InterruptedException {
        database.taskDAO().insert(DEMO_TASK_1);
        database.taskDAO().insert(DEMO_TASK_2);
        database.taskDAO().insert(DEMO_TASK_3);
        List<Task> taskList = LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks());
        assertEquals(3, taskList.size());
        assertEquals(DEMO_TASK_1.getId(), taskList.get(0).getId());
        database.taskDAO().delete(DEMO_TASK_1);
        database.taskDAO().delete(DEMO_TASK_2);
        database.taskDAO().delete(DEMO_TASK_3);
        taskList = LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks());
        assertTrue(taskList.isEmpty());
    }

    /**
     * add 3 tasks, then checks if all were added
     */
    @Test
    public void test_task_getAllTasks() throws InterruptedException {
        database.taskDAO().insert(DEMO_TASK_1);
        database.taskDAO().insert(DEMO_TASK_2);
        database.taskDAO().insert(DEMO_TASK_3);
        List<Task> taskList = LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks());
        assertEquals(3, taskList.size());
        assertTrue(taskList.containsAll(Arrays.asList(DEMO_TASK_1,DEMO_TASK_2,DEMO_TASK_3)));
    }


    /**
     * clears all existing tasks
     */
    public void clearTasks() throws InterruptedException {
        List<Task> taskList = LiveDataTestUtil.getOrAwaitValue(database.taskDAO().getAllTasks());
        for(Task tmp : taskList){
            database.taskDAO().delete(tmp);
        }
    }
}
