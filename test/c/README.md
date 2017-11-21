This directory contains files used to discuss test.

Read the article: [http://wiki.juneday.se/mediawiki/index.php/Android:Test](http://wiki.juneday.se/mediawiki/index.php/Android:Test)

# Makefile

All is executed via the Makefile

## Run test of c program

`make test-c`

## Run test of Java program

`make test-java`

## Run "negative" test of c program

`make neg-test-c`

## Run "negative" test of Java program

`make neg-test-java`

## Run gcov and lcov on C program

`make clean gcov lcov`

and to start a browser showing the results:
`firefox coverage/index.html`

## Run checkstyle on the Java source code

``make checkstyle``

## Clean up

``make clean``

