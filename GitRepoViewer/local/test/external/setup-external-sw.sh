#!/bin/bash

ORG_JSON_JAR=org.json.jar

exit_if_error() {
    if [ $1 -ne 0 ]
    then
        echo "Error $2"
        exit $1
    fi
}


dload_json() {
    if [ ! -f ${ORG_JSON_JAR} ]
    then
        echo " -----===== downloading json file =====-----"
        wget 'https://search.maven.org/remotecontent?filepath=org/json/json/20171018/json-20171018.jar' -O ${ORG_JSON_JAR}
        exit_if_error $? "Failed downloading ${ORG_JSON_JAR}"
    fi
}

dload_stubs() {
    # download zip file
    ZIP_URL=https://github.com/progund/android-stubs/archive/master.zip
    STUBS_ZIP=android-stubs-master.zip
    if [ ! -f ${STUBS_ZIP} ]
    then
        echo " -----===== downloading android stubs file =====-----"
        curl -LJ -o ${STUBS_ZIP} ${ZIP_URL}
        exit_if_error $? "Failed downloading ${ZIP_URL}"
    fi

    # check if compilation is needed
    if [ ! -f android-stubs-master/android/util/Log.java ]
    then
        echo " -----===== unziping android stub files =====-----"
        unzip ${STUBS_ZIP}
        exit_if_error $? "Failed unzipping ${STUBS_ZIP}"
    fi
    
}


#
# main
#
dload_json
dload_stubs

$(dirname $0)/build.sh
