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
    is(ALUOpType.add , ALUOpType.sub , ALUOpType.sll , ALUOpType.slt ,
    ALUOpType.sltu , ALUOpType.xor , ALUOpType.srl , ALUOpType.sra ,
    ALUOpType.or , ALUOpType.and , ALUOpType.addw , ALUOpType.subw ,
    ALUOpType.sllw , ALUOpType.srlw , ALUOpType.sraw, ALUOpType.auipc, ALUOpType.lui, 
    MDUOpType.mul, MDUOpType.mulh, MDUOpType.mulhsu, MDUOpType.mulhu, MDUOpType.div,
    MDUOpType.divu, MDUOpType.rem, MDUOpType.remu, MDUOpType.mulw, MDUOpType.divw,
    MDUOpType.divuw, MDUOpType.remw, MDUOpType.remuw){
      data := io.executeUnit.data
      
      //printf("testest\n")
    }
  }
  io.memoryUnit.data := data
}
