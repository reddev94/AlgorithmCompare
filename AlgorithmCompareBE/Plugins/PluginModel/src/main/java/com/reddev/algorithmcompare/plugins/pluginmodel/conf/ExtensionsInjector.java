package com.reddev.algorithmcompare.plugins.pluginmodel.conf;

import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExtensionsInjector {
    Logger logger = LoggerFactory.getLogger(ExtensionsInjector.class);

    private static final String REGISTER_EXTENSION_AS_BEAN = "Register extension '{}' as bean";

    protected final SpringPluginManager springPluginManager;
    protected final AbstractAutowireCapableBeanFactory beanFactory;

    public ExtensionsInjector(SpringPluginManager springPluginManager, AbstractAutowireCapableBeanFactory beanFactory) {
        this.springPluginManager = springPluginManager;
        this.beanFactory = beanFactory;
    }

    public void injectExtensions() {
        // add extensions from classpath (non plugin)
        Set<String> extensionClassNames = springPluginManager.getExtensionClassNames(null);
        for (String extensionClassName : extensionClassNames) {
            try {
                logger.debug("Register extension '{}' as bean", extensionClassName);
                Class<?> extensionClass = getClass().getClassLoader().loadClass(extensionClassName);
                registerExtension(extensionClass);
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        }

        // add extensions for each started plugin
        List<PluginWrapper> startedPlugins = springPluginManager.getStartedPlugins();
        for (PluginWrapper plugin : startedPlugins) {
            logger.debug("Registering extensions of the plugin '{}' as beans", plugin.getPluginId());
            extensionClassNames = springPluginManager.getExtensionClassNames(plugin.getPluginId());
            for (String extensionClassName : extensionClassNames) {
                try {
                    logger.debug("Register extension '{}' as bean", extensionClassName);
                    Class<?> extensionClass = plugin.getPluginClassLoader().loadClass(extensionClassName);
                    registerExtension(extensionClass);
                } catch (ClassNotFoundException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Register an extension as bean.
     * Current implementation register extension as singleton using {@code beanFactory.registerSingleton()}.
     * The extension instance is created using {@code pluginManager.getExtensionFactory().create(extensionClass)}.
     * The bean name is the extension class name.
     * Override this method if you wish other register strategy.
     */
    protected void registerExtension(Class<?> extensionClass) {
        Map<String, ?> extensionBeanMap = springPluginManager.getApplicationContext().getBeansOfType(extensionClass);
        if (extensionBeanMap.isEmpty()) {
            Object extension = springPluginManager.getExtensionFactory().create(extensionClass);
            beanFactory.registerSingleton(extensionClass.getName(), extension);
        } else {
            logger.debug("Bean registeration aborted! Extension '{}' already existed as bean!", extensionClass.getName());
        }
    }
}
