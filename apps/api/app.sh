#! /bin/bash
#
# Super simple wrapper to allow overriding JVM arguments on startup. Controlled by presence of the JVM_OPTS variable,
# most likely from environment.
#
if [ -z "$JVM_OPTS" ]; then
  # JAVA_OPTS not set, use sensible defaults
  JVM_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"  # Assume execution in container, use cgroups settings for memory and CPU limits
fi

# Proper quoting based on Jenkins CI's start script in official Docker image.
eval "exec java $JVM_OPTS -jar /app/api.jar \"\$@\""
