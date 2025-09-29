package com.xebisco.yield2d.engine;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class TweeningInfo implements Serializable {

    public enum Easing {
        LINEAR(null),
        EASE_IN(new Object() {
            Method eval() {
                try {
                    return EasingEquations.class.getDeclaredMethod("easeIn", float.class, float.class, float.class, float.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }.eval()),
        EASE_OUT(new Object() {
            Method eval() {
                try {
                    return EasingEquations.class.getDeclaredMethod("easeOut", float.class, float.class, float.class, float.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }.eval()),
        EASE_IN_OUT(new Object() {
            Method eval() {
                try {
                    return EasingEquations.class.getDeclaredMethod("easeInOut", float.class, float.class, float.class, float.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }.eval());

        private final Method equation;

        Easing(Method equation) {
            this.equation = equation;
        }

        public float call(EasingEquations equations, float time, float startingValue, float finalValue, float durationValue) {
            if (equation == null) {
                return (finalValue - startingValue) * (time / durationValue) + startingValue;
            }
            try {
                return (float) equation.invoke(equations, time, startingValue, finalValue, durationValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        public Method getEquation() {
            return equation;
        }
    }

    public interface EasingEquations {
        float easeIn(float time, float startingValue, float finalValue, float durationValue);

        float easeOut(float time, float startingValue, float finalValue, float durationValue);

        float easeInOut(float time, float startingValue, float finalValue, float durationValue);
    }


    public record TweeningPoint(
            Class<? extends Script> scriptClass,
            int scriptIndex,
            String varName,
            float startTime,
            float finalValue,
            float durationValue,
            Easing easing,
            EasingEquations easingEquations
    ) implements Serializable {
    }

    public static class Quad implements EasingEquations {
        @Override
        public float easeIn(float t, float b, float c, float d) {
            return c * (t /= d) * t + b;
        }

        @Override
        public float easeOut(float t, float b, float c, float d) {
            return -c * (t /= d) * (t - 2) + b;
        }

        @Override
        public float easeInOut(float t, float b, float c, float d) {
            if ((t /= d / 2) < 1) return c / 2 * t * t + b;
            return -c / 2 * ((--t) * (t - 2) - 1) + b;
        }
    }

    public static class Cubic implements EasingEquations {
        @Override
        public float easeIn(float t, float b, float c, float d) {
            return c * (t /= d) * t * t + b;
        }

        @Override
        public float easeOut(float t, float b, float c, float d) {
            return c * ((t = t / d - 1) * t * t + 1) + b;
        }

        @Override
        public float easeInOut(float t, float b, float c, float d) {
            if ((t /= d / 2) < 1) return c / 2 * t * t * t + b;
            return c / 2 * ((t -= 2) * t * t + 2) + b;
        }
    }

    public static class Back implements EasingEquations {
        @Override
        public float easeIn(float t, float b, float c, float d) {
            float s = 1.70158f;
            return c * (t /= d) * t * ((s + 1) * t - s) + b;
        }

        @Override
        public float easeOut(float t, float b, float c, float d) {
            float s = 1.70158f;
            return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
        }

        @Override
        public float easeInOut(float t, float b, float c, float d) {
            float s = 1.70158f;
            if ((t /= d / 2) < 1) return c / 2 * (t * t * (((s *= (1.525f)) + 1) * t - s)) + b;
            return c / 2 * ((t -= 2) * t * (((s *= (1.525f)) + 1) * t + s) + 2) + b;
        }
    }

    public static class Bounce implements EasingEquations {

        @Override
        public float easeIn(float t, float b, float c, float d) {
            return c - easeOut(d - t, 0, c, d) + b;
        }

        @Override
        public float easeOut(float t, float b, float c, float d) {
            if ((t /= d) < (1 / 2.75f)) {
                return c * (7.5625f * t * t) + b;
            } else if (t < (2 / 2.75f)) {
                return c * (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f) + b;
            } else if (t < (2.5 / 2.75)) {
                return c * (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f) + b;
            } else {
                return c * (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f) + b;
            }
        }

        @Override
        public float easeInOut(float t, float b, float c, float d) {
            if (t < d / 2) return easeIn(t * 2, 0, c, d) * .5f + b;
            else return easeOut(t * 2 - d, 0, c, d) * .5f + c * .5f + b;
        }

    }

    public static class Circ implements EasingEquations {

        @Override
        public float easeIn(float t, float b, float c, float d) {
            return -c * ((float) Math.sqrt(1 - (t /= d) * t) - 1) + b;
        }

        @Override
        public float easeOut(float t, float b, float c, float d) {
            return c * (float) Math.sqrt(1 - (t = t / d - 1) * t) + b;
        }

        @Override
        public float easeInOut(float t, float b, float c, float d) {
            if ((t /= d / 2) < 1) return -c / 2 * ((float) Math.sqrt(1 - t * t) - 1) + b;
            return c / 2 * ((float) Math.sqrt(1 - (t -= 2) * t) + 1) + b;
        }

    }

    public static class Sine implements EasingEquations {
        @Override
        public float easeIn(float t, float b, float c, float d) {
            return -c * (float) Math.cos(t / d * (Math.PI / 2)) + c + b;
        }

        @Override
        public float easeOut(float t, float b, float c, float d) {
            return c * (float) Math.sin(t / d * (Math.PI / 2)) + b;
        }

        @Override
        public float easeInOut(float t, float b, float c, float d) {
            return -c / 2 * ((float) Math.cos(Math.PI * t / d) - 1) + b;
        }
    }

    public static class Elastic implements EasingEquations {
        @Override
        public float easeIn(float t, float b, float c, float d) {
            if (t == 0) return b;
            if ((t /= d) == 1) return b + c;
            float p = d * .3f;
            float a = c;
            float s = p / 4;
            return -(a * (float) Math.pow(2, 10 * (t -= 1)) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p)) + b;
        }

        @Override

        public float easeOut(float t, float b, float c, float d) {
            if (t == 0) return b;
            if ((t /= d) == 1) return b + c;
            float p = d * .3f;
            float a = c;
            float s = p / 4;
            return (a * (float) Math.pow(2, -10 * t) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p) + c + b);
        }

        @Override
        public float easeInOut(float t, float b, float c, float d) {
            if (t == 0) return b;
            if ((t /= d / 2) == 2) return b + c;
            float p = d * (.3f * 1.5f);
            float a = c;
            float s = p / 4;
            if (t < 1)
                return -.5f * (a * (float) Math.pow(2, 10 * (t -= 1)) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p)) + b;
            return a * (float) Math.pow(2, -10 * (t -= 1)) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p) * .5f + c + b;
        }
    }

    private final ArrayList<TweeningPoint> tweeningPoints = new ArrayList<>();

    public TweeningInfo() {
    }

    public TweeningInfo(TweeningPoint... points) {
        tweeningPoints.addAll(Arrays.asList(points));
    }

    public ArrayList<TweeningPoint> getTweeningPoints() {
        return tweeningPoints;
    }
}
