package com.xebisco.yield2d.engine;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TweeningInfo implements Serializable {
    private final boolean loop;

    public interface EasingEquations {
        float easeIn(float time, float startingValue, float finalValue, float durationValue);

        float easeOut(float time, float startingValue, float finalValue, float durationValue);

        float easeInOut(float time, float startingValue, float finalValue, float durationValue);
    }

    public TweeningInfo(boolean loop, TweeningPoint... points) {
        this.loop = loop;
        tweeningPoints.addAll(Arrays.asList(points));
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

    public boolean isLoop() {
        return loop;
    }

    private final ArrayList<TweeningPoint> tweeningPoints = new ArrayList<>();

    public enum Easing {
        LINEAR(null),
        LINEAR_CLIP(null),
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

        private float linearFunc(float time, float startingValue, float finalValue, float durationValue) {
            return (finalValue - startingValue) * (time / durationValue) + startingValue;
        }

        public float call(EasingEquations equations, float time, float startingValue, float finalValue, float durationValue) {
            if (this == LINEAR) {
                return linearFunc(time, startingValue, finalValue, durationValue);
            }
            if (this == LINEAR_CLIP) {
                float val = linearFunc(time, startingValue, finalValue, durationValue);
                if (val - finalValue >= 0) {
                    val = finalValue - 0.0001f;
                }
                return val;
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

    public static class TweeningPoint implements Serializable {
        private final Class<? extends Script> scriptClass;
        private final int scriptIndex;
        private final Field field;
        private final float startTime;
        private final float finalValue;
        private final float durationValue;
        private final Easing easing;
        private final EasingEquations easingEquations;

        public TweeningPoint(Class<? extends Script> scriptClass, int scriptIndex, Field field, float startTime, float finalValue, float durationValue, Easing easing, EasingEquations easingEquations) {
            this.scriptClass = scriptClass;
            this.scriptIndex = scriptIndex;
            this.field = field;
            this.startTime = startTime;
            this.finalValue = finalValue;
            this.durationValue = durationValue;
            this.easing = easing;
            this.easingEquations = easingEquations;
        }

        public TweeningPoint(Class<? extends Script> scriptClass, int scriptIndex, String varName, float startTime, float finalValue, float durationValue, Easing easing, EasingEquations easingEquations) {
            this.scriptClass = scriptClass;
            this.scriptIndex = scriptIndex;
            try {
                this.field = scriptClass.getDeclaredField(varName);
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            this.startTime = startTime;
            this.finalValue = finalValue;
            this.durationValue = durationValue;
            this.easing = easing;
            this.easingEquations = easingEquations;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            TweeningPoint that = (TweeningPoint) o;
            return scriptIndex == that.scriptIndex && Float.compare(startTime, that.startTime) == 0 && Float.compare(finalValue, that.finalValue) == 0 && Float.compare(durationValue, that.durationValue) == 0 && Objects.equals(scriptClass, that.scriptClass) && Objects.equals(field, that.field) && easing == that.easing && Objects.equals(easingEquations, that.easingEquations);
        }

        @Override
        public int hashCode() {
            return Objects.hash(scriptClass, scriptIndex, field, startTime, finalValue, durationValue, easing, easingEquations);
        }

        public Class<? extends Script> getScriptClass() {
            return scriptClass;
        }

        public int getScriptIndex() {
            return scriptIndex;
        }

        public Field getField() {
            return field;
        }

        public float getStartTime() {
            return startTime;
        }

        public float getFinalValue() {
            return finalValue;
        }

        public float getDurationValue() {
            return durationValue;
        }

        public Easing getEasing() {
            return easing;
        }

        public EasingEquations getEasingEquations() {
            return easingEquations;
        }
    }

    public ArrayList<TweeningPoint> getTweeningPoints() {
        return tweeningPoints;
    }
}
