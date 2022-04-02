package code;

import org.jnativehook.*;
import org.jnativehook.keyboard.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SingleClicker {

    private final static Integer BASE_DELAY = 7;
    private final static Integer PRESS_RNG_DELAY = 23;
    private final static Integer RELEASE_RNG_DELAY = 11;

    private final static Long MAX_EXECUTION_TIME = 30l * 60L * 1000L;

    private static Boolean ENABLED = Boolean.FALSE;
    private static Long timeout = System.currentTimeMillis() + MAX_EXECUTION_TIME;

    public static void main(String[] args) throws Exception {
        // Clear previous logging configurations.
        LogManager.getLogManager().reset();

        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

                @Override
                public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
                }

                @Override
                public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
                    String pressed = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
                    System.out.println("User typed: " + pressed + " at " + new Date());
                    if (pressed.equals("F4")) {
                        ENABLED = !ENABLED;
                        timeout = System.currentTimeMillis() + MAX_EXECUTION_TIME;
                    } else if (pressed.equals("Escape")) {
                        System.exit(0);
                    }
                }

                @Override
                public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
                }
            });
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        Robot robot = new Robot();
        Integer delay = 0;
        int count = 1;
        Long systime = 0L;
        while (true) {
            systime = System.currentTimeMillis();
            if (ENABLED) {
                count++;
                long currenttime = timeout - systime;

                delay = (int) (PRESS_RNG_DELAY * Math.random()) + BASE_DELAY;
                robot.delay(delay);
                robot.mousePress(InputEvent.BUTTON1_MASK);

                delay = (int) (RELEASE_RNG_DELAY * Math.random()) + BASE_DELAY;
                robot.delay(delay);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                System.out.println(count + "#\t" + ((int) (currenttime / 1000)) + "s\t" + InputEvent.BUTTON1_MASK +
                        +(currenttime % 1000) + "ms\tPress " + delay + "ms\tRelease " + delay + "ms");
            }
            robot.delay(1);
        }
    }
}
