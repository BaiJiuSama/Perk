package cn.irina.perk.command

import cn.irina.perk.util.CC
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.exception.*
import revxrsal.commands.exception.MissingArgumentException
import revxrsal.commands.exception.NoPermissionException
import revxrsal.commands.exception.UnknownCommandException
import revxrsal.commands.node.ParameterNode


class CommandExceptionHandler: BukkitExceptionHandler() {
    override fun onMissingArgument(exception: MissingArgumentException, actor: BukkitCommandActor, parameter: ParameterNode<BukkitCommandActor, *>) { actor.error(CC.color("&#C42AAB参&#C82C75数&#CB2D3E不&#EF473A全")) }

    override fun onInvalidPlayer(e: InvalidPlayerException, actor: BukkitCommandActor) { actor.error(CC.color("&#C42AAB所&#C52B95选&#C72B7F择&#C82C6A的&#CA2C54玩&#CB2D3E家&#D2323D必&#D9373C须&#E13D3C在&#E8423B线&#EF473A.")) }

    override fun onNoPermission(e: NoPermissionException, actor: BukkitCommandActor) { actor.error(CC.color("&#CF1340你&#CE1740没&#CE1C3F有&#CD203F权&#CC243F限&#CC293E来&#CB2D3E使&#D2323D用&#D9373C此&#E13D3C指&#E8423B令&#EF473A!")) }

    override fun onSenderNotPlayer(e: SenderNotPlayerException, actor: BukkitCommandActor) { actor.error(CC.color("&#CF1340此指&#CE1C3F令必&#CC243F须由&#CB2D3E玩家&#DD3A3C来执&#EF473A行!")) }

    override fun onEmptyEntitySelector(e: EmptyEntitySelectorException, actor: BukkitCommandActor) { actor.error(CC.color("&#F2C6DE所选&#F2AED3中的&#F196C8实体&#EB7AB8不存&#E55EA8在.")) }

    override fun onMoreThanOneEntity(e: MoreThanOneEntityException?, actor: BukkitCommandActor) { actor.error(CC.color("&#F2C6DE只允&#F2BCDA许一&#F2B3D5个实&#F1A9D1体,&#F1A0CC 但&#F196C8提供&#EF8DC3的参&#ED83BD数中&#EB7AB8包含&#E971B3多个&#E767AD实体&#E767AD.")) }

    override fun onUnknownCommand(e: UnknownCommandException, actor: BukkitCommandActor) { actor.error(CC.color("&#F2C6DE未&#F2AED3知&#F196C8的&#EB7AB8命&#E55EA8令 (${e.input()})")) }
}