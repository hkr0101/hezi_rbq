package cpu.pipeline

import chisel3._
import chisel3.util._
import chisel3.util.experimental.BoringUtils
import cpu.defines._
import cpu.defines.Const._
import cpu.CpuConfig

class MemoryUnit extends Module with HasInstrType{
  val io = IO(new Bundle {
    val memoryStage    = Input(new ExecuteUnitMemoryUnit())
    val writeBackStage = Output(new MemoryUnitWriteBackUnit())
    val dataS_rdata    = Input(UInt(DATA_SRAM_DATA_WID.W))
  })
  val lsu = Module(new M_Lsu()).io
  lsu.pc                                           := io.memoryStage.data.pc
  lsu.info                                         := io.memoryStage.data.info
  lsu.src_info                                     := io.memoryStage.data.src_info
  lsu.rdata                                        := io.dataS_rdata

  //printf("dataS_rdata:%x\n",io.dataS_rdata)
  io.writeBackStage.data.pc                        := io.memoryStage.data.pc
  io.writeBackStage.data.info                      := io.memoryStage.data.info
  io.writeBackStage.data.rd_info.wdata             := io.memoryStage.data.rd_info.wdata
  when(io.memoryStage.data.info.valid && io.memoryStage.data.info.fusel === FuType.lsu && io.memoryStage.data.info.instrType === InstrI){
    io.writeBackStage.data.rd_info.wdata           := lsu.result
  }
  //printf("Memory_Unit_result:%x\n",lsu.result)
  //printf("Memory_Unit_pc:%x\n\n",io.memoryStage.data.pc)
}
