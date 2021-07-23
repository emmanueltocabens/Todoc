package com.cleanup.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.data.TodocDataBase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ProjectUnitTest {

    private TodocDataBase database;

    @Rule
    public TestWatcher instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                TodocDataBase.class)
                .allowMainThreadQueries()
                .build();
        clearTasks();
    }

    @After
    public void closeDb() throws Exception {
        clearProjects();
        clearTasks();
        database.close();
    }


    // DATA SET FOR TEST
    private static final Project DEMO_PROJECT = new Project("DEMO PROJECT",0xFFEADAD1);

    @Test
    public void test_getProjectList() throws InterruptedException {
        //TODO
        List<Project> testList = LiveDataTestUtil.getValue(database.projectDAO().getAllProjects());
        assertEquals(testList.size(),3);
        assertEquals(testList.get(0).getName(),"Projet Tartampion");
        assertEquals(testList.get(0).getName(),"Projet Lucidia");
        assertEquals(testList.get(0).getName(),"Projet Circus");
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

    public void clearTasks() throws InterruptedException {
        List<Task> taskList = LiveDataTestUtil.getValue(database.taskDAO().getAllTasks());
        for(Task tmp : taskList){
            database.taskDAO().delete(tmp);
        }
    }

    public void clearProjects() throws InterruptedException {
        List<Project> projectList = LiveDataTestUtil.getValue(database.projectDAO().getAllProjects());
        for(Project tmp : projectList){
            database.projectDAO().delete(tmp);
        }
    }


}
