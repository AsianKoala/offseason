package asiankoala.offseason

import com.asiankoala.koawalib.math.angleWrap
import com.asiankoala.koawalib.math.radians
import kotlin.math.absoluteValue

data class ModuleState(
        var wheelVel: Double,
        var azimuthAngle: Double,
) {
    companion object {
        fun optimizeTarget(currentState: ModuleState, targetState: ModuleState): ModuleState {
            val delta = (targetState.azimuthAngle - currentState.azimuthAngle).angleWrap.absoluteValue
            return if(delta > 90.0.radians) {
                return ModuleState(-targetState.wheelVel, (targetState.azimuthAngle + 180.0.radians).angleWrap)
            } else {
                targetState
            }
        }
    }
}