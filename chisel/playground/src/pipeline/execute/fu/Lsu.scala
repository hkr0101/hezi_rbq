package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class Lsu extends Module {
  val io = IO(new Bundle {
    val dataSram = new DataSram()
    val pc       = Input(UInt(XLEN.W))
    val info     = Input(new Info())
    val src_info = Input(new SrcInfo())
    val result   = Output(UInt(XLEN.W))
  })
  val rs1 = Wire(UInt(XLEN.W))
  val rs2 = Wire(UInt(XLEN.W))
  val imm_32 = Cat(Fill(20,io.info.imm(11)),io.info.imm)
  rs1 := io.src_info.src1_data
  rs2 := io.src_info.src2_data
  val en    = Wire(Bool())
  val addr  = Wire(UInt(SRAM_ADDR_WID.W))
  val wdata = Wire(UInt(DATA_SRAM_DATA_WID.W))
  val wen   = Wire(UInt(DATA_SRAM_WEN_WID.W))
  en       := !reset.asBool
  addr     := 0.U
  wdata    := 0.U
  wen      := 0.U
  io.result:= 0.U

  val DataMEM_add = rs1(31, 0) + imm_32

  io.dataSram.en    := 1.U
  io.dataSram.addr  := 0.U
  io.dataSram.wdata := 0.U
  io.dataSram.wen   := 0.U

  switch(io.info.op){
    is(LSUOpType.sh){
      //printf("Lsu_sh_addr:%x\n",DataMEM_add)
      addr  := DataMEM_add
      val data_temp = rs2(15,0)
      wdata := Cat(Fill(4, data_temp))
      
      val wen_temp = "b0000_0011".U << DataMEM_add(2,0)
      //printf("wen_temp:%x,DataMEM_add:%x\n",wen_temp,DataMEM_add)
      wen   := wen_temp
      io.dataSram.addr  := addr
      io.dataSram.wdata := wdata
      io.dataSram.wen   := wen
    }
    is(LSUOpType.sb){
      addr  := DataMEM_add
      val data_temp = rs2(7,0)
      wdata := Cat(Fill(8, data_temp))
      val wen_temp = "b0000_0001".U << DataMEM_add(2,0)
      wen   := wen_temp
      io.dataSram.addr  := addr
      io.dataSram.wdata := wdata
      io.dataSram.wen   := wen
    }
    is(LSUOpType.sw){
      addr  := DataMEM_add
      val data_temp = rs2(31,0)
      wdata := Cat(Fill(2, data_temp))
      val wen_temp = "b0000_1111".U << DataMEM_add(2,0)
      wen   := wen_temp
      io.dataSram.addr  := addr
      io.dataSram.wdata := wdata
      io.dataSram.wen   := wen
    }
    is(LSUOpType.sd){
      addr  := DataMEM_add
      wdata := rs2
      val wen_temp = "b1111_1111".U << DataMEM_add(2,0)
      wen   := wen_temp
      io.dataSram.addr  := addr
      io.dataSram.wdata := wdata
      io.dataSram.wen   := wen
    }
    is(LSUOpType.lb,LSUOpType.lbu,LSUOpType.lh,LSUOpType.lhu,LSUOpType.ld,LSUOpType.lw,LSUOpType.lwu){
      io.dataSram.addr  := DataMEM_add
      io.dataSram.wdata := 0.U
      io.dataSram.wen   := 0.U
      //printf("Lsu_lh_addr:%x\n",DataMEM_add)
    }
  }
  io.result := io.dataSram.rdata
  // printf("rs2:%x\n",rs2)
  // printf("io.dataSram.wdata:%x\npc:%x\n",io.dataSram.wdata,io.pc)
  // printf("io.dataSram.rdata:%x\n\n",io.dataSram.rdata)
}
