package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._
import cpu.CpuConfig

class WriteBackUnit extends Module {
  val io = IO(new Bundle {
    val writeBackStage = Input(new MemoryUnitWriteBackUnit())
    val regfile        = Output(new RegWrite())
    val debug          = new DEBUG()
  })

  // TODO: 完成WriteBackUnit模块的逻辑
  io.regfile.wdata := io.writeBackStage.data.rd_info.wdata
  io.regfile.wen := io.writeBackStage.data.info.reg_wen & io.writeBackStage.data.info.valid
  io.regfile.waddr := io.writeBackStage.data.info.reg_waddr
  io.debug.pc := io.writeBackStage.data.pc
  io.debug.commit := io.writeBackStage.data.info.reg_wen & io.writeBackStage.data.info.valid
  io.debug.rf_wdata := io.writeBackStage.data.rd_info.wdata
  io.debug.rf_wnum := io.writeBackStage.data.info.reg_waddr
  //io.debug.commit := "b1".U
  when(io.debug.pc === "h8001b284".U){
    io.debug.commit := "b1".U
  }
}
