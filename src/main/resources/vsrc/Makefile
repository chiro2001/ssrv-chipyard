#########################################################################################
# pre-process SSRV into a directory
#########################################################################################
base_dir=$(abspath ../../../../../..)
vsrc_dir=$(abspath .)
ssrv_dir=$(vsrc_dir)/SuperScalar-RISCV-CPU/ssrv-on-scr1/fpga

.PHONY: default
default:
	mkdir -p $(vsrc_dir)/ssrv/scr1
	cd $(ssrv_dir)/scr1 && cp --parent $(shell cat $(ssrv_dir)/scr1/core.files) $(vsrc_dir)/ssrv/scr1
	cd $(ssrv_dir)/scr1 && cp --parent $(shell cat $(ssrv_dir)/scr1/axi_top.files) $(vsrc_dir)/ssrv/scr1
	cp -r $(ssrv_dir)/rtl $(vsrc_dir)/ssrv/

clean:
	rm -rf $(vsrc_dir)/ssrv
