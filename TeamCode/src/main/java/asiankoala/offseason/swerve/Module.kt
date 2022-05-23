package asiankoala.offseason.swerve

import com.asiankoala.koawalib.hardware.motor.KMotor

class Module(private val topMotor: KMotorEx, private val bottomMotor: KMotorEx) {
    val startingTop = topMotor.encoder.pos
    val startingBottom = bottomMotor.encoder.pos
}
