package com.xebisco.yield2d.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

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
    private Set<TweeningInfo> loadedInfos = new HashSet<>(), toLoad = new HashSet<>();
    private Set<TweeningInfo.TweeningPoint> zeroDuration = new HashSet<>();

    private float time;
    private Map<TweeningInfo.TweeningPoint, PointMod> scriptObjectsMap;

    public Tweener() {
    }

    @Override
    public void init() {
        scriptObjectsMap = new HashMap<>();
    }

    private static <T> List<Field> getAllFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        fields.removeIf(f -> Modifier.isStatic(f.getModifiers()));
        return fields;
    }

    @Override
    public void update(TimeSpan elapsed) {
        toLoad.removeIf(info -> {
            loadInfo(info);
            return true;
        });
        if (playingInfo != null) {
            float length = 0;
            for (TweeningInfo.TweeningPoint point : playingInfo.getTweeningPoints()) {
                length = Math.max(length, point.getStartTime() + point.getDurationValue());
                if (point.getScriptClass() == null) continue;
                try {
                    PointMod mod = scriptObjectsMap.get(point);
                    if (point.getDurationValue() == 0 && time >= point.getStartTime()) {
                        if (!zeroDuration.contains(point)) {
                            zeroDuration.add(point);
                            setVarValue(point.getFinalValue(), mod);
                            mod.setStartedValue(null);
                        }
                    } else if (time >= point.getStartTime() && time <= point.getStartTime() + point.getDurationValue()) {
                        if (mod.getStartedValue() == null)
                            mod.setStartedValue(Float.parseFloat(mod.getVariableField().get(mod.getScriptObject()).toString()));
                        setVarValue(point.getEasing().call(point.getEasingEquations(), time - point.getStartTime(), mod.getStartedValue(), point.getFinalValue(), point.getDurationValue()), mod);
                    } else if (time >= point.getStartTime() && mod.getStartedValue() != null) {
                        setVarValue(point.getEasing().call(point.getEasingEquations(), point.getDurationValue(), mod.getStartedValue(), point.getFinalValue(), point.getDurationValue()), mod);
                        mod.setStartedValue(null);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            time += elapsed.getSeconds();
            if (time > length + elapsed.getSeconds() && playingInfo.isLoop()) {
                setPlayingInfo(playingInfo);
            }
        }
    }

    private Object getO(Object script, Field actField, Field field) {
        if (script == null || script.getClass().isPrimitive() || (actField != null && actField.getType().isPrimitive()))
            return null;
        try {
            field.get(script);
            return script;
        } catch (Exception e) {
            for (Field f : getAllFields(script)) {
                try {
                    f.setAccessible(true);
                    Object o = getO(f.get(script), f, field);
                    if (o != null)
                        return o;
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return null;
    }

    private void loadInfo(TweeningInfo info) {
        for (TweeningInfo.TweeningPoint point : info.getTweeningPoints()) {
            if (point.getScriptClass() != null) {
                Script script = getScript(point.getScriptClass(), point.getScriptIndex());
                Field field = point.getField();
                field.setAccessible(true);
                Object so = getO(script, null, field);
                scriptObjectsMap.put(point, new PointMod(so, field));
            }
        }
        loadedInfos.add(info);
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
        if (!loadedInfos.contains(playingInfo)) {
            toLoad.add(playingInfo);
        }
        this.playingInfo = playingInfo;
        zeroDuration.clear();
        time = 0;
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
