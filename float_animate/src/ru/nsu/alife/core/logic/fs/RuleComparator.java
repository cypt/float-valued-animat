package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;
import ru.nsu.alife.core.logic.impl.statistics.volumes.MergedSubVolume;
import ru.nsu.alife.core.logic.impl.statistics.volumes.VolumeCell;

import java.util.Arrays;

public final class RuleComparator {

    private RuleComparator() {
    }

    public static boolean compare(final IRule r1, final IRule r2) {

        if (r1 == r2) {
            return true;
        }

        if (r1 == null || r2 == null) {
            return false;
        }

        if (r1.getClass() != r2.getClass()) {
            return false;
        }

        if (r1 instanceof Rule) {
            if (((Rule) r1).getAction() != ((Rule) r2).getAction()) {
                return false;
            }
        }

        IVolume v1 = r1.getSituationDescription();
        IVolume v2 = r2.getSituationDescription();

        if (v1 == v2) {
            return true;
        }

        if (v1 == null || v2 == null) {
            return false;
        }

        if (v1.getClass() != v2.getClass()) {
            return false;
        }

        if (v1.getClass() == VolumeCell.class) {
            return Arrays.equals(((VolumeCell) v1).getPoint(), ((VolumeCell) v2).getPoint());
        } else if (v1.getClass() == MergedSubVolume.class) {
            return v1 == v2;
        }

        return false;
    }

}
