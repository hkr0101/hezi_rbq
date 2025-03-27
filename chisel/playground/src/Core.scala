package cpu

import chisel3._
import chisel3.util._

import defines._
import defines.Const._
import pipeline._

class Core extends Module {
  val io = IO(new Bundle {
    val interrupt = Input(new ExtInterrupt())
    val instSram  = new InstSram()
    val dataSram  = new DataSram()
    val debug     = new DEBUG()
  })

  val fetchUnit      = Module(new FetchUnit()).io
  val decodeStage    = Module(new DecodeStage()).io
  val decodeUnit     = Module(new DecodeUnit()).io
  val regfile        = Module(new ARegFile()).io
  val executeStage   = Module(new ExecuteStage()).io
  val executeUnit    = Module(new ExecuteUnit()).io
  val memoryStage    = Module(new MemoryStage()).io
  val memoryUnit     = Module(new MemoryUnit()).io
  val writeBackStage = Module(new WriteBackStage()).io
  val writeBackUnit  = Module(new WriteBackUnit()).io

  // printf("decode_pc:%x fet_cp: %x debug_pc:%x,\n\nex_cp:%x me_pc:%x wB_cp:%x\n\ndebug_wdata %x\n\n fet_inst: %x\n\n",
  // decodeUnit.decodeStage.data.pc,
  // fetchUnit.decodeStage.data.pc,
  // io.debug.pc,
  // executeUnit.executeStage.data.pc,
  // memoryUnit.memoryStage.data.pc,
  // writeBackUnit.writeBackStage.data.pc,
  // io.debug.rf_wdata,
  // fetchUnit.instSram.rdata)
  printf("fetch_cp:%x\n",fetchUnit.decodeStage.data.pc)
  printf("decode_pc:%x\n",decodeUnit.decodeStage.data.pc)
  printf("execute_cp:%x\n",executeUnit.executeStage.data.pc)
  printf("memory_cp:%x\n",memoryUnit.memoryStage.data.pc)
  printf("writeBack_cp:%x\n",writeBackUnit.writeBackStage.data.pc)
  printf("decode_unit_src1_data:%x\n",decodeUnit.executeStage.data.src_info.src1_data)
  printf("decode_unit_src2_data:%x\n",decodeUnit.executeStage.data.src_info.src2_data)
  printf("decode_unit_src1_addr:%x\n",decodeUnit.regfile.src1.raddr)
  printf("decode_unit_src2_addr:%x\n",decodeUnit.regfile.src2.raddr)
  printf("executeUnit_result:%x\n",executeUnit.memoryStage.data.rd_info.wdata)
  printf("decoder_op:%x\n",decodeUnit.executeStage.data.info.op)
  printf("memoryStage_wdata:%x\n",memoryStage.memoryUnit.data.rd_info.wdata)
  printf("executeUnit_wdata:%x\n",memoryStage.executeUnit.data.rd_info.wdata)
  printf("\n")
  printf("\n")
  
  // 取指单元
  fetchUnit.instSram <> io.instSram
  fetchUnit.decodeStage <> decodeStage.fetchUnit

  // TODO: 完成Core模块的逻辑
  decodeStage.decodeUnit <> decodeUnit.decodeStage
  decodeUnit.regfile <> regfile.read
  decodeUnit.executeStage <> executeStage.decodeUnit

  executeStage.executeUnit <> executeUnit.executeStage
  executeUnit.memoryStage <> memoryStage.executeUnit

  memoryStage.memoryUnit <> memoryUnit.memoryStage
  memoryUnit.writeBackStage <> writeBackStage.memoryUnit

  writeBackStage.writeBackUnit <> writeBackUnit.writeBackStage
  writeBackUnit.regfile <> regfile.write
  executeUnit.dataSram <> io.dataSram
  writeBackUnit.debug <> io.debug
  //printf("%x\n",io.debug.commit)
}
