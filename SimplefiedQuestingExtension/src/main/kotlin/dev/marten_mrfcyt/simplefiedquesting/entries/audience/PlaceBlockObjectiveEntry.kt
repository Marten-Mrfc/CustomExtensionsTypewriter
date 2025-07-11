package dev.marten_mrfcyt.simplefiedquesting.entries.audience

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
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.util.Vector
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Entry("place_block_objective", "An objective to place blocks", Colors.BLUE_VIOLET, "mdi:pickaxe")
class PlaceBlockObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    @Help("The item that the player needs to place.")
    val item: Var<Material> = ConstVar(Material.AIR),
    @Help("The amount of blocks to place.")
    override val amount: Var<Int> = ConstVar(1),
    override val display: Var<String> = ConstVar(""),
    override val onComplete: Ref<TriggerableEntry> = emptyRef(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return PlaceBlockObjectiveDisplay(ref())
    }
}

private class PlaceBlockObjectiveDisplay(ref: Ref<PlaceBlockObjectiveEntry>) :
    BaseCountObjectiveDisplay<PlaceBlockObjectiveEntry>(ref) {

    private val placedBlocks = ConcurrentHashMap.newKeySet<Vector>()

    @EventHandler(priority = EventPriority.MONITOR)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val entry = ref.get() ?: return
        if (!filter(player)) return
        val expected = entry.item.get(player)
        val block = event.block
        if (block.type != expected) return

        val location = block.location.toVector()
        if (!placedBlocks.add(location)) return // already placed here

        incrementCount(player)
    }
}
