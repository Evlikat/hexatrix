package net.evlikat.hexatrix.entities;

public class HexagonalFieldTest {

    public void test() {

        HexagonalField jar = HexagonalField.generateJar(5, 9, new SpriteContext(32, null, null, null, null), new GameEventCallback() {

            @Override
            public void onLinesRemoved(int linesRemoved) {
            }

            @Override
            public void reset() {
            }

            @Override
            public long framesPerTick() {
                return 1;
            }
        });

    }
}