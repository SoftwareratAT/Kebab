package org.kebab.api.entity;

public interface ArmorStand extends LivingEntity {
    boolean isSmall();

    void setSmall(boolean small);

    boolean showsArms();

    void showArms(boolean arms);

    boolean hasNoBasePlate();

    void setNoBasePlate(boolean noBasePlate);

    boolean isMarker();

    void setMarker(boolean marker);

    Rotation getHeadRotation();

    void setHeadRotation(Rotation headRotation);

    Rotation getBodyRotation();

    void setBodyRotation(Rotation bodyRotation);

    Rotation getLeftArmRotation();

    void setLeftArmRotation(Rotation leftArmRotation);

    Rotation getRightArmRotation();

    void setRightArmRotation(Rotation rightArmRotation);

    Rotation getLeftLegRotation();

    void setLeftLegRotation(Rotation leftLegRotation);

    Rotation getRightLegRotation();

    void setRightLegRotation(Rotation rightLegRotation);
}
