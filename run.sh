#!/bin/bash

#
# Decide on the file for output
#
N=`cat count 2>/dev/null`
if [ "$N" == "" ]
then
  N=2
fi
N=$((N + 1))
echo $N > count
FILE=output-$N.txt
unset _JAVA_OPTIONS
echo Output will go to $FILE...
sleep 3

#
# Prepare the command line
#
PRINT_COMPILATION="-XX:+PrintCompilation"
PRINT_INLINING="-XX:+PrintInlining"
#PRINT_ASSEMBLY="-XX:+PrintOptoAssembly"
TRACE_DEOPT="-XX:+TraceDeoptimization"
PROF_TRAPS="-XX:+ProfileTraps"
OTHERS="-XX:+TraceICs -XX:+TraceInlineCacheClearing -XX:+LogCompilation"
JAVA_OPTS="-Xms2g \
           -Xmx2g \
           -Xss4m \
           -XX:MaxPermSize=512M \
           -XX:ReservedCodeCacheSize=256m \
           -XX:+UseParallelGC \
           -XX:PermSize=256m \
           -XX:+UseNUMA \
           -XX:-TieredCompilation \
           -XX:+UnlockDiagnosticVMOptions \
           $PRINT_COMPILATION \
           $PRINT_INLINING \
           $TRACE_DEOPT \
           $PRINT_ASSEMBLY \
           $PROF_TRAPSi
           $OTHERS"
CMD="java -server -cp lib/scala-library.jar:exp.jar $JAVA_OPTS miniboxing.test.TestJVM"

#
# Run and store results
#
echo -e "$CMD\n\n\n" > $FILE
$CMD 2>&1 | tee -a $FILE
cp hotspot.log hotspot-$N.log
