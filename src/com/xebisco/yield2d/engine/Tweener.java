package com.xebisco.yield2d.engine;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Tweener extends Script {

    private class PointMod {
        private final Object scriptObject;
        private final Field variableField;
        private Float startedValue = null;

        public PointMod(Object scriptObject, Field variableField) {
            this.scriptObject = scriptObject;
            this.variableField = variableField;
        }

        public Object getScriptObject() {
            return scriptObject;
        }

        public Field getVariableField() {
            return variableField;
        }

        public Float getStartedValue() {
            return startedValue;
        }

        public void setStartedValue(Float startedValue) {
            this.startedValue = startedValue;
        }
    }

    private TweeningInfo playingInfo;
    private Set<TweeningInfo> loadedInfos = new HashSet<>();

    private float time;
    private Map<TweeningInfo.TweeningPoint, PointMod> scriptObjectsMap;

    public Tweener() {
    }

    @Override
    public void init() {
        scriptObjectsMap = new HashMap<>();
    }

    @Override
    public void update(TimeSpan elapsed) {
        if (playingInfo != null) {
            playingInfo.getTweeningPoints().stream().parallel().forEach(point -> {
                try {
                    PointMod mod = scriptObjectsMap.get(point);
                    if (time >= point.startTime() && time <= point.startTime() + point.durationValue()) {
                        if (mod.getStartedValue() == null)
                            mod.setStartedValue(Float.parseFloat(mod.getVariableField().get(mod.getScriptObject()).toString()));
                        setVarValue(point.easing().call(point.easingEquations(), time - point.startTime(), mod.getStartedValue(), point.finalValue() - mod.getStartedValue(), point.durationValue()), mod);
                    } else if (time >= point.startTime() && mod.getStartedValue() != null) {
                        setVarValue(point.easing().call(point.easingEquations(), point.durationValue(), mod.getStartedValue(), point.finalValue() - mod.getStartedValue(), point.durationValue()), mod);
                        mod.setStartedValue(null);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            time += elapsed.getSeconds();
        }
    }

    private void loadInfo(TweeningInfo info) {
        try {
            for (TweeningInfo.TweeningPoint point : info.getTweeningPoints()) {
                Script script = getScript(point.scriptClass(), point.scriptIndex());
                Field field = script.getClass().getDeclaredField(point.varName());
                field.setAccessible(true);
                scriptObjectsMap.put(point, new PointMod(script, field));
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        loadedInfos.add(info);
    }

    public void play(TweeningInfo info) {
        if(!loadedInfos.contains(info)) loadInfo(info);
        playingInfo = info;
        time = 0;
    }

    private static void setVarValue(Float value, PointMod mod) throws IllegalAccessException {
        if (mod.getVariableField().getType().equals(Float.class) || mod.getVariableField().getType().equals(float.class) || mod.getVariableField().getType().equals(Double.class) || mod.getVariableField().getType().equals(double.class)) {
            mod.getVariableField().set(mod.getScriptObject(), value);
        } else if (mod.getVariableField().getType().equals(Integer.class) || mod.getVariableField().getType().equals(int.class)) {
            mod.getVariableField().set(mod.getScriptObject(), value.intValue());
        } else if (mod.getVariableField().getType().equals(Long.class) || mod.getVariableField().getType().equals(long.class)) {
            mod.getVariableField().set(mod.getScriptObject(), value.longValue());
        } else if (mod.getVariableField().getType().equals(Short.class) || mod.getVariableField().getType().equals(short.class)) {
            mod.getVariableField().set(mod.getScriptObject(), value.shortValue());
        } else if (mod.getVariableField().getType().equals(Byte.class) || mod.getVariableField().getType().equals(byte.class)) {
            mod.getVariableField().set(mod.getScriptObject(), value.byteValue());
        } else throw new IllegalArgumentException(mod.getVariableField().getType() + " is not supported.");
    }

    public TweeningInfo getPlayingInfo() {
        return playingInfo;
    }

    public Tweener setPlayingInfo(TweeningInfo playingInfo) {
        this.playingInfo = playingInfo;
        return this;
    }

    public float getTime() {
        return time;
    }

    public Tweener setTime(float time) {
        this.time = time;
        return this;
    }

    public Map<TweeningInfo.TweeningPoint, PointMod> getScriptObjectsMap() {
        return scriptObjectsMap;
    }

    public Tweener setScriptObjectsMap(Map<TweeningInfo.TweeningPoint, PointMod> scriptObjectsMap) {
        this.scriptObjectsMap = scriptObjectsMap;
        return this;
    }
}
