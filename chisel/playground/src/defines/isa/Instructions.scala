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
  def isRegWen(instrType: UInt): Bool = instrType(8)
}

object FuType {
  def num     = 1
  def alu     = 0.U // arithmetic logic unit
  def csr     = 2.U
  def mdu     = 3.U
  def apply() = UInt(8.W)
}

object FuOpType {
  def apply() = UInt(8.W)
}

// ALU
object ALUOpType {
  def add  = 0.U//0
  def sub  = 1.U//8
  def sll  = 2.U//1
  def slt  = 3.U//2
  def sltu = 4.U//3
  def xor  = 5.U//4
  def srl  = 6.U//5
  def sra  = 7.U//13
  def or   = 8.U//6
  def and  = 9.U//7
  def addw = 10.U//16
  def subw = 11.U//24
  def sllw = 12.U//17
  def srlw = 13.U//21
  def sraw = 14.U//29
  def ecall = 15.U
  def auipc =16.U
  def lui =17.U
  def isWordOp(func: UInt) = func(8)
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
