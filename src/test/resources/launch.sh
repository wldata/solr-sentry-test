#!/bin/bash

for i in `seq 1 20`;
do
    java -cp .:./* com.test.MultithreadedWriter 1 500 2>&1>"$i.log"  &
done
