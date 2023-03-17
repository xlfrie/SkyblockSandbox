package me.xlfrie.sbsandbox.Bosses.Custom;

@SuppressWarnings("SameReturnValue")
public interface CustomBossTemplate {
    int phases();

    default double phase(double percent) {
        return Math.max(1, Math.ceil(percent / (1 / 3F)));
    }

    int[] triggeredPhases = new int[10];
}
