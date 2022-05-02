package asiankoala.offseason.commands.subsystem

import asiankoala.offseason.subsystems.Outtake
import com.asiankoala.koawalib.command.commands.InstantCmd

object OuttakeCommands {
    class OuttakeHomeCommand(outtake: Outtake) : InstantCmd(outtake::home, outtake)
    class OuttakeCockCommand(outtake: Outtake) : InstantCmd(outtake::cock, outtake)
    class OuttakeDepositHighCommand(outtake: Outtake) : InstantCmd(outtake::depositHigh, outtake)
    class OuttakeDepositSharedCommand(outtake: Outtake) : InstantCmd(outtake::depositShared, outtake)
}