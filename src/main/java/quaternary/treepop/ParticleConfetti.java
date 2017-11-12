package quaternary.treepop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleConfetti extends Particle {
	public static final ResourceLocation confetti = new ResourceLocation(Treepop.MODID, "textures/confetti_particle.png");
	
	int initialY; //used for collisions (confetti only collides lower than its spawn point, to prevent it colliding with the tree, but still allowing it to land on the ground)
	
	public ParticleConfetti(World w, BlockPos pos) {
		super(w, pos.getX() + 0.5, pos.getY() + 0.5d, pos.getZ() + 0.5d);
		
		//adapted from https://math.stackexchange.com/questions/44689/
		double azimuth = Math.random() * Math.PI * 2;
		double height = Math.random(); //between 0 and 1 so particles always go upwards
		double coeff = Math.sqrt(1 - height*height);
		
		double speed = Math.random() + 0.4;
		
		motionX = coeff * Math.cos(azimuth) * speed;
		motionY = coeff * Math.sin(azimuth) * speed;
		motionZ = height * speed;
		//end
		
		particleMaxAge = (int) ((Math.random() * 30) + 100);
		
		initialY = pos.getY();
		
		particleAlpha = 0.9f;
	}
	
	@Override
	public void onUpdate() {
		//Copy from super
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}
		//End copy
		
		canCollide = posY < initialY;
		this.move(motionX, motionY, motionZ);
		
		if(!onGround) motionY -= 0.01;
		motionX *= 0.95;
		motionY = Math.max(-0.5, motionY);
		motionZ *= 0.95;
	}
	
	@Override
	public int getFXLayer() {
		return 2;
	}
	
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		Minecraft.getMinecraft().renderEngine.bindTexture(confetti);
		
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}
}
