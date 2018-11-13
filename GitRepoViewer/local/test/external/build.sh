#!/bin/bash

exit_if_error() {
    if [ $1 -ne 0 ]
    then
        echo "Error $2"
        exit $1
    fi
}


build_stubs() {
    echo " -----===== building android stubs =====-----"
    cd android-stubs-master && make && cd ..
    exit_if_error $? "Failed building stub jar"
}

build_stubs
