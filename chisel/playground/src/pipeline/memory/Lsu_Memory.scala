package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class M_Lsu extends Module {
  val io = IO(new Bundle {
    val pc       = Input(UInt(XLEN.W))
    val info     = Input(new Info())
    val src_info = Input(new SrcInfo())
    val rdata    = Input(UInt(DATA_SRAM_DATA_WID.W))
    val result   = Output(UInt(XLEN.W))
  })
  io.result      := 0.U
  val rs1 = Wire(UInt(XLEN.W))
  val rs2 = Wire(UInt(XLEN.W))
  val imm_32 = Cat(Fill(20,io.info.imm(11)),io.info.imm)
  rs1 := io.src_info.src1_data
  rs2 := io.src_info.src2_data
  val DataMEM_add = rs1(31,0) + imm_32
  val rdata_temp  = Wire(UInt(DATA_SRAM_DATA_WID.W))
  rdata_temp := 0.U

  switch(DataMEM_add(2, 0)){
    is("b000".U){
      rdata_temp := io.rdata
    }
    is("b001".U){
      rdata_temp := Cat(Fill(8,0.U(1.W)),io.rdata(63,8))
    }
    is("b010".U){
      rdata_temp := Cat(Fill(16,0.U(1.W)),io.rdata(63,16))
    }
    is("b011".U){
      rdata_temp := Cat(Fill(24,0.U(1.W)),io.rdata(63,24))
    }
    is("b100".U){
      rdata_temp := Cat(Fill(32,0.U(1.W)),io.rdata(63,32))
    }
    is("b101".U){
      rdata_temp := Cat(Fill(40,0.U(1.W)),io.rdata(63,40))
    }
    is("b110".U){
      rdata_temp := Cat(Fill(48,0.U(1.W)),io.rdata(63,48))
    }
    is("b111".U){
      rdata_temp := Cat(Fill(56,0.U(1.W)),io.rdata(63,56))
    }
  }
  switch(io.info.op){
    is(LSUOpType.lb){
      val result_temp = rdata_temp(7,0)
      io.result := Cat(Fill(56, rdata_temp(7)),result_temp)
    }
    is(LSUOpType.lbu){
      val result_temp = rdata_temp(7,0)
      io.result := Cat(Fill(56, 0.U(1.W)),result_temp)
    }
    is(LSUOpType.lh){
      
      val result_temp = rdata_temp(15,0)
      io.result := Cat(Fill(48, rdata_temp(15)),result_temp)
      //printf("result_temp:%x\n",result_temp)
    }
    is(LSUOpType.lhu){
      val result_temp = rdata_temp(15,0)
      io.result := Cat(Fill(48, 0.U(1.W)),result_temp)
    }
    is(LSUOpType.ld){
      io.result := rdata_temp
    }
    is(LSUOpType.lw){
      val result_temp = rdata_temp(31,0)
      io.result := Cat(Fill(32, rdata_temp(31)),result_temp)
    }
    is(LSUOpType.lwu){
      val result_temp = rdata_temp(31,0)
      io.result := Cat(Fill(32, 0.U(1.W)),result_temp)
    }
  }
}