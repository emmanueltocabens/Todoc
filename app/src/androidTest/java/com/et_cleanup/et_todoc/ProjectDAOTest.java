package com.et_cleanup.et_todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
public class ProjectDAOTest {

    private TodocDataBase database;

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

    @Rule
    public TestWatcher instantTaskExecutorRule = new InstantTaskExecutorRule();

        @Before
        public void initDb() throws Exception {
            this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                    TodocDataBase.class)
                    .allowMainThreadQueries()
                    .addCallback(prepopulate)
                    .addMigrations(MIGRATION_3_4)
                    .build();
        }

        @After
        public void closeDb() throws Exception {
            clearProjects();
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
            Log.d("untag","....");
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
            assertEquals("DEMO PROJECT",inserted.getName());
            database.projectDAO().delete(inserted);
            assertNull(LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(4)));
        }

        @Test
        public void test_getProjectFromID() throws InterruptedException {
            assertNull(LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(4)));
            assertEquals(0,LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(0)).getId());
            assertEquals(1,LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(1)).getId());
            assertEquals(2,LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getProjectFromID(2)).getId());
        }

        public void clearProjects() throws InterruptedException {
            List<Project> projectList = LiveDataTestUtil.getOrAwaitValue(database.projectDAO().getAllProjects());
            for(Project tmp : projectList){
                database.projectDAO().delete(tmp);
            }
        }
}
