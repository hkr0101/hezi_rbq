package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class Bru extends Module {
  val io = IO(new Bundle {
    val pc       = Input(UInt(XLEN.W))
    val info     = Input(new Info())
    val src_info = Input(new SrcInfo())
    val result   = Output(UInt(XLEN.W))
    val bra_info = Output(new BranchInfo())
  })

  io.result := 0.U
  io.bra_info.branch := 0.U
  io.bra_info.target := 0.U
  val rs1 = io.src_info.src1_data
  val rs2 = io.src_info.src2_data
  val target = Wire(UInt(XLEN.W))
  target := 0.U
  switch(io.info.op){
    is(BRUOpType.beq){
      when(rs1 === rs2){
        io.bra_info.branch := 1.U
        // printf("EX_BRU_target:%x\n",io.info.imm)
        // printf("pc:%x\n",io.pc)
      }
      target := io.info.imm
      
    }
    is(BRUOpType.bne){
      when(rs1 =/= rs2){
        io.bra_info.branch := 1.U
      }
      target := io.info.imm
    }
    is(BRUOpType.blt){
      when(rs1.asSInt < rs2.asSInt){
        io.bra_info.branch := 1.U
      }
      target := io.info.imm
    }
    is(BRUOpType.bge){
      when(rs1.asSInt >= rs2.asSInt){
        io.bra_info.branch := 1.U
      }
      target := io.info.imm
    }
    is(BRUOpType.bltu){
      when(rs1.asUInt < rs2.asUInt){
        io.bra_info.branch := 1.U
      }
      target := io.info.imm
    }
    is(BRUOpType.bgeu){
      when(rs1.asUInt >= rs2.asUInt){
        io.bra_info.branch := 1.U
      }
      target := io.info.imm
    }
    is(BRUOpType.jal){
      io.bra_info.branch := 1.U
      target := io.info.imm
      io.result := io.pc + 4.U

    }
    is(BRUOpType.jalr){
      io.bra_info.branch := 1.U
      target := (rs1 + io.info.imm) & (~1.U(XLEN.W))
      io.result := io.pc + 4.U
      // printf("test_test1\n")
      // printf("EX_BRU_target+pc:%x\n\n",target + io.pc)
      // printf("target:%x\n",target)
      // printf("pc:%x\n",io.pc)
    }
  }
  when(io.info.valid === 0.U){
    io.bra_info.branch := 0.U
  }
  io.bra_info.target := target + io.pc
  when(io.info.op === BRUOpType.jalr){
    io.bra_info.target := target
  }
  // printf("EX_BRU_target+pc:%x\n\n",target + io.pc)
}