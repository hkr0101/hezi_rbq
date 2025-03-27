BUILD_DIR = ./build
DIFF_DIR = ../difftest/core
DIFF_WORK_DIR = ../difftest

export PATH := $(PATH):$(abspath ./utils)

verilog:
	$(call git_commit, "generate verilog") # DO NOT REMOVE THIS LINE!!!
	$(MAKE) clean
	mkdir -p $(BUILD_DIR)
	mill -i __.test.runMain Elaborate -td $(BUILD_DIR)

test:
	@echo "make test"
	$(call git_commit, "test chisel module") # DO NOT REMOVE THIS LINE!!!
	$(MAKE) clean
	mkdir -p $(BUILD_DIR)
	mill -i __.test.runMain TestMain -td $(BUILD_DIR)

count:
	find ./playground/ -name "*.scala" | xargs wc -l 

help:
	mill -i __.test.runMain Elaborate --help

compile:
	mill -i __.compile

bsp:
	mill -i mill.bsp.BSP/install

reformat:
	mill -i __.reformat

checkformat:
	mill -i __.checkFormat

clean:
	-rm -rf $(BUILD_DIR)

.PHONY: test verilog help compile bsp reformat checkformat clean

sim:
	@echo "Write this Makefile by yourself."

-include ../Makefile
