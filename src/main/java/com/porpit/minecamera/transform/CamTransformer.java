package com.porpit.minecamera.transform;

import static org.objectweb.asm.Opcodes.RETURN;

import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.porpit.minecamera.MineCamera;

import net.minecraft.launchwrapper.IClassTransformer;

public class CamTransformer extends CamTransformerBase implements IClassTransformer {
	public CamTransformer() {
		super(MineCamera.MODID);
	}

	@Override
	protected void initTransformers() {
		addTransformer(new Transformer("net.minecraft.client.entity.EntityPlayerSP") {
			@Override
			public void transform(ClassNode node) {
				MethodNode m = findMethod(node, "isCurrentViewEntity", "()Z");
				m.instructions.clear();
				m.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
						"com/porpit/minecamera/transform/CamEventHandler", "shouldPlayerTakeInput", "()Z", false));
				m.instructions.add(new InsnNode(Opcodes.IRETURN));

			}
		});
		addTransformer(new Transformer("net.minecraft.client.renderer.EntityRenderer") {

			@Override
			public void transform(ClassNode node) {
				MethodNode m = findMethod(node, "getMouseOver", "(F)V");
				m.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC,
						"com/porpit/minecamera/transform/CamEventHandler", "setupMouseHandlerBefore", "()V", false));

				AbstractInsnNode currentNode = null;

				@SuppressWarnings("unchecked")
				Iterator<AbstractInsnNode> iter = m.instructions.iterator();

				while (iter.hasNext()) {
					currentNode = iter.next();
					if (currentNode instanceof InsnNode && ((InsnNode) currentNode).getOpcode() == RETURN) {
						m.instructions.insertBefore(currentNode,
								new MethodInsnNode(Opcodes.INVOKESTATIC,
										"com/porpit/minecamera/transform/CamEventHandler", "setupMouseHandlerAfter",
										"()V", false));
					}
				}

			}
		});
	}
}
