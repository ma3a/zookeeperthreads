package com.example.zookeeperthreads;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperthreadsApplicationTests {

    static {
        try {
            TestingServer testingServer = new TestingServer(2181);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    CuratorFramework curatorFramework;

    /**
     * This test shows thread grow
     */
    @Test
    public void contextLoads() throws Exception {
        Thread.sleep(5000); // waiting to make sure that all curatorframework objects are created

        int before = getCuratorsThreadCount();
        curatorFramework.create().creatingParentsIfNeeded().forPath("/root/app", "data".getBytes());

        Thread.sleep(5000); // waiting for TreeCache received event
        int after = getCuratorsThreadCount();

        assertEquals("Curator thread should not created when property changed", before, after);
    }

    public static int getCuratorsThreadCount() {
        int count = 0;
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        for (Thread thread : allStackTraces.keySet()) {
            if (thread.getName().contains("Curator")) {
                count++;
            }
        }
        return count;
    }

}
