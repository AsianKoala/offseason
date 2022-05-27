package asiankoala.offseason

import com.acmerobotics.roadrunner.control.PIDFController
import com.asiankoala.koawalib.control.controller.PIDGains
import com.asiankoala.koawalib.control.controller.ProfiledPIDController
import com.asiankoala.koawalib.control.motor.FFGains
import com.asiankoala.koawalib.control.profile.MotionConstraints
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.subsystem.DeviceSubsystem
import kotlin.math.PI

class SwerveModule(
        private val leftMotor: KMotor,
        private val rightMotor: KMotor,
        private val startingAngle: Double
) : DeviceSubsystem() {
    companion object {
        private const val kCountsPerRev = 1.0
        private const val kGearRatio = 1.0
        private const val kAngleConstant = (2 * PI) / (kCountsPerRev * kGearRatio)
        private const val kRadius = 1.0

        private const val kV = 0.0
        private val velocityPID = PIDGains(0.0, 0.0, 0.0)
        private val turningPID = PIDGains(0.0, 0.0, 0.0)
        private val turningFF = FFGains()
        private val turningConstraints = MotionConstraints(60.0, 60.0)
    }

    private var _state = ModuleState(0.0, startingAngle)
    private var _target = ModuleState(0.0, startingAngle)

    private val wheelVelController = PIDFController(velocityPID.coeffs)
    private val azimuthAngleController = ProfiledPIDController(turningPID, turningFF, turningConstraints)

    private var _lastWheelVelControllerOutput = 0.0
    private var _lastAzimuthAngleControllerOutput = 0.0

    private var _azimuthVel = 0.0

    private fun updateWheelVel() {
        _state.wheelVel = ((leftMotor.vel - rightMotor.vel) / 2) * kAngleConstant
    }

    private fun updateAzimuthAngle() {
        _state.azimuthAngle = ((leftMotor.pos + rightMotor.pos) / 2) * kAngleConstant + startingAngle
    }

    private fun updateAzimuthOmega() {
        _azimuthVel = ((leftMotor.vel + rightMotor.vel) / 2) * kAngleConstant
    }

    private fun updateModuleState() {
        updateAzimuthAngle()
        updateAzimuthOmega()
        updateWheelVel()
    }

    private fun updateWheelVelController() {
        _lastWheelVelControllerOutput = wheelVelController.update(_state.wheelVel) + kV * _target.wheelVel
    }

    private fun updateAzimuthAngleController() {
        _lastAzimuthAngleControllerOutput = azimuthAngleController.update(_state.azimuthAngle, _azimuthVel)
    }

    fun setTargetModuleState(target: ModuleState) {
        _target = ModuleState.optimizeTarget(_state, target)
        wheelVelController.targetPosition = _target.wheelVel
        azimuthAngleController.setTarget(_state.azimuthAngle, _target.azimuthAngle)
    }

    fun update() {
        updateModuleState()
        updateWheelVelController()
        updateAzimuthAngle()
    }

    init {
        leftMotor.zero(0.0)
        rightMotor.zero(0.0)
    }
}