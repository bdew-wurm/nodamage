package net.bdew.wurm.nodamage;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import java.util.logging.Logger;

public class NoDamageMod implements WurmServerMod, Initable, PreInitable {
    private static final Logger logger = Logger.getLogger("NoDamageMod");


    private void changeClass(CtClass cls) throws NotFoundException, CannotCompileException {
        cls.getMethod("setDamage", "(F)Z").insertBefore("if($1<100f) $1 = 0f;");
        logger.info(String.format("Disabled damage on %s", cls.getName()));
    }

    @Override
    public void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            changeClass(classPool.getCtClass("com.wurmonline.server.structures.DbBridgePart"));
            changeClass(classPool.getCtClass("com.wurmonline.server.structures.DbFence"));
            changeClass(classPool.getCtClass("com.wurmonline.server.structures.DbFloor"));
            changeClass(classPool.getCtClass("com.wurmonline.server.structures.DbWall"));
            changeClass(classPool.getCtClass("com.wurmonline.server.items.DbItem"));
        } catch (CannotCompileException | NotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void init() {
    }
}
