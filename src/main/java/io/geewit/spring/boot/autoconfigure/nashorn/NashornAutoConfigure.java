package io.geewit.spring.boot.autoconfigure.nashorn;

import io.geewit.spring.boot.autoconfigure.nashorn.service.ScriptEvalService;
import io.geewit.spring.boot.autoconfigure.nashorn.service.ScriptEvalService;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


/**
 * @author geewit
 */
@Configuration
@ConditionalOnClass(ScriptEngine.class)
public class NashornAutoConfigure {

    @ConditionalOnMissingBean(ScriptEngineManager.class)
    @Bean
    public ScriptEngineManager scriptEngineManager() {
        return new ScriptEngineManager();
    }

    private NashornScriptEngine scriptEngine() {
        NashornScriptEngine scriptEngine = (NashornScriptEngine)scriptEngineManager().getEngineByName("nashorn");
        return scriptEngine;
    }

    @ConditionalOnMissingBean
    @Bean
    public ScriptEvalService scriptEvalService(ObjectProvider<PropertyResolver> propertyResolverProvider) {
        PropertyResolver propertyResolver = propertyResolverProvider.getIfAvailable();
        ScriptEvalService scriptEvalService = new ScriptEvalService(scriptEngine(), propertyResolver);
        return scriptEvalService;
    }
}
