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
  io.out.info.valid := true.B
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
    
  }
  io.out.info.reg_wen   := isRegWen(instrType)
  io.out.info.reg_waddr := Mux((instrType === InstrS) || (instrType === InstrB), 0.U, rd)
  io.out.info.op := ALUOpType.add
  switch(opcode) {
    is("b0110011".U) {
      switch(funct3) {
        is("b000".U) {
          switch(funct7) {
            is("b0000000".U) {
              io.out.info.op := ALUOpType.add
            }
            is("b0100000".U) {
              io.out.info.op := ALUOpType.sub
            }
          }
        }
        is("b001".U) {
          io.out.info.op := ALUOpType.sll
        }
        is("b010".U) {
          io.out.info.op := ALUOpType.slt
        }
        is("b011".U) {
          io.out.info.op := ALUOpType.sltu
        }
        is("b100".U){
          io.out.info.op := ALUOpType.xor
        }
        is("b101".U){
          switch(funct7) {
            is("b0000000".U) {
              io.out.info.op := ALUOpType.srl
            }
            is("b0100000".U) {
              io.out.info.op := ALUOpType.sra
            }
          }
        }
        is("b110".U){
          io.out.info.op := ALUOpType.or
        }
        is("b111".U){
          io.out.info.op := ALUOpType.and
        }
      }
    }
    is("b0111011".U) {
      switch(funct3){
        is("b000".U){
          switch(funct7) {
            is("b0000000".U) {
              io.out.info.op := ALUOpType.addw
            }
            is("b0100000".U) {
              io.out.info.op := ALUOpType.subw
            }
          }
        }
        is("b001".U){
          io.out.info.op := ALUOpType.sllw
        }
        is("b101".U){
          switch(funct7) {
            is("b0000000".U) {
              io.out.info.op := ALUOpType.srlw
            }
            is("b0100000".U) {
              io.out.info.op := ALUOpType.sraw
            }
          }
        }
      }
    }
  }
}
