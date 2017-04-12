package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tushar on 4/11/17.
 */
public class MultithreadedWriter {
    private static final Logger logger = LoggerFactory.getLogger(MultithreadedWriter.class);

    public static void main(String[] args) throws InterruptedException {

        int numTasks = 1;
        int numDocs = 1;
        if (args.length > 0) {
            numTasks = Integer.parseInt(args[0]);
        }

        if (args.length > 1) {
            numDocs = Integer.parseInt(args[1]);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numTasks);

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < numTasks; i++) {
            Callable<Void> task = getTask(numDocs);
            tasks.add(task);
        }

        logger.info("Invoking {} tasks", numTasks);

        executorService.invokeAll(tasks);

        executorService.shutdown();
        logger.info("Terminated");
    }

    private static Callable<Void> getTask(final int numDocs) {
        logger.info("Creating Task");
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SolrDocWriter solrDocWriter = new SolrDocWriter();
                solrDocWriter.run(numDocs);
                return null;
            }


        };
    }
}
