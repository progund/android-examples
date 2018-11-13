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

build_oc() {
    echo " -----===== building ObjectCache =====-----"
    cd object-cache && javac se/juneday/*.java && cd ..
    exit_if_error $? "Failed building ObjectCache"
}

build_stubs
#build_oc

echo "Build finished :)"
