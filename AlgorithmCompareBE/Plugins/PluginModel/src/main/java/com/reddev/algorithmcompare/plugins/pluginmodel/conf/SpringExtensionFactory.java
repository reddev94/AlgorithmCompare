package com.reddev.algorithmcompare.plugins.pluginmodel.conf;

import org.pf4j.ExtensionFactory;
import org.pf4j.Plugin;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

public class SpringExtensionFactory implements ExtensionFactory {
    Logger logger = LoggerFactory.getLogger(SpringExtensionFactory.class);
    public static final boolean AUTOWIRE_BY_DEFAULT = true;
    private static final int AUTOWIRE_CONSTRUCTOR = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;

    /**
     * The plugin manager is used for retrieving a plugin from a given extension class
     * and as a fallback supplier of an application context.
     */
    protected final PluginManager pluginManager;
    /**
     * Indicates if springs autowiring possibilities should be used.
     */
    protected final boolean autowire;

    public SpringExtensionFactory(final PluginManager pluginManager) {
        this(pluginManager, AUTOWIRE_BY_DEFAULT);
    }

    public SpringExtensionFactory(final PluginManager pluginManager, final boolean autowire) {
        this.pluginManager = pluginManager;
        this.autowire = autowire;
        if (!autowire) {
            logger.warn("Autowiring is disabled although the only reason for existence of this special factory is" +
                    " supporting spring and its application context.");
        }
    }

    @Override
    public <T> T create(final Class<T> extensionClass) {
        if (!this.autowire) {
            logger.warn("Create instance of '" + nameOf(extensionClass) + "' without using springs possibilities as" +
                    " autowiring is disabled.");
            return createWithoutSpring(extensionClass);
        }

        return getApplicationContextBy(extensionClass)
                .map(applicationContext -> createWithSpring(extensionClass, applicationContext))
                .orElseGet(() -> createWithoutSpring(extensionClass));
    }

    @SuppressWarnings("unchecked")
    protected <T> T createWithSpring(final Class<T> extensionClass, final ApplicationContext applicationContext) {
        final AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        logger.debug("Instantiate extension class '" + nameOf(extensionClass) + "' by using constructor autowiring.");
        // Autowire by constructor. This does not include the other types of injection (setters and/or fields).
        final Object autowiredExtension = beanFactory.autowire(extensionClass, AUTOWIRE_CONSTRUCTOR,
                // The value of the 'dependencyCheck' parameter is actually irrelevant as the using constructor of 'RootBeanDefinition'
                // skips action when the autowire mode is set to 'AUTOWIRE_CONSTRUCTOR'. Although the default value in
                // 'AbstractBeanDefinition' is 'DEPENDENCY_CHECK_NONE', so it is set to false here as well.
                false);
        logger.trace("Created extension instance by constructor injection: " + autowiredExtension);
        logger.debug("Completing autowiring of extension: " + autowiredExtension);
        // Autowire by using remaining kinds of injection (e. g. setters and/or fields).
        beanFactory.autowireBean(autowiredExtension);
        logger.trace("Autowiring has been completed for extension: " + autowiredExtension);
        return (T) autowiredExtension;
    }

    protected <T> Optional<ApplicationContext> getApplicationContextBy(final Class<T> extensionClass) {
        final Plugin plugin = Optional.ofNullable(this.pluginManager.whichPlugin(extensionClass))
                .map(PluginWrapper::getPlugin)
                .orElse(null);
        final ApplicationContext applicationContext;
        if (plugin instanceof SpringPlugin) {
            logger.debug("  Extension class ' " + nameOf(extensionClass) + "' belongs to spring-plugin '" + nameOf(plugin)
                    + "' and will be autowired by using its application context.");
            applicationContext = ((SpringPlugin) plugin).getApplicationContext();
        } else if (this.pluginManager instanceof SpringPluginManager) {
            logger.debug("  Extension class ' " + nameOf(extensionClass) + "' belongs to a non spring-plugin (or main application)" +
                    " '" + nameOf(plugin) + ", but the used PF4J plugin-manager is a spring-plugin-manager. Therefore" +
                    " the extension class will be autowired by using the managers application contexts");
            applicationContext = ((SpringPluginManager) this.pluginManager).getApplicationContext();
        } else {
            logger.warn("  No application contexts can be used for instantiating extension class '" + nameOf(extensionClass) + "'."
                    + " This extension neither belongs to a PF4J spring-plugin (id: '" + nameOf(plugin) + "') nor is the used" +
                    " plugin manager a spring-plugin-manager (used manager: '" + nameOf(this.pluginManager.getClass()) + "')." +
                    " At perspective of PF4J this seems highly uncommon in combination with a factory which only reason for existence" +
                    " is using spring (and its application context) and should at least be reviewed. In fact no autowiring can be" +
                    " applied although autowire flag was set to 'true'. Instantiating will fallback to standard Java reflection.");
            applicationContext = null;
        }
        return Optional.ofNullable(applicationContext);
    }

    @SuppressWarnings("unchecked")
    protected <T> T createWithoutSpring(final Class<T> extensionClass) throws IllegalArgumentException {
        final Constructor<?> constructor = getPublicConstructorWithShortestParameterList(extensionClass)
                // An extension class is required to have at least one public constructor.
                .orElseThrow(() -> new IllegalArgumentException("Extension class '" + nameOf(extensionClass)
                        + "' must have at least one public constructor."));
        try {
            logger.debug("Instantiate '" + nameOf(extensionClass) + "' by calling '" + constructor + "'with standard Java reflection.");
            // Creating the instance by calling the constructor with null-parameters (if there are any).
            return (T) constructor.newInstance(nullParameters(constructor));
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            // If one of these exceptions is thrown it it most likely because of NPE inside the called constructor and
            // not the reflective call itself as we precisely searched for a fitting constructor.
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("Most likely this exception is thrown because the called constructor (" + constructor + ")" +
                    " cannot handle 'null' parameters. Original message was: "
                    + ex.getMessage(), ex);
        }
    }

    private Optional<Constructor<?>> getPublicConstructorWithShortestParameterList(final Class<?> extensionClass) {
        return Stream.of(extensionClass.getConstructors())
                .min(Comparator.comparing(Constructor::getParameterCount));
    }

    private Object[] nullParameters(final Constructor<?> constructor) {
        return new Object[constructor.getParameterCount()];
    }

    private String nameOf(final Plugin plugin) {
        return nonNull(plugin)
                ? plugin.getWrapper().getPluginId()
                : "system";
    }

    private <T> String nameOf(final Class<T> clazz) {
        return clazz.getName();
    }
}
