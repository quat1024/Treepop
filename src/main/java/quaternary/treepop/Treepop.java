package quaternary.treepop;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Treepop.MODID, version = Treepop.VERSION, name = Treepop.NAME)
@Mod.EventBusSubscriber
public class Treepop {
	public static final String MODID = "treepop";
	public static final String VERSION = "1";
	public static final String NAME = "Tree Pop";
	
	static SoundEvent popSound = null;
	
	private static final SimpleNetworkWrapper NET = new SimpleNetworkWrapper(MODID);
	
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent e) {
		MinecraftForge.TERRAIN_GEN_BUS.register(this);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		NET.registerMessage(PacketConfetti.Handler.class, PacketConfetti.class, 0, Side.CLIENT);
	}
	
	@SubscribeEvent
	public void textureStitch(TextureStitchEvent.Pre e) {
		//e.getMap().registerSprite(ParticleConfetti.confetti);
	}
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> e) {
		ResourceLocation popLocation = new ResourceLocation(MODID, "pop");
		popSound = new SoundEvent(popLocation).setRegistryName(popLocation.toString());
		
		e.getRegistry().register(popSound);
	}
	
	@SubscribeEvent
	public void newTree(SaplingGrowTreeEvent e) {
		World w = e.getWorld();
		if(w.isRemote) return;
		
		BlockPos pos = e.getPos();
		
		w.playSound(null, pos, popSound, SoundCategory.BLOCKS, 2, 1);
			
		PacketConfetti particlePacket = new PacketConfetti(pos);
		
		WorldServer ws = (WorldServer) w;
		for(EntityPlayer player : ws.playerEntities) {
			EntityPlayerMP pmp = (EntityPlayerMP) player;
			
			if(pmp.getDistanceSq(pos) < 64*64 && ws.getPlayerChunkMap().isPlayerWatchingChunk(pmp, pos.getX() >> 4, pos.getZ() >> 4)) {
				NET.sendTo(particlePacket, pmp);
			}
		}
	}
}
