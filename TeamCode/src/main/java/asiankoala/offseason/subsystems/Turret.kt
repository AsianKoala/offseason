package asiankoala.offseason.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotorEx
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

class Turret(val motor: KMotorEx) : DeviceSubsystem() {
    companion object TurretConstants {
        private const val allianceDiff = 69.0

        const val zeroAngle = 183.0
        const val homeAngle = 183.0
        const val blueAngle = 180 + allianceDiff - 10.0
        const val redAngle = 180.0 - allianceDiff

        const val sharedBlueAngle = 90.0
        const val sharedRedAngle = 245.0

        private const val autoDiff = 64.0
        const val autoBlueAngle = 180 + autoDiff - 10.0 // blueAngle
        const val autoRedAngle = 180 - autoDiff - 10.0 // redAngle

        const val fuckyAngle = 80.0
    }
}
