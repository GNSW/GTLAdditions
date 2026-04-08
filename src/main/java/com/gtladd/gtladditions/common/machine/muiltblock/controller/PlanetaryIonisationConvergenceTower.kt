package com.gtladd.gtladditions.common.machine.muiltblock.controller

import org.gtlcore.gtlcore.api.machine.trait.ICheckPatternMachine
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus
import org.gtlcore.gtlcore.api.recipe.RecipeResult
import org.gtlcore.gtlcore.common.data.GTLMaterials.*
import org.gtlcore.gtlcore.common.machine.multiblock.electric.StorageMachine

import com.gregtechceu.gtceu.api.block.ICoilType
import com.gregtechceu.gtceu.api.data.chemical.material.Material
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic
import com.gregtechceu.gtceu.common.data.GTMaterials
import com.gregtechceu.gtceu.utils.FormattingUtil

import com.lowdragmc.lowdraglib.side.fluid.FluidStack
import com.lowdragmc.lowdraglib.syncdata.annotation.DropSaved
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

import appeng.client.render.effects.ParticleTypes
import com.gtladd.gtladditions.api.machine.IEnergyMachine
import com.gtladd.gtladditions.api.machine.gui.MultiblockDisplayText
import com.gtladd.gtladditions.common.machine.muiltblock.controller.Resource.Cryotheum
import com.gtladd.gtladditions.common.machine.muiltblock.controller.Resource.HyperdimensionalDrone
import com.gtladd.gtladditions.utils.ComponentUtil.toComponent
import com.gtladd.gtladditions.utils.ComponentUtil.translatable
import com.gtladd.gtladditions.utils.MachineUtil.inputFluidStack
import com.gtladd.gtladditions.utils.MathUtil.minToLong
import com.gtladd.gtladditions.utils.Registries.getItem
import com.hepdd.gtmthings.api.misc.WirelessEnergyManager
import com.hepdd.gtmthings.data.CreativeMachines

import java.math.BigInteger
import java.util.*

class PlanetaryIonisationConvergenceTower(holder: IMachineBlockEntity) : StorageMachine(holder, 64), IMachineLife, IExplosionMachine {

    @Persisted
    private var storageEUt = 0L

    @Persisted
    @DropSaved
    private var isSuper = false

    @Persisted
    private var uuid: UUID? = null

    @Persisted
    private var cycleAmount = 0

    @Persisted
    private var startCycle = false

    private var coilEnergy: CoilToEnergy? = null
    private var stellarTier = 0
    private var maxStorageEUt = 0L

    init {
        this.isWorkingEnabled = false
    }

    fun addStorageEUt(start: Boolean): Long {
        val addEUt = if (start) {
            coilEnergy?.instantPower ?: 0
        } else {
            coilEnergy?.dischargePower ?: 0
        }
        storageEUt += addEUt
        if (storageEUt > maxStorageEUt) doExplosion(this.pos, 500f)
        return addEUt
    }

    fun renderParticles() {
        val serverLevel = level as ServerLevel
        val x = when (frontFacing) {
            Direction.WEST -> -7
            Direction.EAST -> 7
            else -> 0
        }
        val z = when (frontFacing) {
            Direction.SOUTH -> -7
            Direction.NORTH -> 7
            else -> 0
        }
        val pos = pos.offset(x, 20, z)
        serverLevel.sendParticles(ParticleTypes.LIGHTNING, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 200, 4.0, 4.0, 4.0, 0.01)
    }

    override fun filter(itemStack: ItemStack) = when (coilEnergy?.workTier) {
        1 -> itemStack.`is`(SpaceDroneMK2)
        2 -> itemStack.`is`(SpaceDroneMK4)
        3 -> itemStack.`is`(SpaceDroneMK6)
        else -> null
    } ?: false || itemStack.`is`(HyperdimensionalDrone)

    override fun onStructureFormed() {
        super.onStructureFormed()
        val context = this.multiblockState.matchContext
        val coilType = context.get("CoilType") as ICoilType
        this.stellarTier = context.get("SCTier")
        this.coilEnergy = findCoil(coilType.material)
        this.maxStorageEUt = when (this.stellarTier) {
            1 -> 0x4ec14566800
            2 -> 0xc587e7c983000
            3 -> 0x101925daa3740000
            else -> 0
        }
    }

    override fun onStructureInvalid() {
        super.onStructureInvalid()
        this.coilEnergy = null
        this.stellarTier = 0
        this.maxStorageEUt = 0
    }

    override fun onUse(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult {
        if (!world.isClientSide && !this.isSuper) {
            val stack = player.`kjs$getMainHandItem`()
            if (stack.`is`(CreativeMachines.CREATIVE_LASER_INPUT_HATCH.item)) {
                stack.shrink(1)
                player.`kjs$setMainHandItem`(if (stack.isEmpty) ItemStack.EMPTY else stack)
                this.isSuper = true
                if (!machineStorageItem.`is`(HyperdimensionalDrone)) this.clearInventory(this.machineStorage.storage)
            }
        }
        return super.onUse(state, world, pos, player, hand, hit)
    }

    override fun addDisplayText(textList: MutableList<Component>) {
        MultiblockDisplayText.builder(textList, isFormed)
            .setWorkingStatus(recipeLogic.isWorkingEnabled, recipeLogic.isActive)
            .addEnergyUsageLine(energyContainer)
            .addEnergyTierLine(tier)
            .addWorkingStatusLine()
            .addProgressLine(recipeLogic.progressPercent)
            .addComponent(
                Component.translatable("gui.gtladditions.planetary_ionisation_convergence_tower_1", (coilEnergy?.material?.unlocalizedName ?: "").translatable),
                Component.translatable("gui.gtladditions.planetary_ionisation_convergence_tower_2", this.stellarTier),
                Component.translatable("gui.gtladditions.planetary_ionisation_convergence_tower_3", FormattingUtil.formatNumbers(this.storageEUt))
            )
            .addRecipeStatus(recipeLogic as IRecipeStatus)
    }

    override fun attachConfigurators(configuratorPanel: ConfiguratorPanel) {
        configuratorPanel.attachConfigurators(
            IFancyConfiguratorButton.Toggle(
                GuiTextures.BUTTON_POWER.getSubTexture(0.0, 0.0, 1.0, 0.5),
                GuiTextures.BUTTON_POWER.getSubTexture(0.0, 0.5, 1.0, 0.5),
                this::isWorkingEnabled
            ) { clickData, pressed -> this.isWorkingEnabled = pressed }
                .setTooltipsSupplier { listOf((if (it) "behaviour.soft_hammer.enabled" else "behaviour.soft_hammer.disabled").toComponent) }
        )
        ICheckPatternMachine.attachConfigurators(configuratorPanel, self())
    }

    override fun onMachinePlaced(player: LivingEntity?, stack: ItemStack) {
        player?.let { this.uuid = it.uuid }
    }

    override fun shouldOpenUI(player: Player, hand: InteractionHand, hit: BlockHitResult): Boolean {
        if (this.uuid == null || this.uuid != player.uuid) this.uuid = player.uuid
        return true
    }

    override fun createRecipeLogic(vararg args: Any) = PICTRecipeLogic(this)

    override fun getFieldHolder() = MANAGED_FIELD_HOLDER

    override fun doExplosion(pos: BlockPos, explosionPower: Float) {
        val machine = this.self()
        machine.level?.let {
            it.removeBlock(machine.pos, false)
            it.explode(
                null,
                pos.x + 0.5,
                pos.y + 0.5,
                pos.z + 0.5,
                explosionPower,
                Level.ExplosionInteraction.BLOCK
            )
        }
    }

    class PICTRecipeLogic(val pictMachine: PlanetaryIonisationConvergenceTower) : RecipeLogic(pictMachine), IRecipeStatus {

        override fun serverTick() {
            duration = if (pictMachine.isSuper) 20 else 60
            if (!this.isSuspend) {
                if (progress < duration) this.handleRecipeWorking()
                if (progress >= duration) {
                    pictMachine.startCycle = false
                    progress = 0
                }
            } else if (subscription != null) {
                subscription.unsubscribe()
                subscription = null
            }
        }

        override fun handleRecipeWorking() {
            workingStatus = null
            pictMachine.renderParticles()
            if (progress == 0) {
                val droneResult = DroneResult(false, "")
                if (pictMachine.isSuper) {
                    pictMachine.startCycle = pictMachine.inputFluidStack(Miracle.getFluid(10))
                    if (pictMachine.startCycle && pictMachine.cycleAmount++ % 1000000 == 0) {
                        pictMachine.startCycle = pictMachine.machineStorageItem.`is`(HyperdimensionalDrone).also { droneResult.isDrone = it }
                        if (pictMachine.startCycle) {
                            pictMachine.machineStorage.extractItemInternal(0, 1, false)
                        } else {
                            pictMachine.cycleAmount--
                        }
                        droneResult.tier = HyperdimensionalDrone.descriptionId
                    }
                } else {
                    pictMachine.coilEnergy?.let {
                        when (it.workTier) {
                            1 -> {
                                pictMachine.startCycle = (
                                    pictMachine.inputFluidStack(GTMaterials.Rhenium.getFluid(73728)) &&
                                        pictMachine.inputFluidStack(GTMaterials.Ice.getFluid(8000000))
                                    )
                                if (pictMachine.startCycle && pictMachine.cycleAmount++ % 10000 == 0) {
                                    pictMachine.startCycle = pictMachine.machineStorageItem.`is`(SpaceDroneMK2).also { value -> droneResult.isDrone = value }
                                    if (pictMachine.startCycle) {
                                        pictMachine.machineStorage.extractItemInternal(0, 1, false)
                                    } else {
                                        pictMachine.cycleAmount--
                                    }
                                    droneResult.tier = SpaceDroneMK2.descriptionId
                                }
                            }
                            2 -> {
                                pictMachine.startCycle = (
                                    pictMachine.inputFluidStack(GTMaterials.Promethium.getFluid(36864)) &&
                                        pictMachine.inputFluidStack(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 4000000))
                                    )
                                if (pictMachine.startCycle && pictMachine.cycleAmount++ % 20000 == 0) {
                                    pictMachine.startCycle = pictMachine.machineStorageItem.`is`(SpaceDroneMK4).also { value -> droneResult.isDrone = value }
                                    if (pictMachine.startCycle) {
                                        pictMachine.machineStorage.extractItemInternal(0, 1, false)
                                    } else {
                                        pictMachine.cycleAmount--
                                    }
                                    droneResult.tier = SpaceDroneMK4.descriptionId
                                }
                            }
                            3 -> {
                                pictMachine.startCycle = (
                                    pictMachine.inputFluidStack(Crystalmatrix.getFluid(FluidStorageKeys.LIQUID, 9216)) &&
                                        pictMachine.inputFluidStack(FluidStack.create(Cryotheum, 1000000))
                                    )
                                if (pictMachine.startCycle && pictMachine.cycleAmount++ % 100000 == 0) {
                                    pictMachine.startCycle = pictMachine.machineStorageItem.`is`(SpaceDroneMK6).also { value -> droneResult.isDrone = value }
                                    if (pictMachine.startCycle) {
                                        pictMachine.machineStorage.extractItemInternal(0, 1, false)
                                    } else {
                                        pictMachine.cycleAmount--
                                    }
                                    droneResult.tier = SpaceDroneMK6.descriptionId
                                }
                            }
                        }
                    }
                }
                if (!droneResult.isDrone && !droneResult.tier.isEmpty()) {
                    workingStatus = RecipeResult.fail(Component.translatable("gui.gtladditions.planetary_ionisation_convergence_tower_4").append(droneResult.tier.toComponent))
                }
            }
            if (pictMachine.startCycle) {
                if (pictMachine.isSuper) {
                    WirelessEnergyManager.addEUToGlobalEnergyMap(pictMachine.uuid, maxEUt, pictMachine)
                } else {
                    val ecList = (pictMachine as IEnergyMachine).energyContainerList
                    val actualEUt = ecList.energyCanBeInserted minToLong pictMachine.addStorageEUt(progress == 0)
                    ecList.addEnergy(actualEUt)
                    pictMachine.storageEUt -= actualEUt
                }
                this.status = Status.WORKING
                ++this.progress
            } else {
                this.setWaiting(null)
            }
            if (this.status == Status.WAITING) this.doDamping()
        }

        internal class DroneResult(var isDrone: Boolean, var tier: String)
    }

    enum class CoilToEnergy(val material: Material, val instantPower: Long, val dischargePower: Long, val workTier: Int) {
        TITAN_STEEL(TitanSteel, 0x7fffffff00, 0x7fffffff, 1),
        ADAMANTINE(Adamantine, 0x7fffffff000, 0x7fffffff0, 1),
        NAQUADRIATIC_TARANIUM(NaquadriaticTaranium, 0x3fffffff8000, 0x7fffffff0, 2),
        STAR_METAL(Starmetal, 0x3fffffff80000, 0x7fffffff00, 2),
        INFINITY(Infinity, 0x7fffffff00000, 0x1fffffffc0, 3),
        HYPOGEN(Hypogen, 0x7fffffff000000, 0x1fffffffc00, 3),
        ETERNITY(Eternity, 0x7fffffff0000000, 0x1fffffffc000, 3)
    }

    companion object {
        val MANAGED_FIELD_HOLDER = ManagedFieldHolder(PlanetaryIonisationConvergenceTower::class.java, StorageMachine.MANAGED_FIELD_HOLDER)
        val maxEUt: BigInteger = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(64))
        val SpaceDroneMK2 = "kubejs:space_drone_mk2".getItem
        val SpaceDroneMK4 = "kubejs:space_drone_mk4".getItem
        val SpaceDroneMK6 = "kubejs:space_drone_mk6".getItem
        fun findCoil(material: Material?) = enumValues<CoilToEnergy>().find { it.material == material }
    }
}
