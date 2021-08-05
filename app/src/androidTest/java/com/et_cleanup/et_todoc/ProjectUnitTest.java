package com.et_cleanup.et_todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.et_cleanup.et_todoc.data.TodocDataBase;
import com.et_cleanup.et_todoc.model.Project;
import com.et_cleanup.et_todoc.ui.MainActivity;
import com.et_cleanup.et_todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;

import java.util.List;
import java.util.concurrent.Executors;

public class ProjectUnitTest {

    private TodocDataBase database;

    private final RoomDatabase.Callback prepopulate = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                database.projectDAO().insert(new Project("Projet Tartampion", 0xFFEADAD1,0));
                database.projectDAO().insert(new Project("Projet Lucidia", 0xFFB4CDBA,1));
                database.projectDAO().insert(new Project( "Projet Circus", 0xFFA3CED2,2));
            });
        }
    };

    @Rule
    public TestWatcher instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

        @Before
        public void initDb() throws Exception {
            this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                    TodocDataBase.class)
                    .allowMainThreadQueries()
                    .addCallback(prepopulate)
                    .build();
        }

        @After
        public void closeDb() throws Exception {
            database.close();
        }


        // DATA SET FOR TEST
        private static final Project DEMO_PROJECT = new Project("DEMO PROJECT",0xFFEADAD1,4);

        @Test
        public void initTest() throws InterruptedException {
            List<Project> projectList = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects());
            assertNotNull(projectList);
            assertEquals(LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects()).size(),3);
        }

        @Test
        public void test_getProjectList() throws InterruptedException {
            List<Project> testList = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects());
            assertEquals(3,testList.size());
            assertEquals("Projet Tartampion",testList.get(0).getName());
            assertEquals("Projet Lucidia",testList.get(1).getName());
            assertEquals("Projet Circus",testList.get(2).getName());
        }

        @Test
        public void test_insertAndDelete_project() throws InterruptedException {
            database.projectDAO().insert(DEMO_PROJECT);
            Project inserted = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(4));
            database.projectDAO().delete(LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(4)));
            assertNull(inserted);

        }

        @Test
        public void test_getProjectFromID(){

        }

        public void clearProjects() throws InterruptedException {
            List<Project> projectList = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects());
            for(Project tmp : projectList){
                database.projectDAO().delete(tmp);
            }
        }
}
