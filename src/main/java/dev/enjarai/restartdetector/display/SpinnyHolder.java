package dev.enjarai.restartdetector.display;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import static dev.enjarai.restartdetector.ModMath.TO_RAD;

import com.mojang.math.Axis;

public abstract class SpinnyHolder extends ElementHolder {
    public final ServerLevel world;
    private final ItemDisplayElement spinnyElement = new ItemDisplayElement();
    private float spinniedness = 0f;

    public SpinnyHolder(ServerLevel world, ItemStack stack) {
        this.world = world;

        spinnyElement.setItem(stack);
        spinnyElement.setScale(new Vector3f(0.5f));
//        updateSpinny();
        spinnyElement.setTranslation(new Vector3f(0.0f, 0.25f, 0.0f));
        spinnyElement.setInterpolationDuration(getUpdateRate());

        addElement(spinnyElement);
    }

    private void updateSpinny() {
        spinniedness += getUpdateRate() * getSpeed();
        spinniedness = spinniedness % 360f;

        spinnyElement.setRightRotation(Axis.XP.rotationDegrees(-90)
                .rotateLocalY(spinniedness * TO_RAD));

        spinnyElement.setTranslation(new Vector3f(0.0f, 0.25f + (float) Math.sin(spinniedness * TO_RAD) * 0.05f, 0.0f));
    }

    @Override
    protected void onTick() {
        if (world.getGameTime() % getUpdateRate() == 0) {
            updateSpinny();
            spinnyElement.startInterpolation();
        }
    }

    public int getUpdateRate() {
        return 3;
    }

    public abstract float getSpeed();
}
