#!/bin/bash

ORG_NAME=progund
GITHUB_URL="https://api.github.com/orgs/${ORG_NAME}/repos?per_page=400"
SAVE_FILE=${ORG_NAME}-repos.json

if [ "$1" = "--clean" ]
then
    rm ${SAVE_FILE}
fi

download_json() {

    if [ ! -f  ${SAVE_FILE} ]
    then
        echo -n "Download repos for ${ORG_NAME}: "
        curl -s -LJ -o ${SAVE_FILE} ${GITHUB_URL}
        RET=$?
        if [ $RET -ne 0 ]
        then
            echo "FAILED"
            exit 1
        else
            echo "OK"
            echo
        fi

        echo -n "Validating json data with jq: "
        which jq > /dev/null 2> /dev/null
        RET=$?
        if [ $RET -ne 0 ]
        then
            echo " .. no jq present ($RET), not validating"
        else
            cat ${SAVE_FILE} | jq '.' > /dev/null
            RET=$?
            if [ $RET -ne 0 ]
            then
                echo "FAILED"
                exit 1
            else
                echo "OK"
                echo
            fi
        fi
        
        echo "Stored JSON data for ${ORG_NAME} in ${SAVE_FILE}"
        echo
    fi
}

create_small_json() {
    
    echo "Creating small Java String in a (for copy-paste) "
    echo " * Creating small JSON file (tmp.json)"
    echo "[" > tmp.json
    cat ${SAVE_FILE} | jq '.[0]' >> tmp.json
    echo "," >> tmp.json
    cat ${SAVE_FILE} | jq '.[1]' >> tmp.json
    echo "]" >> tmp.json
    echo " * Creating string.txt"
    cat tmp.json| sed -e 's,\",\\\",g' | tr '\n' ' '> string.txt
#    rm tmp.json
    echo ""
    echo "You can now copy/paste the content of the file string.txt to your Java class"
}


download_json
create_small_json

