package org.example;

import io.cucumber.java.Before;

public class Hooks {

    private static final ThreadLocal<TestWorld> WORLD = ThreadLocal.withInitial(TestWorld::new);

    public static TestWorld world() {
        return WORLD.get();
    }

    @Before
    public void beforeEach() {
        TestWorld w = WORLD.get();
        w.reset();
        w.seedBaseline();
    }
}
