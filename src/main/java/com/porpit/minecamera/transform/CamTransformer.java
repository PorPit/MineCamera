package com.porpit.minecamera.transform;

import static org.objectweb.asm.Opcodes.RETURN;

import java.util.ArrayList;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.porpit.minecamera.MineCamera;

import net.minecraft.launchwrapper.IClassTransformer;

public class CamTransformer implements IClassTransformer {

	public CamTransformer() {
		initTransformers();
	}

	private final ArrayList<Transformer> transformers = new ArrayList<Transformer>();

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

	protected void addTransformer(Transformer transformer) {
		transformers.add(transformer);
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (name.contains("com.porpit"))
			return basicClass;
		return transform(transformedName, basicClass);
	}

	public byte[] transform(String name, byte[] basicClass) {
		int i = 0;
		while (i < transformers.size()) {
			if (transformers.get(i).is(name)) {
				ClassNode classNode = new ClassNode();
				ClassReader classReader = new ClassReader(basicClass);
				classReader.accept(classNode, 0);

				transformers.get(i).transform(classNode);

				ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES);
				classNode.accept(writer);
				basicClass = writer.toByteArray();

				System.out.println("[" + MineCamera.MODID + "] Patched " + transformers.get(i).className + " ...");
				transformers.get(i).done();
				i++;
				// transformers.remove(i);
			} else
				i++;
		}
		return basicClass;
	}
}
