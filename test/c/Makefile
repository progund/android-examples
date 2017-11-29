READ_PROG=test-user-io-read
PRINT_PROG=test-user-io-print
SOURCES=user-io.c
CLASS=ReadFile
CLASSES=ReadFile.class

CC=gcc
CFLAGS= -pedantic -Wconversion -Wall -Werror -Wextra -Wstrict-prototypes 

JAVA_SOURCES=$(CLASSES:%.class=%.java)

%.class:%.java
	javac $<

$(READ_PROG): $(SOURCES)
	gcc $(SOURCES) test-user-io-read.c  -o $@

$(PRINT_PROG): $(SOURCES)
	gcc $(SOURCES) test-user-io-print.c -o $@


test:
	@echo " ==== Test C ===="
	make test-c
	@echo " ==== Test Java ===="
	make test-java

test-java: $(CLASSES)
	@echo "Testing with file itself"
	java $(CLASS) $(CLASS).java

test-c: $(READ_PROG) $(PRINT_PROG)
	@echo "--== READ ==--"
	@echo 
	@echo "Testing with 3"
	@echo 3 | ./$(READ_PROG)
	@echo 
	@echo "Testing with -1"
	@echo -1 | ./$(READ_PROG)
	@echo
	@echo "--== PRINT ==--"
	@echo 
	@./$(PRINT_PROG)
	@echo

neg-test-java: $(CLASSES)
	@echo "Testing with non existing file"
	-java $(CLASS) "file-not-existing"
	@echo "Testing with existing file but wrong permissions"
	touch existing-file
	chmod a-r existing-file
	-java $(CLASS) "existing-file"
	rm existing-file
	@echo "Pass empty parameter"
	java $(CLASS) "existing-file"

neg-test-c: $(READ_PROG) $(PRINT_PROG)
	@echo "--== READ ==--"
	@echo "Testing read with a"
	@echo a | ./$(READ_PROG)
	@echo 
	@echo "Testing read with no data"
	@echo -n | ./$(READ_PROG)
	@echo 
	@echo "--== PRINT ==--"
	@echo "Testing print with null"
	@./$(PRINT_PROG) arguments to force null 
	@echo 


clean:
	-rm *~ *.o $(CLASSES) $(PROG) *.gcda *.gcno $(READ_PROG) $(PRINT_PROG) *.info
	-rm -fr coverage

gcov:
	gcc -c $(CFLAGS) --coverage -fprofile-arcs -ftest-coverage $(SOURCES) -o user-io.o
	gcc $(CFLAGS) --coverage -fprofile-arcs -ftest-coverage	user-io.o test-user-io-read.c  -o $(READ_PROG) -lm
	gcc $(CFLAGS) --coverage -fprofile-arcs -ftest-coverage	user-io.o test-user-io-print.c  -o $(PRINT_PROG) -lm
	for i in `seq -200 200` ; do echo "$$i" | ./$(READ_PROG) ; done
	./$(PRINT_PROG)
	gcov -b user-io.c

lcov: gcov 
	lcov --directory .  --capture --output-file  user-info.info && genhtml -o coverage user-info.info 

checkstyle: 
	for fille in $(JAVA_SOURCES) ; do checkstyle -c google_checks.xml ReadFile.java; done
