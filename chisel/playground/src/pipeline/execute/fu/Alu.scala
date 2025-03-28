package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class Alu extends Module {
  val io = IO(new Bundle {
    val pc       = Input(UInt(XLEN.W))
    val info     = Input(new Info())
    val src_info = Input(new SrcInfo())
    val result   = Output(UInt(XLEN.W))
  })
  // TODO: 完成ALU模块的逻辑
  val ans = Wire(UInt(XLEN.W))
  val rs1 = Wire(UInt(XLEN.W))
  val rs2 = Wire(UInt(XLEN.W))
  val imm_u = Cat(io.info.imm(19,0), 0.U(12.W))
  rs1 := io.src_info.src1_data
  rs2 := io.src_info.src2_data
  ans := rs1 + rs2
    switch(io.info.op){
    is(ALUOpType.add){
      ans := rs1 + rs2 
    }
    is(ALUOpType.sub){ans := rs1 - rs2 }
    is(ALUOpType.sll){ans := rs1 << rs2(5, 0) }
    is(ALUOpType.slt){ans := (rs1.asSInt < rs2.asSInt).asUInt}
    is(ALUOpType.sltu){ans := rs1 < rs2}
    is(ALUOpType.xor){ans := rs1 ^ rs2}
    is(ALUOpType.srl){ans := rs1 >> rs2(5, 0)}
    is(ALUOpType.sra){ans := (rs1.asSInt >> rs2(5, 0)).asUInt}
    is(ALUOpType.or){ans := rs1 | rs2}
    is(ALUOpType.and){ans := rs1 & rs2}
    is(ALUOpType.addw){
      val result32 = (rs1(31,0) + rs2(31,0))(31,0)
      ans := Cat(Fill(32,result32(31)),result32)
    }
    is(ALUOpType.subw){
      val result32 = (rs1(31,0) - rs2(31,0))(31,0)
      ans := Cat(Fill(32,result32(31)),result32)
    }
    is(ALUOpType.sllw){
      val result32 = (rs1(31,0) << rs2(4,0))(31,0)
      ans := Cat(Fill(32,result32(31)),result32)
    }
    is(ALUOpType.srlw){
      val result32 = (rs1(31,0) >> rs2(4,0))
      ans := Cat(Fill(32,result32(31)),result32)
    }
    is(ALUOpType.sraw){
      val result32 = (rs1(31,0).asSInt >> rs2(4,0)).asUInt
      ans := Cat(Fill(32,result32(31)),result32)
    }
    is(ALUOpType.auipc){
      ans := (io.pc + imm_u)(31, 0)
    }
    is(ALUOpType.lui){
      val result32 = imm_u(31, 0)
      ans := Cat(Fill(32,result32(31)),result32)
    }
  }
  io.result := ans
}
