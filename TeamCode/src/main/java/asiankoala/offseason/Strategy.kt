package asiankoala.offseason

import asiankoala.offseason.subsystems.Arm
import asiankoala.offseason.subsystems.Outtake
import asiankoala.offseason.subsystems.Slides
import asiankoala.offseason.subsystems.Turret
import com.asiankoala.koawalib.logger.Logger
import com.asiankoala.koawalib.util.Alliance

class Strategy(val alliance: Alliance = Alliance.BLUE) {
    var strat = alliance.decide(Strats.ALLIANCE_BLUE, Strats.ALLIANCE_RED)

    val isAttackingOtherCrater: Boolean
        get() {
            return if(alliance == Alliance.BLUE) {
                strat == Strats.SHARED_RED
            } else {
                strat == Strats.SHARED_BLUE
            }
        }

    val isAlliance: Boolean
        get() = (strat == Strats.ALLIANCE_BLUE || strat == Strats.ALLIANCE_RED)

    val isShared: Boolean
        get() = !isAlliance

    val isExtendingImmediately: Boolean
        get() = isShared && !isAttackingOtherCrater

    fun getTurretAngle(): Double {
        return when {
            strat == Strats.SHARED_BLUE && alliance == Alliance.RED -> Turret.fuckyAngle
            strat == Strats.ALLIANCE_BLUE -> Turret.blueAngle
            strat == Strats.ALLIANCE_RED -> Turret.redAngle
            strat == Strats.SHARED_BLUE -> Turret.sharedBlueAngle
            strat == Strats.SHARED_RED -> Turret.sharedRedAngle
            else -> 0.0
        }
    }
    
    fun getSlideInches(): Double {
        return when {
            strat == Strats.ALLIANCE_BLUE -> Slides.blueDepositInches
            strat == Strats.ALLIANCE_RED -> Slides.redDepositInches
            else -> Slides.movingShared
        }
    }

    fun getArmPosition(): Double {
        return when(isShared) {
            true -> Arm.armSharedPosition
            false -> Arm.armHighPosition
        }
    }

    fun getOuttakePosition(): Double {
        return when(isShared) {
            true -> Outtake.outtakeSharedPosition
            false -> Outtake.outtakeHighPosition
        }
    }

    fun log() {
        Logger.addTelemetryData("strat", strat)
        Logger.addTelemetryData("is attacking other crater", isAttackingOtherCrater)
        Logger.addTelemetryData("is extending immediately", isExtendingImmediately)
        Logger.addTelemetryData("turret target angle", getTurretAngle())
        Logger.addTelemetryData("slide target inches", getSlideInches())
    }
}

