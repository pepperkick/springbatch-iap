#!/bin/bash

clear

lines=${LINES:="10"}
file=${FILE_PATH:="../input.txt"}
output=""

echo "Number of lines to generate: $lines"

rm ${file}
for i in $(seq 1 ${lines})
do
    max=$(( $RANDOM % 1000 + 1 ))
    echo "Generating line: $i / $lines ($max)"
    for j in $(seq 0 ${max})
    do
        if [[ "$j" != "0" ]]; then
            output="${output},"
        fi
        output="${output}$RANDOM"
    done
    echo "$output" >> ${file}
    output=""
done

