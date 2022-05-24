package asiankoala.reiko.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

class Slides(val motor: KMotor) : DeviceSubsystem() {
    companion object SlideConstants {
        const val slideHomeValue = 0.0
        const val blueDepositInches = 33.5
        const val redDepositInches = 35.0
        const val sharedInches = 7.5
        const val sharedExtInches = 12.0
        const val autoInches = 36.5 // 33.5
        var movingShared = 7.5
    }
}
