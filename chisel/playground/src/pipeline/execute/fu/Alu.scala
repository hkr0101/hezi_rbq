package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class Alu extends Module {
  val io = IO(new Bundle {
    val info     = Input(new Info())
    val src_info = Input(new SrcInfo())
    val result   = Output(UInt(XLEN.W))
  })
  // TODO: 完成ALU模块的逻辑
  val ans = Reg(UInt(XLEN.W))
  val rs1 = Reg(UInt(XLEN.W))
  val rs2 = Reg(UInt(XLEN.W))
  rs1 := io.src_info.src1_data
  rs2 := io.src_info.src2_data
  ans := "b0".U
    switch(io.info.op){
    is(ALUOpType.add){ans := rs1 + rs2 }
    is(ALUOpType.sub){ans := rs1 - rs2 }
    is(ALUOpType.sll){ans := rs1 << rs2(4, 0) }
    is(ALUOpType.slt){ans := (rs1.asSInt < rs2.asSInt).asUInt}
    is(ALUOpType.sltu){ans := rs1 < rs2}
    is(ALUOpType.xor){ans := rs1 ^ rs2}
    is(ALUOpType.srl){ans := rs1 >> rs2(4, 0)}
    is(ALUOpType.sra){ans := (rs1.asSInt >> rs2(4, 0)).asUInt}
    is(ALUOpType.or){ans := rs1 | rs2}
    is(ALUOpType.and){ans := rs1 & rs2}
    is(ALUOpType.addw){ans := (rs1 + rs2)(31, 0).asSInt.asUInt}
    is(ALUOpType.subw){ans := (rs1 - rs2)(31, 0).asSInt.asUInt}
    is(ALUOpType.sllw){ans := (rs1(31, 0) << rs2(4, 0))(31, 0).asSInt.asUInt}
    is(ALUOpType.srlw){ans := (rs1(31, 0).asUInt >> rs2(4, 0))(31, 0)}
    is(ALUOpType.sraw){ans := (rs1(31, 0).asSInt >> rs2(4, 0)).asUInt}
  }
  io.result := ans
}
