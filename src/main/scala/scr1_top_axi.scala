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
import chisel3.experimental.{ChiselAnnotation, RunFirrtlTransform}
import chisel3.util._
import firrtl.transforms.{BlackBoxPathAnno, BlackBoxSourceHelper}

import scala.reflect.io.File
import scala.sys.process._

trait SSRVCoreIOIMem extends Bundle {
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
}

trait SSRVCoreIODMem extends Bundle {
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

trait SSRVCoreIOJtag extends Bundle {
  // -- JTAG I/F
  val trst_n = Input(Bool())
  val tck = Input(Bool())
  val tms = Input(Bool())
  val tdi = Input(Bool())
  val tdo = Output(Bool())
  val tdo_en = Output(Bool())
}

trait SSRVCoreIOIRQWithIPIC extends Bundle {
  val irq_lines = Input(UInt(14.W)) // External IRQ input
}

trait SSRVCoreIOIRQWithoutIPIC extends Bundle {
  val ext_irq = Input(Bool()) // External IRQ input
}

trait SSRVCoreIOIRQ
  extends Bundle
    with SSRVCoreIOIRQWithIPIC {
  val soft_irq = Input(Bool()) // Software IRQ input
}

trait SSRVCoreIODNGC extends Bundle with SSRVCoreIOJtag {
  val ndm_rst_n_out = Output(Bool()) // Non-DM Reset from the Debug Module (DM)
  val fuse_idcode = Input(UInt((31 + 1).W)) // TAPC IDCODE
}

trait SSRVCoreIOBase extends Bundle {
  val pwrup_rst_n = Input(Bool()) // Power-Up Reset
  val rst_n = Input(Bool()) // Regular Reset signal
  val cpu_rst_n = Input(Bool()) // CPU Reset (Core Reset)
  val test_mode = Input(Bool()) // Test mode
  val test_rst_n = Input(Bool()) // Test mode's reset
  val clk = Input(Clock()) // System clock
  val rtc_clk = Input(Clock()) // Real-time clock

  val fuse_mhartid = Input(UInt(32.W)) // Hart ID
}

class SSRVCoreIO extends Bundle
  with SSRVCoreIOBase
  with SSRVCoreIOIRQ
  with SSRVCoreIOIMem
  with SSRVCoreIODMem

class scr1_top_axi
  extends BlackBox
    // with HasBlackBoxResource
    with HasBlackBoxDir {
  val io = IO(new SSRVCoreIO)

  val chipyardDir = System.getProperty("user.dir")
  val ssrvVsrcDir = s"$chipyardDir/generators/ssrv/src/main/resources/vsrc"

  // pre-process the verilog to remove "includes" and combine into one file
  val make = s"make -C $ssrvVsrcDir default "
  require(make.! == 0, "Failed to run preprocessing step")

  // add wrapper/blackbox after it is pre-processed
  // addPath(s"$ssrvVsrcDir/SSRVCoreBlackbox.preprocessed.sv")
  // addResource(s"$ssrvVsrcDir/ssrv")
  addDir(s"$ssrvVsrcDir/ssrv")
  // File(s"$ssrvVsrcDir/ssrv").toDirectory.deepFiles.foreach(f => {
  //   println(s"file: ${f.path}")
  // })
}

trait HasBlackBoxDir extends BlackBox {
  self: BlackBox =>

  /** Copies files to the target directory
   *
   * This works with absolute and relative paths. Relative paths are relative
   * to the current working directory, which is generally not the same as the
   * target directory.
   */
  def addDir(blackBoxPath: String): Unit = {
    File(blackBoxPath).toDirectory.deepFiles.foreach(f => {
      chisel3.experimental.annotate(new ChiselAnnotation with RunFirrtlTransform {
        def toFirrtl = BlackBoxPathAnno(self.toNamed, f.path)

        def transformClass = classOf[BlackBoxSourceHelper]
      })
    })
  }
}