package asiankoala.ftc2021.commands.subsystem

import asiankoala.offseason.subsystems.Arm
import com.asiankoala.koawalib.command.commands.InstantCmd

object ArmCommands {
    class ArmHomeCommand(arm: Arm) : InstantCmd(arm::home, arm)
    class ArmDepositHighCommand(arm: Arm) : InstantCmd(arm::depositHigh, arm)
    class ArmDepositSharedCommand(arm: Arm) : InstantCmd(arm::depositShared, arm)
}