package dev.marten_mrfcyt.simplefiedquesting.entries.audience
// CraftObjectiveEntry.kt
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.quest.QuestEntry
import dev.marten_mrfcyt.simplefiedquesting.BaseCountObjectiveDisplay
import dev.marten_mrfcyt.simplefiedquesting.BaseCountObjectiveEntry
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.CraftItemEvent
import java.util.*

@Entry("craft_objective", "An objective to craft items", Colors.BLUE_VIOLET, "mdi:hammer")
class CraftObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    @Help("The item that the player needs to craft.")
    val item: Var<Material> = ConstVar(Material.AIR),
    @Help("The amount of items to craft.")
    override val amount: Var<Int> = ConstVar(1),
    override val display: Var<String> = ConstVar(""),
    override val onComplete: Ref<TriggerableEntry> = emptyRef(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return CraftObjectiveDisplay(ref())
    }
}

private class CraftObjectiveDisplay(ref: Ref<CraftObjectiveEntry>) :
    BaseCountObjectiveDisplay<CraftObjectiveEntry>(ref) {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onCraft(event: CraftItemEvent) {
        val player = event.whoClicked
        val entry = ref.get() ?: return
        if (player !is Player) return
        if (!filter(player)) return

        if (event.currentItem?.type == entry.item.get(player)) {
            incrementCount(player)
        }
    }
}