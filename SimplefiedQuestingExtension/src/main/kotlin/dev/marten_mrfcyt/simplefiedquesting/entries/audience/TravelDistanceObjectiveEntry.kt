package dev.marten_mrfcyt.simplefiedquesting.entries.audience
// TravelDistanceObjectiveEntry.kt
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
import dev.marten_mrfcyt.simplefiedquesting.enums.TravelType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*

@Entry("travel_objective", "An objective to travel a distance", Colors.BLUE_VIOLET, "mdi:run")
class TravelDistanceObjectiveEntry(
    override val id: String = "",
    override val name: String = "",
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    override val fact: Ref<CachableFactEntry> = emptyRef(),
    @Help("Travel method (walk, swim, fly, ride)")
    val travelType: TravelType = TravelType.TRAVELING,
    @Help("The amount of distance the player needs to travel.")
    override val amount: Var<Int> = ConstVar(1),
    override val display: Var<String> = ConstVar(""),
    override val onComplete: Ref<TriggerableEntry> = emptyRef(),
    override val priorityOverride: Optional<Int> = Optional.empty()
) : BaseCountObjectiveEntry {
    override suspend fun display(): AudienceFilter {
        return TravelDistanceObjectiveDisplay(ref())
    }
}

private class TravelDistanceObjectiveDisplay(ref: Ref<TravelDistanceObjectiveEntry>) :
    BaseCountObjectiveDisplay<TravelDistanceObjectiveEntry>(ref) {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.hasChangedBlock()) return
        val player = event.player
        val entry = ref.get() ?: return
        if (!filter(player)) return
        val isValidTravel = when (entry.travelType) {
            TravelType.SWIMMING -> player.isSwimming
            TravelType.FLYING -> player.isFlying
            TravelType.GLIDING -> player.isGliding
            TravelType.RIDING -> player.isInsideVehicle
            TravelType.SPRINTING -> player.isSprinting
            TravelType.RIPTIDING -> player.isRiptiding
            TravelType.SNEAKING -> player.isSneaking
            TravelType.DIVING -> player.isUnderWater && player.isSwimming
            TravelType.CLIMBING -> player.isClimbing
            TravelType.WALKING -> !player.isSwimming && !player.isFlying && !player.isGliding && !player.isInsideVehicle && !player.isSprinting && !player.isRiptiding && !player.isSneaking && !player.isUnderWater && !player.isClimbing
            TravelType.TRAVELING -> true
        }
        if (!isValidTravel) return
        val distance = event.from.distance(event.to)
        if (distance > 5) return // anti teleport
        incrementCount(player, kotlin.math.ceil(distance).toInt())
    }
}