package dev.marten_mrfcyt.simplefiedquesting.entries.audience
// BreedMobObjectiveEntry.kt
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
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityBreedEvent
import java.util.*

@Entry("breed_mob_objective", "A objective to breed mobs", Colors.BLUE_VIOLET, "mdi:heart")
class BreedMobObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    @Help("The entity type that the player needs to breed.")
    val entityType: EntityType = EntityType.PIG,
    @Help("The amount of mobs needed to breed.")
    override val amount: Var<Int> = ConstVar(0),
    override val display: Var<String> = ConstVar(""),
    override val onComplete: Ref<TriggerableEntry> = emptyRef(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return BreedMobObjectiveDisplay(ref())
    }
}

private class BreedMobObjectiveDisplay(ref: Ref<BreedMobObjectiveEntry>) :
    BaseCountObjectiveDisplay<BreedMobObjectiveEntry>(ref) {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityBreed(event: EntityBreedEvent) {
        val breeder = event.breeder
        val entry = ref.get() ?: return

        if (breeder !is Player) return

        if (!filter(breeder)) return

        if (event.entityType == entry.entityType) {
            incrementCount(breeder)
        }
    }
}