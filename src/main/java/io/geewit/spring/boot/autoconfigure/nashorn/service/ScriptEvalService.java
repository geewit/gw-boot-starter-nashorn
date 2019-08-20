package io.geewit.spring.boot.autoconfigure.nashorn.service;

import io.geewit.core.exception.ProcessedException;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.NumberUtils;

import javax.script.Bindings;
import javax.script.ScriptException;

/**
 * @author geewit
 */
public class ScriptEvalService {
    private final static Logger logger = LoggerFactory.getLogger(ScriptEvalService.class);

    private final NashornScriptEngine scriptEngine;
    private final PropertyResolver propertyResolver;

    public ScriptEvalService(NashornScriptEngine scriptEngine, PropertyResolver propertyResolver) {
        this.scriptEngine = scriptEngine;
        this.propertyResolver = propertyResolver;
    }

    @SuppressWarnings({"unchecked","unused"})
    public <T>T eval(String script, Bindings bindings, Class<T> clazz) {
        try {
            Object result = this.scriptEngine.eval(script, bindings);
            if(clazz.isAssignableFrom(result.getClass())) {
                return (T) result;
            }
            return propertyResolver.getProperty(result.toString(), clazz);
        } catch (ScriptException e) {
            logger.warn(e.getMessage(), e);
            throw new ProcessedException(e.getMessage());
        }
    }

    @SuppressWarnings({"unchecked","unused"})
    public <T extends Number>T calc(String script, Object params, Class<T> clazz) {
        try {
            this.scriptEngine.eval(script);
            Object result = this.scriptEngine.invokeFunction("calc", params);
            if(clazz.isAssignableFrom(result.getClass())) {
                return (T) result;
            }
            if(Number.class.isAssignableFrom(result.getClass())) {
                return NumberUtils.parseNumber(result.toString(), clazz);
            }
            return null;
        } catch (ScriptException | NoSuchMethodException e) {
            logger.warn(e.getMessage(), e);
            throw new ProcessedException(e.getMessage());
        }
    }

    public Boolean test(String script, Object params) {
        try {
            this.scriptEngine.eval(script);
            Object result = this.scriptEngine.invokeFunction("calc", params);
            if(Boolean.class.isAssignableFrom(result.getClass())) {
                return (Boolean)result;
            }
            return null;
        } catch (ScriptException | NoSuchMethodException e) {
            logger.warn(e.getMessage(), e);
            throw new ProcessedException(e.getMessage());
        }
    }
}
