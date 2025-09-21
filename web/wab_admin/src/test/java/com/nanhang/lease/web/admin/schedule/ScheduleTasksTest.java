package com.nanhang.lease.web.admin.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleTasksTest {
    @Autowired
    protected ScheduleTasks scheduleTasks;

    @Test
    public void testScheduleTasks1() {
        scheduleTasks.checkLeaseStatus();


    }
}

