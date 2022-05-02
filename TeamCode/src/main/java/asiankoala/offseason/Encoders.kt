package asiankoala.offseason

import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import com.asiankoala.koawalib.subsystem.odometry.KThreeWheelOdometry

class Encoders(hardware: Hardware) {
    private val ticksPerUnit = 1892.3724

    private val leftEncoder = KEncoder(hardware.frMotor, ticksPerUnit, true).zero()
    private val rightEncoder = KEncoder(hardware.flMotor, ticksPerUnit, true).zero()
    private val auxEncoder = KEncoder(hardware.brMotor, ticksPerUnit, true).zero()

    private val trackWidth = 8.690685
    private val perpTracker = 6.4573 // 7.641969
    val odo = KThreeWheelOdometry(
            leftEncoder,
            rightEncoder,
            auxEncoder,
            trackWidth,
            perpTracker
    )
}