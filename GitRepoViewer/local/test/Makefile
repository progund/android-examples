##########################################################
#
# Makefile used to test GitRepoViewer (an example Android project)
# used in Juneday's course material
#
# Authors:   Rikard Fröberg, Henrik Sandklef
# Copyright: 2018 juneday.se
# License:   GPLv3
#   - no, we're not adding license info in this file as we should :(
#
##########################################################
#
# to get some help on this makefile, type:
#    make help
#
##########################################################



##########################################################
#
# Settings for the android app, both
#  * test code (for android app) and
#  * android app code and
#
##########################################################
TEST_PKG_PATH=se/juneday/gitrepoviewer
TEST_SRC= \
  $(TEST_PKG_PATH)/domain/test/RepositoryTest.java   \
  $(TEST_PKG_PATH)/util/test/RepositoryCacheTest.java \
  $(TEST_PKG_PATH)/util/test/JsonParserTest.java 
TEST_CLASSES=$(TEST_SRC:%.java=%.class)
TEST_CLASS_NAMES=$(shell echo $(TEST_CLASSES) | sed -e 's,\.class,,g' -e 's,/,\.,g')
ANDROID_APP_DIR=../../app/src/main/java/


##########################################################
#
# Settings for external code
#  * android-stubs: https://github.com/progund/android-stubs
#  * org.json:      https://search.maven.org/remotecontent?filepath=org/json/json/20171018/json-20171018.jar
#  * ADHD: https://github.com/progund/adhd
#  * ObjectCache: https://raw.githubusercontent.com/progund/java-extra-lectures/master/caching
#
##########################################################
EXTERNAL_SW_DIR=external
ANDROID_STUBS=$(EXTERNAL_SW_DIR)/android-stubs-master/android-stubs.jar
ORG_JSON=$(EXTERNAL_SW_DIR)/org.json.jar
ADHD_SH=external/adhd-master/bin/adhd.sh
OC_DIR=object-cache
EXTERNAL_SW_CLASSPATH=$(ANDROID_STUBS):$(ORG_JSON)


##########################################################
#
# CLASSPATH for compile and execution
#
##########################################################
CLASSPATH=$(EXTERNAL_SW_CLASSPATH):$(ANDROID_APP_DIR):.


#
# rule for compiling java source code
#
%.class:%.java
	@echo " ---=== Compile: $< ===---"
	javac -cp $(CLASSPATH) $<

#
# help informaiton target 
#
help:
	@echo "Makefile for building and executing tests for GitRepoViewer"
	@echo 
	@echo "SYNTAX"
	@echo 
	@echo "  make [rule]"
	@echo 
	@echo "RULES"
	@echo "  clean        - cleans up compiled code "
	@echo "  really-clean - cleans up external sw"
	@echo "  check        - performs tests (compiles as well)"
	@echo "  compile      - compiles java test code"
	@echo "  info         - output information about tests"
	@echo "  external     - download and build external software"
	@echo "  adhd         - extract cached data from app on device"
	@echo
	@echo "EXAMPLES"
	@echo "  make clean"
	@echo "  make check"
	@echo "  make compile"
	@echo "  make external"
	@echo

#
# target to do it all
#
compile: external $(TEST_CLASSES)

#
# rule for testing code
#
check: external $(TEST_CLASSES)
	@echo " ---=== Test with all classes ===---"
	@echo " ---=== $(TEST_CLASS_NAMES) ===---"
	@$(foreach var,$(TEST_CLASS_NAMES), echo java -cp $(CLASSPATH) $(var); java -cp $(CLASSPATH) $(var) || exit 1; )

#
# rule for cleaning up
#
clean:
	@echo " ---=== Cleaning up ===---"
	rm -fr $(TEST_CLASSES) android repositories_serialized.data external/repositories_serialized.data 
	-find . -name "*~" | xargs rm -f 
	-find $(ANDROID_APP_DIR)/se -name "*.class" | xargs rm -f 

#
# rule for really cleaning up
#
really-clean: clean
	@echo " ---=== Really leaning up ===---"
	-rm external/*.zip
	-rm -fr external/adhd* external/object-* external/android-stubs* adhd* external/se $(ORG_JSON)
	-rm -fr se/juneday/Obje*

#
# rule for cleaning up
#
info:
	@echo "Test files:                $(TEST_SRC)"
	@echo "CLASSPATH:                 $(CLASSPATH)"
	@echo " * Android app source dir: $(ANDROID_APP_DIR)"
	@echo " * JSON jar:               $(ORG_JSON)"
	@echo " * Android stub files:     $(ANDROID_STUBS)"

#
# setup and build external software
#
.PHONY: external
external:
	cd external && ./setup-external-sw.sh && cd ..

#
# extract serialized objects from device
#
.PHONY: adhd
adhd:
	@echo -n "Making sure only one device is attached: "
	@if [ `${ADHD_SH} -ld | grep emulator | wc -l` -ne 1 ] ; then echo "Only one attached device allowed"; exit 1; fi
	@echo " OK"

	@echo -n "Making sure GitRepoViewer is installed: "
	@if [ `${ADHD_SH} -la | grep gitrepoviewer | wc -l` -ne 1 ] ; then echo "GitRepoViewer does not seem to be installed"; exit 1; fi
	@echo " OK"

	@echo -n "Extracting serialized data from device/app: "
	${ADHD_SH} -ocd $(OC_DIR) -cp $(CLASSPATH) se.juneday.gitrepoviewer serialized
	@echo " OK"

	@echo ""
	@echo "To check the repositories in your app:"
	@echo "cat adhd/apps/se.juneday.gitrepoviewer/se.juneday.gitrepoviewer.MainActivity.txt"

