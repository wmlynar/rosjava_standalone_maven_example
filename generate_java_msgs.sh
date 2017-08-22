#!/bin/bash
echo "this step requires rosjava installed (and sourced)"
cd rosjava-standalone-catkin-workspace
catkin_make clean install
cd ..
cp -r ./rosjava-standalone-catkin-workspace/build/rosjava_standalone_msgs/java/rosjava_standalone_msgs/build/tmp/publishMavenJavaPublicationToMavenRepository/* rosjava-standalone-parent/local-maven-repo/
