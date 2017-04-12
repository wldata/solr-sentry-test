package com.test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tushar on 4/11/17.
 */
public class SparkSolrRunner {
    public static void main(String[] args) {

        int numDocs = 1;
        if (args.length > 0) {
            numDocs = Integer.parseInt(args[0]);
        }


        SparkConf sparkConf = new SparkConf().setAppName(SparkSolrRunner.class.getSimpleName());
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            integers.add(i);
        }

        final int finalNumDocs = numDocs;
        JavaRDD<Integer> javaRDD = sparkContext.parallelize(integers, 32).map(new Function<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) throws Exception {
                SolrDocWriter solrDocWriter = new SolrDocWriter();
                solrDocWriter.run(finalNumDocs);
                return integer + 1;
            }
        });

        List<Integer> collect = javaRDD.collect();
        System.out.println(collect.size());
    }
}
