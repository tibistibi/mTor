package nl.bhit.mtor.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * will make live easier for working with annotations.
 * 
 * @author tibi
 */
public class AnnotationUtil {
	private final static ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

	protected final static Log log = LogFactory.getLog(AnnotationUtil.class);

	/**
	 * will search for annotation of type searchForAnnotation within the basePackage
	 * 
	 * @param searchForAnnotation
	 *            the annotation to search for.
	 * @param basePackage
	 *            the package to search in (recursive).
	 * @return the found candidates.
	 */
	public static Set<BeanDefinition> findProviders(Class<? extends Annotation> searchForAnnotation, String basePackage) {
		provider.addIncludeFilter(new AnnotationTypeFilter(searchForAnnotation));
		provider.setResourceLoader(new PathMatchingResourcePatternResolver(AnnotationUtil.class.getClassLoader()));
		return provider.findCandidateComponents(basePackage);
	}

	/**
	 * will search for the method annotation of type searchForMethodAnnotation within the beanDefinition
	 * 
	 * @param searchForMethodAnnotation
	 *            the method annotation to search for.
	 * @param beanDefinition
	 *            the bean to search in.
	 * @return the found methods.
	 */
	public static List<Method> findMethods(Class<? extends Annotation> searchForMethodAnnotation, BeanDefinition beanDefinition) {
		List<Method> methods = new ArrayList<Method>();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(beanDefinition.getBeanClassName());
		} catch (ClassNotFoundException e) {
			log.warn("Class not found, return null.");
			log.debug("Error belonging to: class not found, return null.", e);
			return null;
		}
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(searchForMethodAnnotation)) {
				methods.add(method);
			}
		}
		return methods;
	}
}
