package asiankoala.ftc2021.opmodes

import com.asiankoala.koawalib.util.Alliance
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="Blue")
class HutaoBlueTeleOp : HutaoTeleOp(Alliance.BLUE)

@TeleOp(name="Red")
class HutaoRedTeleOp : HutaoTeleOp(Alliance.RED)
