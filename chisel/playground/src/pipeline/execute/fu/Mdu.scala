package cpu.pipeline

import chisel3._
import chisel3.util._
import cpu.defines._
import cpu.defines.Const._

class Mdu extends Module {
  val io = IO(new Bundle {
    val pc       = Input(UInt(XLEN.W))
    val info     = Input(new Info())
    val src_info = Input(new SrcInfo())
    val result   = Output(UInt(XLEN.W))
  })

  val ans = Wire(UInt(XLEN.W))
  val rs1 = io.src_info.src1_data
  val rs2 = io.src_info.src2_data
  ans := 0.U
  switch(io.info.op) {
    is(MDUOpType.mul) {
      ans := rs1 * rs2
    }
    is(MDUOpType.mulh) {
      val result128 = (rs1.asSInt * rs2.asSInt).asUInt
      ans := result128(127, 64)
    }
    is(MDUOpType.mulhsu) {
      val result128 = (rs1.asSInt * rs2.asUInt).asUInt
      ans := result128(127, 64)
    }
    is(MDUOpType.mulhu) {
      val result128 = rs1 * rs2
      ans := result128(127, 64)
    }
    is(MDUOpType.div) {
      val dividend = rs1.asSInt
      val divisor = rs2.asSInt
      val result = Mux(divisor === 0.S, (-1).S, dividend / divisor)
      ans := result.asUInt
    }
    is(MDUOpType.divu) {
      val result = Mux(
        rs2 === 0.U,
        Cat(Fill(64, 1.U)),
        rs1 / rs2
      )
      ans := result
    }
    is(MDUOpType.rem) {
      val dividend = rs1.asSInt
      val divisor = rs2.asSInt
      val result = Mux(divisor === 0.S, dividend, dividend % divisor)
      ans := result.asUInt
    }
    is(MDUOpType.remu) {
      val result = Mux(rs2 === 0.U, rs1, rs1 % rs2)
      ans := result
    }
    is(MDUOpType.mulw) {
      val result32 = (rs1(31,0).asSInt * rs2(31,0).asSInt).asUInt(31,0)
      ans := Cat(Fill(32, result32(31)), result32)
    }
    is(MDUOpType.divw) {
      val dividend = rs1(31,0).asSInt
      val divisor = rs2(31,0).asSInt
      val result32 = Mux(divisor === 0.S, (-1).S, dividend / divisor).asUInt
      ans := Cat(Fill(32, rs1(63)), result32)
      printf("divw_ dividend:%x divisor:%x result32:%x\n",dividend,divisor,result32)
    }
    is(MDUOpType.divuw) {
      val rs1_32_signed = rs1(31, 0).asSInt  // 转为有符号数
      val rs2_32_signed = rs2(31, 0).asSInt
      val result32 = Mux(
        rs2_32_signed === 0.S,
        (-1).S(32.W),                      // 有符号全 1（-1）
        rs1_32_signed / rs2_32_signed
      )
      ans := Cat(Fill(32, result32(31)), result32)
    }
    is(MDUOpType.remw) {
      val dividend = rs1(31, 0).asSInt
      val divisor = rs2(31, 0).asSInt
      val q = dividend / divisor
      val remainder = Mux(divisor === 0.S, dividend, dividend - divisor * q)
      ans := Cat(Fill(32, rs1(63)), remainder)
      // printf("q: %x\n",q)
      // printf("remw_ dividend:%x,divisor:%x,remainder:%x,ans:%x\n",dividend,divisor,remainder,ans)
    }
    is(MDUOpType.remuw) {
      val result32 = Mux(rs2(31,0) === 0.U, rs1(31,0), rs1(31,0) % rs2(31,0))
      ans := Cat(Fill(32, result32(31)), result32)
    }
  }
  io.result := ans
}
