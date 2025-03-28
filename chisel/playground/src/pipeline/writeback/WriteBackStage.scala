package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._
import cpu.CpuConfig

class MemWbData extends Bundle {
  val pc      = UInt(XLEN.W)
  val info    = new Info()
  val rd_info = new RdInfo()
}

class MemoryUnitWriteBackUnit extends Bundle {
  val data = new MemWbData()
}
class WriteBackStage extends Module {
  val io = IO(new Bundle {
    val memoryUnit    = Input(new MemoryUnitWriteBackUnit())
    val writeBackUnit = Output(new MemoryUnitWriteBackUnit())
  })

  val data = RegInit(0.U.asTypeOf(new MemWbData()))
  
  // TODO: 完成WriteBackStage模块的逻辑
  //io.writeBackUnit.data := io.memoryUnit.data
  switch(io.memoryUnit.data.info.op){
    is(ALUOpType.add , ALUOpType.sub , ALUOpType.sll , ALUOpType.slt ,
    ALUOpType.sltu , ALUOpType.xor , ALUOpType.srl , ALUOpType.sra ,
    ALUOpType.or , ALUOpType.and , ALUOpType.addw , ALUOpType.subw ,
    ALUOpType.sllw , ALUOpType.srlw , ALUOpType.sraw, ALUOpType.auipc, ALUOpType.lui, 
    MDUOpType.mul, MDUOpType.mulh, MDUOpType.mulhsu, MDUOpType.mulhu, MDUOpType.div,
    MDUOpType.divu, MDUOpType.rem, MDUOpType.remu, MDUOpType.mulw, MDUOpType.divw,
    MDUOpType.divuw, MDUOpType.remw, MDUOpType.remuw){
      data := io.memoryUnit.data
    }
  }
  io.writeBackUnit.data := data
}
