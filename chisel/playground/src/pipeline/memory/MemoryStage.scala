package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._
import cpu.CpuConfig

class ExeMemData extends Bundle {
  val pc       = UInt(XLEN.W)
  val info     = new Info()
  val rd_info  = new RdInfo()
  val src_info = new SrcInfo()
}

class ExecuteUnitMemoryUnit extends Bundle {
  val data = new ExeMemData()
}

class MemoryStage extends Module {
  val io = IO(new Bundle {
    val executeUnit = Input(new ExecuteUnitMemoryUnit())
    val memoryUnit  = Output(new ExecuteUnitMemoryUnit())
  })

  val data = RegInit(0.U.asTypeOf(new ExeMemData()))

  
  // TODO: 完成MemoryStage模块的逻辑
  //io.memoryUnit.data := io.executeUnit.data
  switch(io.executeUnit.data.info.fusel){
    is(FuType.alu, FuType.mdu, FuType.lsu, FuType.bru){
      data := io.executeUnit.data
      //printf("testest\n")
    }
  }
  //printf("MEMS_io.executeUnit_wdata:%x\n\n",io.executeUnit.data.rd_info.wdata)
  // when(io.executeUnit.data.pc === "h8000034c".U){
  //   data.rd_info.wdata := "h1111_0000_0000_0000".U
  // }

  io.memoryUnit.data := data
  
}
