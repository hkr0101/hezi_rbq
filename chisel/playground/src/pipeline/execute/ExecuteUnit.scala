package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.CpuConfig
import cpu.defines._
import cpu.defines.Const._
import chisel3.util.experimental.BoringUtils

class ExecuteUnit extends Module {
  val io = IO(new Bundle {
    val executeStage = Input(new DecodeUnitExecuteUnit())
    val memoryStage  = Output(new ExecuteUnitMemoryUnit())
    val dataS_rdata  = Output(UInt(DATA_SRAM_DATA_WID.W))
    val bra_info     = Output(new BranchInfo())
    val dataSram     = new DataSram()
  })
  val fu = Module(new Fu()).io
  fu.data.pc       := io.executeStage.data.pc
  fu.data.info     := io.executeStage.data.info
  fu.data.src_info := io.executeStage.data.src_info

  io.dataSram <> fu.dataSram
  io.dataS_rdata   := fu.dataSram.rdata
  io.bra_info := fu.bra_info

  // TODO: 完成ExecuteUnit模块的逻辑
  io.memoryStage.data      := fu.data
}
