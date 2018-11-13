#!/bin/bash

ORG_JSON_JAR=org.json.jar
ADHD_SH=adhd-master/bin/adhd.sh

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

dload_adhd() {
    UDHD_URL=https://github.com/progund/adhd/archive/master.zip
    if [ ! -f ${ADHD_SH} ]
    then
        echo " -----===== downloading adhd =====-----"
        curl -LJ -o adhd-master.zip ${UDHD_URL}
        exit_if_error $? "Failed downloading ${UDHD_URL}"

        unzip adhd-master.zip
    fi
}

dload_oc() {
    OC_URL=https://raw.githubusercontent.com/progund/java-extra-lectures/master/caching/
    OC_DIR=se/juneday/
    OC_JAVA=ObjectCache.java
    OCR_JAVA=ObjectCacheReader.java

    DEST_DIR=object-cache
    
    mkdir -p ${DEST_DIR}/${OC_DIR}
    if [ ! -f ${DEST_DIR}/${OC_DIR}/${OC_JAVA} ]
    then
        echo " -----===== downloading OC =====-----"
        curl -LJ -o ${DEST_DIR}/${OC_DIR}/${OC_JAVA} ${OC_URL}/${OC_DIR}/${OC_JAVA}
        exit_if_error $? "Failed downloading ObjectCache"
        curl -LJ -o ${DEST_DIR}/${OC_DIR}/${OCR_JAVA} ${OC_URL}/${OC_DIR}/${OCR_JAVA}
        exit_if_error $? "Failed downloading ObjectCache"
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
dload_adhd
#dload_oc

$(dirname $0)/build.sh
