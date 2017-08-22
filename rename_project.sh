#!/bin/bash
OLD_NAME_1="rosjava-standalone"
OLD_NAME_2="rosjava_standalone"
NEW_NAME_1="new-name"
NEW_NAME_2="new_name"
find . -name \* -exec sed -i "s/$OLD_NAME_1/$NEW_NAME_1/g" {} \;
find . -name \* -exec sed -i "s/$OLD_NAME_2/$NEW_NAME_2/g" {} \;
for f in *; do mv "$f" "`echo $f | sed s/$OLD_NAME_1/$NEW_NAME_1/`"; done
for f in *; do mv "$f" "`echo $f | sed s/$OLD_NAME_2/$NEW_NAME_2/`"; done
