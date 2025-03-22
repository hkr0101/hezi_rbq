package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class DecodeUnit extends Module {
  val io = IO(new Bundle {
    // 输入
    val decodeStage = Flipped(new FetchUnitDecodeUnit())
    val regfile     = new Src12Read()
    // 输出
    val executeStage = Output(new DecodeUnitExecuteUnit())
  })

  val decoder = Module(new Decoder()).io
  decoder.in.inst := io.decodeStage.data.inst

  val pc   = io.decodeStage.data.pc 
  val info = Wire(new Info())

  info       := decoder.out.info
  info.valid := io.decodeStage.data.valid

  // TODO:完成寄存器堆的读取
  io.regfile.src1.raddr := io.decodeStage.data.inst(19,15)
  io.regfile.src2.raddr := io.decodeStage.data.inst(24,20) 

  // TODO: 完成DecodeUnit模块的逻辑
  io.executeStage.data.pc                 := pc
  io.executeStage.data.info               := info
  io.executeStage.data.src_info.src1_data := io.regfile.src1.rdata
  io.executeStage.data.src_info.src2_data := io.regfile.src2.rdata

  //printf("pc:%x 1:%x 2:%x \n",pc,io.regfile.src1.rdata,io.regfile.src2.rdata)

}
