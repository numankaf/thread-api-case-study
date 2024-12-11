package com.threadserver.thread;

import com.threadserver.service.ThreadMapProviderService;
import com.threadserver.threads.SenderThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ThreadMapProviderServiceTests {

    @Autowired
    private ThreadMapProviderService threadMapProviderService;

    @Autowired
    private  ApplicationContext applicationContext;

    private Thread createdThread;
    private final Long threadId = 1L;

    @BeforeEach
    void setUp() {
        Runnable runnable = applicationContext.getBean(SenderThread.class);
        Thread thread = new Thread(runnable);
        thread.setName("Test Thread");
        thread.setPriority(5);
        thread.start();
        createdThread = thread;
    }

    @Test
    public void shouldPutAndGetThread()  {
       threadMapProviderService.putThread(threadId, createdThread);
       var savedThread = threadMapProviderService.getThread(threadId);
       assertThat(savedThread).isNotNull();
       assertThat(savedThread.getName()).isEqualTo(createdThread.getName());
       assertThat(savedThread.getPriority()).isEqualTo(createdThread.getPriority());
       assertThat(savedThread.isAlive()).isEqualTo(createdThread.isAlive());
    }

    @Test
    public void shouldRemoveThread()  {
        threadMapProviderService.putThread(threadId, createdThread);
        threadMapProviderService.removeThread(threadId);

        var maybeThread = threadMapProviderService.getThread(threadId);
        assertThat(maybeThread).isNull();
    }

}
