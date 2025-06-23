package dev.marten_mrfcyt.simplefiedquesting.enums

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

enum class EnchantmentType(private val key: String?) {
    PROTECTION("protection"),
    FIRE_PROTECTION("fire_protection"),
    FEATHER_FALLING("feather_falling"),
    BLAST_PROTECTION("blast_protection"),
    PROJECTILE_PROTECTION("projectile_protection"),
    RESPIRATION("respiration"),
    AQUA_AFFINITY("aqua_affinity"),
    THORNS("thorns"),
    DEPTH_STRIDER("depth_strider"),
    FROST_WALKER("frost_walker"),
    BINDING_CURSE("binding_curse"),
    SOUL_SPEED("soul_speed"),
    SWIFT_SNEAK("swift_sneak"),
    SHARPNESS("sharpness"),
    SMITE("smite"),
    BANE_OF_ARTHROPODS("bane_of_arthropods"),
    KNOCKBACK("knockback"),
    FIRE_ASPECT("fire_aspect"),
    LOOTING("looting"),
    SWEEPING("sweeping"),
    EFFICIENCY("efficiency"),
    SILK_TOUCH("silk_touch"),
    UNBREAKING("unbreaking"),
    FORTUNE("fortune"),
    POWER("power"),
    PUNCH("punch"),
    FLAME("flame"),
    INFINITY("infinity"),
    LUCK_OF_THE_SEA("luck_of_the_sea"),
    LURE("lure"),
    LOYALTY("loyalty"),
    IMPALING("impaling"),
    RIPTIDE("riptide"),
    CHANNELING("channeling"),
    MULTISHOT("multishot"),
    QUICK_CHARGE("quick_charge"),
    PIERCING("piercing"),
    MENDING("mending"),
    VANISHING_CURSE("vanishing_curse");

    fun toEnchantment(): Enchantment? {
        val enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
        return key?.let {
            val namespacedKey = NamespacedKey.minecraft(it)
            enchantmentRegistry.getOrThrow(TypedKey.create(RegistryKey.ENCHANTMENT, namespacedKey))
        }
    }

    companion object {
        fun fromEnchantment(enchantment: Enchantment): EnchantmentType? {
            return EnchantmentType.entries.find {
                val key = enchantment.key
                key.namespace == NamespacedKey.MINECRAFT && key.key == it.key
            }
        }
    }
}