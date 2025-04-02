package cpu.defines

import chisel3._
import chisel3.util._

trait HasInstrType {
  def InstrN = 0.U
  def InstrI = 1.U
  def InstrR = 2.U
  def InstrS = 3.U
  def InstrB = 4.U
  def InstrU = 5.U
  def InstrJ = 6.U
  def InstrSys = 7.U
  //def isRegWen(instrType: UInt): Bool = instrType(8)
}

object FuType {
  def num     = 5
  def alu     = 0.U
  def csr     = 1.U
  def mdu     = 2.U
  def lsu     = 3.U
  def bru     = 4.U
  def apply() = UInt(log2Up(num).W)
}

object FuOpType {
  def apply() = UInt(8.W)
}

// ALU
object ALUOpType {
  def add  = 0.U
  def sub  = 1.U
  def sll  = 2.U
  def slt  = 3.U
  def sltu = 4.U
  def xor  = 5.U
  def srl  = 6.U
  def sra  = 7.U
  def or   = 8.U
  def and  = 9.U

  def addw = 10.U
  def subw = 11.U
  def sllw = 12.U
  def srlw = 13.U
  def sraw = 14.U

  def ecall = 15.U
  def auipc = 16.U
  def lui = 17.U
  def isWordOp(func: UInt) = func >= 10.U && func <= 14.U
  // TODO: 定义更多的ALU操作类型
}

object MDUOpType{
  def mul = 0.U
  def mulh = 1.U
  def mulhsu = 2.U
  def mulhu = 3.U
  def div = 4.U
  def divu = 5.U
  def rem = 6.U
  def remu = 7.U
  def mulw = 8.U
  def divw = 9.U
  def divuw = 10.U
  def remw = 11.U
  def remuw =12.U
  // def isDiv(op:     Uint) = op(2)
  // def isDivSign(op: Uint) = isDiv(op) && !op(0)
  // def isWordOp(op:  Uint) = op(3)
}

object LSUOpType{
  def sh = 0.U
  def sb = 1.U
  def sw = 2.U
  def sd = 3.U

  def lb = 4.U
  def lbu = 5.U
  def lh = 6.U
  def lhu = 7.U
  def ld = 8.U
  def lw = 9.U
  def lwu = 10.U

  def isStore(func: UInt): Bool = func >= 0.U && func <=3.U
  def isLoad(func: UInt): Bool = func >= 4.U && func <=10.U
}

object BRUOpType{
  def beq = 0.U
  def bne = 1.U
  def blt = 2.U
  def bge = 3.U
  def bltu = 4.U
  def bgeu = 5.U
  def jal = 6.U
  def jalr = 7.U
  
}
