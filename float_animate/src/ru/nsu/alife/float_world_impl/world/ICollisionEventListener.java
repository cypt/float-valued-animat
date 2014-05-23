package ru.nsu.alife.float_world_impl.world;

public interface ICollisionEventListener {
    public void onCollisionDetected(final IObject o1, final IObject o2);
}
