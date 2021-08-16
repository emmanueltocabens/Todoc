package com.et_cleanup.et_todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.et_cleanup.et_todoc.data.TodocDataBase;
import com.et_cleanup.et_todoc.model.Project;
import com.et_cleanup.et_todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
public class ProjectDAOTest {

    private TodocDataBase database;

    // DATA SET FOR TEST
    private static final Project DEMO_PROJECT = new Project(4,"DEMO PROJECT", 0xFFEADAD1);

    private final RoomDatabase.Callback prepopulate = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                database.projectDAO().insertAll(
                        new Project("Projet Tartampion", 0xFFEADAD1),
                        new Project("Projet Lucidia", 0xFFB4CDBA),
                        new Project("Projet Circus", 0xFFA3CED2)
                );
            });

        }
    };


    @Rule
    public TestWatcher instantTaskExecutorRule = new InstantTaskExecutorRule();

    /**
     * init db and prepopulate with projects
     */
    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                TodocDataBase.class)
                .allowMainThreadQueries()
                .addCallback(prepopulate)
                .build();
    }

    @After
    public void closeDb() throws Exception {
        clearProjects();
        database.close();
    }




    /**
     * Retrieves project list, asserts that it has 3 items, then checks project names
     */
    @Test
    public void test_getProjectList() throws InterruptedException {
        List<Project> testList = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects());
        assertEquals(3, testList.size());
        assertEquals("Projet Tartampion", testList.get(0).getName());
        assertEquals("Projet Lucidia", testList.get(1).getName());
        assertEquals("Projet Circus", testList.get(2).getName());
    }

    /**
     * Insert demo project, check if inserted, then delete and check if deleted
     */
    @Test
    public void test_insertAndDelete_project() throws InterruptedException {
        database.projectDAO().insert(DEMO_PROJECT);
        Project inserted = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(4));
        assertEquals("DEMO PROJECT", inserted.getName());
        database.projectDAO().delete(inserted);
        assertNull(LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(4)));
    }

    /**
     * asserts that trying to get a project that doesn't exists returns null
     * then gets each project by their IDs
     */
    @Test
    public void test_getProjectFromID() throws InterruptedException {
        assertNull(LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(4)));
        assertEquals(1, LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(1)).getId());
        assertEquals(2, LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(2)).getId());
        assertEquals(3, LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(3)).getId());
    }

    /**
     * clears all existing projects
     */
    public void clearProjects() throws InterruptedException {
        List<Project> projectList = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects());
        for (Project tmp : projectList) {
            database.projectDAO().delete(tmp);
        }
    }
}
