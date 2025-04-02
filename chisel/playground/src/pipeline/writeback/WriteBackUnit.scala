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

  // printf("pc:%x\n",io.writeBackStage.data.pc)
  // printf("rf_wdata:%x\n",io.writeBackStage.data.rd_info.wdata)

  // TODO: 完成WriteBackUnit模块的逻辑
  io.regfile.wdata := io.writeBackStage.data.rd_info.wdata
  io.regfile.wen := io.writeBackStage.data.info.reg_wen && io.writeBackStage.data.info.valid
  io.regfile.waddr := io.writeBackStage.data.info.reg_waddr
  io.debug.pc := io.writeBackStage.data.pc
  io.debug.commit := io.writeBackStage.data.info.valid
  io.debug.rf_wdata := io.writeBackStage.data.rd_info.wdata
  io.debug.rf_wnum := io.writeBackStage.data.info.reg_waddr
  // when(io.writeBackStage.data.info.op === ALUOpType.ecall){
  //   io.debug.commit := "b1".U
  // }
  //printf("io.debug.commit:%x\n",io.debug.commit)
  //printf("io.debug.rf_wdata:%x\n",io.debug.rf_wdata)
  // when(io.writeBackStage.data.pc === "h8000034c".U){
  //   printf("testtest_222\n\n")
  //   io.debug.rf_wdata := "h1010_1010_1010_1010".U
  // }
}
