#!/bin/bash


ANDROID_CODE_DIR=../app/src/main/java
ANDROID_CODE=$(find ${ANDROID_CODE_DIR}/se/juneday/gitrepoviewer/domain ${ANDROID_CODE_DIR}/se/juneday/gitrepoviewer/util -name "*.java")

TEST_CODE_DIR=test-code
TEST_CODE=$(find ${TEST_CODE_DIR} -name "*.java")
TEST_CLASSES=$(find test-code/ -name "*.java" | sed -e "s,$TEST_CODE_DIR[/]*,,g" -e 's,\.java,,g' -e 's,\/,\.,g' )

ORG_JSON_JAR=org.json.jar

DIR_SEP=:

CLASSPATH=${ORG_JSON_JAR}${DIR_SEP}${ANDROID_CODE_DIR}${DIR_SEP}${TEST_CODE_DIR}

dload() {
    echo " -----===== downloading jar file =====-----"
    if [ ! -f ${ORG_JSON_JAR} ]
    then
        echo "Downloading JSON jar"
        wget 'https://search.maven.org/remotecontent?filepath=org/json/json/20171018/json-20171018.jar' -O ${ORG_JSON_JAR}
    else
        echo "Skipping download since file exists"
    fi
}


exit_if_error() {
    if [ $1 -ne 0 ]
    then
        echo "Error $2"
        exit $1
    fi
}

build() {
    echo " -----===== building =====-----"
    echo "Compiling android code: ${ANDROID_CODE}"
    javac -cp ${CLASSPATH} ${ANDROID_CODE}
    exit_if_error $? "Compiling android code"
    echo
    
    echo "Compiling test code: ${TEST_CODE}"
    echo " - classpath: ${ANDROID_CODE_DIR}"
    javac -cp ${CLASSPATH} ${TEST_CODE}
    exit_if_error $? "Compiling test code"
    echo
    
}

tests() {
    echo " -----===== testing =====-----"
    echo "Executing test code"
    echo "-classpath: ${TEST_CLASSES}:${TEST_CODE_DIR}"
    echo "-testing classses one by one:"
    for class in $TEST_CLASSES
    do
        echo
        echo "* Testing class: $class"
        echo "  ---------------------------------"
        java -cp ${CLASSPATH} $class
        exit_if_error $? "Failed test: $class"
    done
    echo
    echo "All tests passed :)"
}

dload
build
tests








