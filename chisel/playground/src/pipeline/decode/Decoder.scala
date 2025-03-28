package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class Decoder extends Module with HasInstrType {
  val io = IO(new Bundle {
    // inputs
    val in = Input(new Bundle {val inst = UInt(XLEN.W)})
    // outputs
    val out = Output(new Bundle {val info = new Info()})
  })
// TODO: 完成Decoder模块的逻辑

//------------------------------------info初始化------------------------------------
  io.out.info := {
    val default = Wire(new Info())
    default.valid       := false.B 
    default.instrType   := InstrN
    default.op          := ALUOpType.add
    default.reg_wen     := false.B
    default.reg_waddr   := 0.U
    default.src1_raddr  := 0.U
    default.src2_raddr  := 0.U
    default.imm         := 0.U
    default.src1_ren    := false.B
    default.src2_ren    := false.B
    default.fusel       := FuType.alu
    default
  }
//---------------------------------------------------------------------------------
  Instructions.DecodeTable.foreach { 
  case (bitPat, ctrl) => 
    when (bitPat === io.in.inst) {
      io.out.info.valid     := true.B
      io.out.info.instrType := ctrl(0).asUInt
      io.out.info.op        := ctrl(2)
      io.out.info.fusel     := ctrl(1)
      io.out.info.reg_wen   := (ctrl(0) =/= InstrS) && (ctrl(0) =/= InstrB)
      io.out.info.reg_waddr := io.in.inst(11,7)
      io.out.info.src1_raddr := Mux(ctrl(0) === InstrU, 0.U, io.in.inst(19,15))
      io.out.info.src2_raddr := Mux(ctrl(0) === InstrI, 0.U, io.in.inst(24,20))
      io.out.info.imm := generateImm(ctrl(0), io.in.inst)
      io.out.info.src1_ren    := Mux(ctrl(0) === InstrU, 0.U, 1.U)
      io.out.info.src2_ren    := Mux(ctrl(0) === InstrI, 0.U, 1.U)
    }
  }
  private def generateImm(instrType: UInt, inst: UInt): UInt = {
    val imm = Wire(UInt(XLEN.W))
    imm := "h0".U
    switch(instrType) {
      is(InstrI) { imm := Cat(Fill(XLEN-12, inst(31)), inst(31,20)) }
      is(InstrU) { imm := Cat(Fill(XLEN-20, inst(31)), inst(31,12)) }
      // is(InstrS) { imm := Cat(Fill(XLEN-12, inst(31)), inst(31,25), inst(11,7)) }
      // is(InstrB) { imm := Cat(Fill(XLEN-13, inst(31)), inst(7), inst(30,25), inst(11,8), 0.U(1.W)) }
    }
    imm
  }


}
