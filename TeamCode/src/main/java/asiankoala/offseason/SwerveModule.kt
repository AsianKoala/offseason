package asiankoala.offseason

import com.acmerobotics.roadrunner.control.PIDFController
import com.asiankoala.koawalib.control.controller.PIDGains
import com.asiankoala.koawalib.control.controller.ProfiledPIDController
import com.asiankoala.koawalib.control.motor.FFGains
import com.asiankoala.koawalib.control.profile.MotionConstraints
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.math.Vector
import com.asiankoala.koawalib.subsystem.DeviceSubsystem
import kotlin.math.PI

class SwerveModule(
        private val motor1: KMotor,
        private val motor2: KMotor,
        private val startingAngle: Double
) : DeviceSubsystem() {
    companion object {
        private const val kCountsPerRev = 1.0
        private const val kGearRatio = 1.0
        private val kAngleConstant = (2 * PI) / (kCountsPerRev * kGearRatio)
        val velocityPID = PIDGains(0.0, 0.0, 0.0)

        const val kV = 0.0

        val turningPID = PIDGains(0.0, 0.0, 0.0)
        val turningFF = FFGains()
        val turningConstraints = MotionConstraints(60.0, 60.0)
    }

    private val velocityController = PIDFController(velocityPID.coeffs)
    private val turningController = ProfiledPIDController(turningPID, turningFF, turningConstraints)

    private val moduleState = SwerveState(0.0, startingAngle, 0.0)
    private val targetState = SwerveState(0.0, startingAngle, 0.0)
    private var motor1Vector = Vector()
    private var motor2Vector = Vector()

    fun updateModuleState() {
        val avg = (motor1.pos + motor2.pos) / 2
        moduleState.angle = avg * kAngleConstant
    }

    fun setModuleVelocity(vel: Double) {
        velocityController.targetPosition = vel
        targetState.v = vel
    }

    fun setModuleAngle(targetAngle: Double) {
        turningController.setTarget(moduleState.angle, targetAngle)
        targetState.angle = targetAngle
    }

    fun update() {
        val velocityOutput = velocityController.update(moduleState.v) + kV * targetState.v
        val turningOutput = turningController.update(moduleState.angle, moduleState.v)
    }

    init {
        motor1.zero(0.0)
        motor2.zero(0.0)
    }
}