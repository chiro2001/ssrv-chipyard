//******************************************************************************
// Copyright (c) 2019 - 2019, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE and LICENSE.SiFive for license details.
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// SSRV Tile Wrapper
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

package ssrv

import chisel3._
import chisel3.util._

import scala.sys.process._

class SSRVCoreIO extends Bundle {
  // Control
  val pwrup_rst_n = Input(Bool()) // Power-Up Reset
  val rst_n = Input(Bool()) // Regular Reset signal
  val cpu_rst_n = Input(Bool()) // CPU Reset (Core Reset)
  val test_mode = Input(Bool()) // Test mode
  val test_rst_n = Input(Bool()) // Test mode's reset
  val clk = Input(Bool()) // System clock
  val rtc_clk = Input(Bool()) // Real-time clock
  // `ifdef SCR1_DBGC_EN
  //     val ndm_rst_n_out = Output(Bool())           // Non-DM Reset from the Debug Module (DM)
  // `endif // SCR1_DBGC_EN

  // Fuses
  val fuse_mhartid = Input(UInt(32.W)) // Hart ID
  // `ifdef SCR1_DBGC_EN
  //     val fuse_idcode = Input(UInt((31+1).W))             // TAPC IDCODE
  // `endif // SCR1_DBGC_EN

  // IRQ
  // `ifdef SCR1_IPIC_EN
  //     input   logic [SCR1_IRQ_LINES_NUM-1:0]          irq_lines,              // IRQ lines to IPIC
  val irq_lines = Input(UInt(14.W)) // External IRQ input
  // `else // SCR1_IPIC_EN
  //     val ext_irq = Input(Bool())                 // External IRQ input
  // `endif // SCR1_IPIC_EN
  val soft_irq = Input(Bool()) // Software IRQ input

  // `ifdef SCR1_DBGC_EN
  //     // -- JTAG I/F
  //     val trst_n = Input(Bool()) 
  //     val tck = Input(Bool()) 
  //     val tms = Input(Bool()) 
  //     val tdi = Input(Bool()) 
  //     val tdo = Output(Bool()) 
  //     val tdo_en = Output(Bool()) 
  // `endif // SCR1_DBGC_EN

  // Instruction Memory Interface
  val io_axi_imem_awid = Output(UInt((3 + 1).W))
  val io_axi_imem_awaddr = Output(UInt((31 + 1).W))
  val io_axi_imem_awlen = Output(UInt((7 + 1).W))
  val io_axi_imem_awsize = Output(UInt((2 + 1).W))
  val io_axi_imem_awburst = Output(UInt((1 + 1).W))
  val io_axi_imem_awlock = Output(Bool())
  val io_axi_imem_awcache = Output(UInt((3 + 1).W))
  val io_axi_imem_awprot = Output(UInt((2 + 1).W))
  val io_axi_imem_awregion = Output(UInt((3 + 1).W))
  val io_axi_imem_awuser = Output(UInt((3 + 1).W))
  val io_axi_imem_awqos = Output(UInt((3 + 1).W))
  val io_axi_imem_awvalid = Output(Bool())
  val io_axi_imem_awready = Input(Bool())
  val io_axi_imem_wdata = Output(UInt((31 + 1).W))
  val io_axi_imem_wstrb = Output(UInt((3 + 1).W))
  val io_axi_imem_wlast = Output(Bool())
  val io_axi_imem_wuser = Output(UInt((3 + 1).W))
  val io_axi_imem_wvalid = Output(Bool())
  val io_axi_imem_wready = Input(Bool())
  val io_axi_imem_bid = Input(UInt((3 + 1).W))
  val io_axi_imem_bresp = Input(UInt((1 + 1).W))
  val io_axi_imem_bvalid = Input(Bool())
  val io_axi_imem_buser = Input(UInt((3 + 1).W))
  val io_axi_imem_bready = Output(Bool())
  val io_axi_imem_arid = Output(UInt((3 + 1).W))
  val io_axi_imem_araddr = Output(UInt((31 + 1).W))
  val io_axi_imem_arlen = Output(UInt((7 + 1).W))
  val io_axi_imem_arsize = Output(UInt((2 + 1).W))
  val io_axi_imem_arburst = Output(UInt((1 + 1).W))
  val io_axi_imem_arlock = Output(Bool())
  val io_axi_imem_arcache = Output(UInt((3 + 1).W))
  val io_axi_imem_arprot = Output(UInt((2 + 1).W))
  val io_axi_imem_arregion = Output(UInt((3 + 1).W))
  val io_axi_imem_aruser = Output(UInt((3 + 1).W))
  val io_axi_imem_arqos = Output(UInt((3 + 1).W))
  val io_axi_imem_arvalid = Output(Bool())
  val io_axi_imem_arready = Input(Bool())
  val io_axi_imem_rid = Input(UInt((3 + 1).W))
  val io_axi_imem_rdata = Input(UInt((31 + 1).W))
  val io_axi_imem_rresp = Input(UInt((1 + 1).W))
  val io_axi_imem_rlast = Input(Bool())
  val io_axi_imem_ruser = Input(UInt((3 + 1).W))
  val io_axi_imem_rvalid = Input(Bool())
  val io_axi_imem_rready = Output(Bool())

  // Data Memory Interface
  val io_axi_dmem_awid = Output(UInt((3 + 1).W))
  val io_axi_dmem_awaddr = Output(UInt((31 + 1).W))
  val io_axi_dmem_awlen = Output(UInt((7 + 1).W))
  val io_axi_dmem_awsize = Output(UInt((2 + 1).W))
  val io_axi_dmem_awburst = Output(UInt((1 + 1).W))
  val io_axi_dmem_awlock = Output(Bool())
  val io_axi_dmem_awcache = Output(UInt((3 + 1).W))
  val io_axi_dmem_awprot = Output(UInt((2 + 1).W))
  val io_axi_dmem_awregion = Output(UInt((3 + 1).W))
  val io_axi_dmem_awuser = Output(UInt((3 + 1).W))
  val io_axi_dmem_awqos = Output(UInt((3 + 1).W))
  val io_axi_dmem_awvalid = Output(Bool())
  val io_axi_dmem_awready = Input(Bool())
  val io_axi_dmem_wdata = Output(UInt((31 + 1).W))
  val io_axi_dmem_wstrb = Output(UInt((3 + 1).W))
  val io_axi_dmem_wlast = Output(Bool())
  val io_axi_dmem_wuser = Output(UInt((3 + 1).W))
  val io_axi_dmem_wvalid = Output(Bool())
  val io_axi_dmem_wready = Input(Bool())
  val io_axi_dmem_bid = Input(UInt((3 + 1).W))
  val io_axi_dmem_bresp = Input(UInt((1 + 1).W))
  val io_axi_dmem_bvalid = Input(Bool())
  val io_axi_dmem_buser = Input(UInt((3 + 1).W))
  val io_axi_dmem_bready = Output(Bool())
  val io_axi_dmem_arid = Output(UInt((3 + 1).W))
  val io_axi_dmem_araddr = Output(UInt((31 + 1).W))
  val io_axi_dmem_arlen = Output(UInt((7 + 1).W))
  val io_axi_dmem_arsize = Output(UInt((2 + 1).W))
  val io_axi_dmem_arburst = Output(UInt((1 + 1).W))
  val io_axi_dmem_arlock = Output(Bool())
  val io_axi_dmem_arcache = Output(UInt((3 + 1).W))
  val io_axi_dmem_arprot = Output(UInt((2 + 1).W))
  val io_axi_dmem_arregion = Output(UInt((3 + 1).W))
  val io_axi_dmem_aruser = Output(UInt((3 + 1).W))
  val io_axi_dmem_arqos = Output(UInt((3 + 1).W))
  val io_axi_dmem_arvalid = Output(Bool())
  val io_axi_dmem_arready = Input(Bool())
  val io_axi_dmem_rid = Input(UInt((3 + 1).W))
  val io_axi_dmem_rdata = Input(UInt((31 + 1).W))
  val io_axi_dmem_rresp = Input(UInt((1 + 1).W))
  val io_axi_dmem_rlast = Input(Bool())
  val io_axi_dmem_ruser = Input(UInt((3 + 1).W))
  val io_axi_dmem_rvalid = Input(Bool())
  val io_axi_dmem_rready = Output(Bool())
}

class SSRVCoreBlackbox
  extends BlackBox
    with HasBlackBoxResource {
  val io = IO(new SSRVCoreIO)

  val chipyardDir = System.getProperty("user.dir")
  val ssrvVsrcDir = s"$chipyardDir/generators/ssrv/src/main/resources/vsrc"

  // pre-process the verilog to remove "includes" and combine into one file
  val make = s"make -C $ssrvVsrcDir default "
  require(make.! == 0, "Failed to run preprocessing step")

  // add wrapper/blackbox after it is pre-processed
  // addPath(s"$ssrvVsrcDir/SSRVCoreBlackbox.preprocessed.sv")
  addResource(s"$ssrvVsrcDir/ssrv")
}
