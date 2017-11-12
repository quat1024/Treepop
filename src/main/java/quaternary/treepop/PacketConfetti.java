package quaternary.treepop;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketConfetti implements IMessage {
	BlockPos pos;
	
	//Pls
	public PacketConfetti() {}
	
	public PacketConfetti(BlockPos pos_) {
		pos = pos_;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
	}
	
	public static class Handler implements IMessageHandler<PacketConfetti, IMessage> {
		@Override
		public IMessage onMessage(PacketConfetti msg, MessageContext ctx) {
			for(int i=0; i < 60; i++) {
				Minecraft client = Minecraft.getMinecraft();
				
				double x = msg.pos.getX() + 0.5 + (Math.random()-0.5) * 4;
				double y = msg.pos.getY() + Math.random() * 4;
				double z = msg.pos.getZ() + 0.5 + (Math.random()-0.5) * 4;
				
				client.effectRenderer.spawnEffectParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), x, y, z, 0, 0.01, 0);
				
				//client.effectRenderer.addEffect(new ParticleConfetti(client.world, msg.pos));
			}
			return null;
		}
	}
}