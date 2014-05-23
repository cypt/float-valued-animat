package ru.nsu.alife.float_world_impl.world;

import ru.nsu.alife.float_world_impl.world.Apple;
import ru.nsu.alife.float_world_impl.world.World;

public interface IAppleFactory {
    Apple createApple(World world);
}
