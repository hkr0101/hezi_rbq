package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._
import cpu.CpuConfig

class Fu extends Module {
  val io = IO(new Bundle {
    val data = new Bundle {
      val pc       = Input(UInt(XLEN.W))
      val info     = Input(new Info())
      val src_info = Input(new SrcInfo())
      val rd_info  = Output(new RdInfo())
    }

    val dataSram = new DataSram()
  })
  io.dataSram.en    := false.B
  io.dataSram.addr  := 0.U
  io.dataSram.wdata := 0.U
  io.dataSram.wen   := 0.U
  val result = WireInit(0.U(XLEN.W))
  result := 0.U
  when(io.data.info.fusel === FuType.alu){
    val alu = Module(new Alu()).io
    alu.info     := io.data.info
    alu.src_info := io.data.src_info
    alu.pc       := io.data.pc
    result       := alu.result
  } .elsewhen(io.data.info.fusel === FuType.mdu){
    val mdu = Module(new Mdu()).io
    mdu.info     := io.data.info
    mdu.src_info := io.data.src_info
    mdu.pc       := io.data.pc
    result       := mdu.result
  } .elsewhen(io.data.info.fusel === FuType.lsu){
    val lsu = Module(new Lsu()).io
    lsu.info     := io.data.info
    lsu.src_info := io.data.src_info
    lsu.pc       := io.data.pc
    lsu.dataSram <> io.dataSram
    // io.dataSram.en    := lsu.en
    // io.dataSram.addr  := lsu.addr
    // io.dataSram.wdata := lsu.wdata
    // io.dataSram.wen   := lsu.wen
    result       := lsu.result
  }
 
  io.data.rd_info.wdata := result
}
