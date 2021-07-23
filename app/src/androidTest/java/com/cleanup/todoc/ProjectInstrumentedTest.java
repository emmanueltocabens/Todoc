package com.cleanup.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.data.TodocDataBase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.ui.MainActivity;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
public class ProjectInstrumentedTest {

    private TodocDataBase database;

    @Rule
    public TestWatcher instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

        @Before
        public void initDb() throws Exception {
            this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                    TodocDataBase.class)
                    .allowMainThreadQueries()
                    .build();
        }

        @After
        public void closeDb() throws Exception {
            database.close();
        }


        // DATA SET FOR TEST
        private static final Project DEMO_PROJECT = new Project("DEMO PROJECT",0xFFEADAD1);

        @Test
        public void initTest() throws InterruptedException {
            List<Project> projectList = LiveDataTestUtil.getValue(database.projectDAO().getAllProjects());
            List<Task> taskList = LiveDataTestUtil.getValue(database.taskDAO().getAllTasks());
            assertNotNull(projectList);
            assertNotNull(taskList);
        }

        @Test
        public void test_getProjectList() throws InterruptedException {
            List<Project> testList = LiveDataTestUtil.getValue(database.projectDAO().getAllProjects());
            assertEquals(3,testList.size());
            assertEquals("Projet Tartampion",testList.get(0).getName());
            assertEquals("Projet Lucidia",testList.get(1).getName());
            assertEquals("Projet Circus",testList.get(2).getName());
        }

        @Test
        public void test_insertAndDelete_project() throws InterruptedException {
            //TODO
            List<Project> expected = new ArrayList<>(LiveDataTestUtil.getValue(database.projectDAO().getAllProjects()));
            expected.add(DEMO_PROJECT);
            database.projectDAO().insert(DEMO_PROJECT);
            List<Project> result = LiveDataTestUtil.getValue(database.projectDAO().getAllProjects());
            assertTrue(result.containsAll(expected));
            database.projectDAO().delete(DEMO_PROJECT);
            assertFalse(result.contains(DEMO_PROJECT));
        }

        @Test
        public void test_getProjectFromID(){
            Project tmp = database.projectDAO().getProjectFromID(1);
            assertEquals(tmp.getId(),1);
            tmp = database.projectDAO().getProjectFromID(3);
            assertEquals(tmp.getId(),3);
            tmp = database.projectDAO().getProjectFromID(4);
            assertNull(tmp);
        }

        public void clearProjects() throws InterruptedException {
            List<Project> projectList = LiveDataTestUtil.getValue(database.projectDAO().getAllProjects());
            for(Project tmp : projectList){
                database.projectDAO().delete(tmp);
            }
        }
}
