package dev.marten_mrfcyt.simplefiedquesting.entries.audience
// GatherObjectiveEntry.kt
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.snippets.snippet
import com.typewritermc.engine.paper.utils.asMini
import com.typewritermc.engine.paper.utils.asMiniWithResolvers
import com.typewritermc.engine.paper.utils.item.Item
import com.typewritermc.quest.ObjectiveEntry
import com.typewritermc.quest.QuestEntry
import com.typewritermc.quest.inactiveObjectiveDisplay
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.absoluteValue

private val gatherObjectiveDisplay by snippet(
    "quest.gather_objective.display",
    "<gold><display> <dark_gray>(<gray><current><dark_gray>/<gray><required><dark_gray>)"
)

private val completedGatherObjectiveDisplay by snippet(
    "quest.gather_objective.completed",
    "<green>✔</green> <gold><display> <dark_gray>(<gray><current><dark_gray>/<gray><required><dark_gray>)</gold>"
)

@Entry(
    "gather_objective",
    "A Gather Objective definition",
    Colors.BLUE_VIOLET,
    "material-symbols:shopping-basket-outline"
)
class GatherObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("The item that the player needs to gather.")
    val item: Var<Item> = ConstVar(Item.Empty),
    @Help("The amount of items to gather.")
    val amount: Var<Int> = ConstVar(1),
    override val display: Var<String> = ConstVar(""),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : ObjectiveEntry {

    private fun itemCount(player: Player): Int {
        val item = item.get(player)
        return player.inventory.contents.filterNotNull().filter { item.isSameAs(player, it) }.sumOf { it.amount }
    }

    override fun display(player: Player?): String {
        if (player == null) return inactiveObjectiveDisplay
        val itemCount = itemCount(player)
        val requiredAmount = amount.get(player).absoluteValue
        val isComplete = itemCount >= requiredAmount

        val text = if (isComplete) {
            completedGatherObjectiveDisplay
        } else {
            gatherObjectiveDisplay
        }

        return text.asMiniWithResolvers(
            parsed("display", display.get(player)),
            parsed("current", itemCount.toString()),
            parsed("required", requiredAmount.toString())
        ).asMini().parsePlaceholders(player)
    }
}