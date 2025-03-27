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
  io.dataSram.addr  := DontCare
  io.dataSram.wdata := DontCare
  io.dataSram.wen   := 0.U

  val result = WireInit(0.U(XLEN.W))
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
  }
 
  io.data.rd_info.wdata := result
}
