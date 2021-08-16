package com.et_cleanup.et_todoc;

import static org.junit.Assert.assertEquals;

import com.et_cleanup.et_todoc.model.Project;
import com.et_cleanup.et_todoc.model.Task;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ModelUnitTest {

    private final Task DEMO_TASK_1 = new Task(1,1,"test",new Date().getTime());
    private final Task DEMO_TASK_2 = new Task(2,2,"test",new Date().getTime());
    private final Task DEMO_TASK_3 = new Task(3,3,"test",new Date().getTime());

    private final List<Project> projectList = Arrays.asList(
            new Project(1,"Projet Tartampion", 0xFFEADAD1),
            new Project(2,"Projet Lucidia", 0xFFB4CDBA),
            new Project(3, "Projet Circus", 0xFFA3CED2)
    );

    /**
     * asserts that Task.getProject() returns the right project
     */
    @Test
    public void test_task_getProject() {
        assertEquals(
                DEMO_TASK_1.getProject(projectList),
                projectList.get(0));
        assertEquals(
                DEMO_TASK_2.getProject(projectList),
                projectList.get(1));
        assertEquals(
                DEMO_TASK_3.getProject(projectList),
                projectList.get(2));
    }




}
