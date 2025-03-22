package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class Decoder extends Module with HasInstrType {
  val io = IO(new Bundle {
    // inputs
    val in = Input(new Bundle {
      val inst = UInt(XLEN.W)
    })
    // outputs
    val out = Output(new Bundle {
      val info = new Info()
    })
  })
  // TODO: 完成Decoder模块的逻辑
  val opcode = io.in.inst(6, 0)
  val funct3 = io.in.inst(14, 12)
  val funct7 = io.in.inst(31, 25)
  val rs1    = io.in.inst(19, 15)
  val rs2    = io.in.inst(24, 20)
  val rd     = io.in.inst(11, 7)
  io.out.info.src1_raddr := rs1
  io.out.info.src2_raddr := rs2
  val instrType = Wire(UInt(3.W))
  instrType := InstrN 
  switch(opcode) {
    is("b0110011".U) {
      instrType := InstrR
    }
    is("b0111011".U) {
      instrType := InstrR
    }
    is("b0010011".U) {
    instrType := InstrI
    }
    is("b0011011".U) {
      instrType := InstrI
    }
    is("b0000011".U) {
      instrType := InstrI
    }
    is("b0100011".U) {
      instrType := InstrS
    }
    is("b1100011".U) {
      instrType := InstrB
    }
    is("b1101111".U) {
      instrType := InstrJ
    }
    is("b1100111".U) {
      instrType := InstrI
    }
    is("b0010111".U) {
      instrType := InstrU
    }
    is("b0110111".U) {
      instrType := InstrU
    }
    is("b0001111".U) {
      instrType := InstrI
    }
    is("b1110011".U) {
      when (funct3 =/= 0.U) {
        instrType := InstrI
      }.otherwise {
        instrType := InstrS
      }
    }
  }
  io.out.info.valid := (instrType =/= InstrN)
  io.out.info.reg_wen   := isRegWen(instrType)
  io.out.info.reg_waddr := Mux((instrType === InstrS) || (instrType === InstrB), 0.U, rd)
  io.out.info.op := ALUOpType.add
  //printf("op::%x %x\n",ALUOpType.add,io.out.info.op)
  def isWOpcode(opcode: UInt): Bool = opcode === "b0111011".U
  switch(opcode) {
  is("b0110011".U, "b0111011".U) { 
    val isW = isWOpcode(opcode)
    val combined = Cat(funct7(5), funct3)
    switch(combined) {
      is("b0_000".U)   {io.out.info.op := Mux(isW, ALUOpType.addw, ALUOpType.add)}
      is("b1_000".U)   {io.out.info.op := Mux(isW, ALUOpType.subw, ALUOpType.sub)}
      is("b0_001".U , "b1_001".U)   {io.out.info.op := Mux(isW, ALUOpType.sllw, ALUOpType.sll)}
      is("b0_010".U , "b1_010".U)   {io.out.info.op := ALUOpType.slt}
      is("b0_011".U , "b1_011".U)   {io.out.info.op := ALUOpType.sltu}
      is("b0_100".U , "b1_100".U)   {io.out.info.op := ALUOpType.xor}
      is("b0_110".U , "b1_110".U)   {io.out.info.op := ALUOpType.or}
      is("b0_111".U , "b1_111".U)   {io.out.info.op := ALUOpType.and}
      is("b0_101".U)   {io.out.info.op := Mux(isW, ALUOpType.srlw, ALUOpType.srl)}
      is("b1_101".U)   {io.out.info.op := Mux(isW, ALUOpType.sraw, ALUOpType.sra)}
    }
  }
}

}
