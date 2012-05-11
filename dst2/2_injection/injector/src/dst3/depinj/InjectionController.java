package dst3.depinj;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dst3.depinj.annotation.Component;
import dst3.depinj.annotation.ComponentId;
import dst3.depinj.annotation.Inject;

public class InjectionController implements IInjectionController {

	private static InjectionController instance = null;

	private HashMap<Class<?>, Object> singletonMap = new HashMap<Class<?>, Object>();
	private List<String> attrNames = new ArrayList<String>();

	private Long id = 0L;

	private InjectionController() {
	}

	public static InjectionController getController() {
		if (instance == null) {
			instance = new InjectionController();
		}
		return instance;
	}

	private synchronized Long assignId() {
		return id++;
	}

	@Override
	public synchronized void initialize(Object obj) throws InjectionException {
		boolean isComponentAnnotated = false;
		boolean hasValidComponentId = false;

		Component annotation = obj.getClass().getAnnotation(Component.class);

		// for (Annotation annotation : annotations) {
		if (annotation != null) {
			isComponentAnnotated = true;

			if (annotation.scope() == ScopeType.SINGLETON) {
				if (singletonMap.containsKey(obj.getClass())) {
					throw new InjectionException("Already a Singleton injected");
				}
				singletonMap.put(obj.getClass(), obj);
			}
		}

		if (!isComponentAnnotated)
			throw new InjectionException("Class is not a valid Component!");

		for (Class<?> clazz = obj.getClass(); clazz != null; clazz = clazz
				.getSuperclass()) {
			for (Field field : clazz.getDeclaredFields()) {
				for (Annotation fieldAnnotation : field
						.getDeclaredAnnotations()) {

					// componentId
					if (fieldAnnotation instanceof ComponentId) {
						if (field.getType().equals(Long.class)) {
							if (hasValidComponentId)
								throw new InjectionException(
										"id already set by subclass :S");
							hasValidComponentId = true;
							try {
								field.setAccessible(true);
								field.set(obj, assignId());
							} catch (IllegalArgumentException e) {
								throw new InjectionException(
										"Can't assign an id : "
												+ e.getMessage());
							} catch (IllegalAccessException e) {
								throw new InjectionException(
										"Can't assign an id : "
												+ e.getMessage());
							}
						}
					}

					// injected fields
					if (fieldAnnotation instanceof Inject) {
						Inject injectAnno = (Inject) fieldAnnotation;

						Class<?> type = injectAnno.specificType();

						try {
							if (type == Object.class) {
								field.setAccessible(true);
								type = field.getType();
								System.out.println(field.getType());
								field.set(obj, type.newInstance());
							} else {
								field.setAccessible(true);
								field.set(obj, type.newInstance());
							}
						} catch (Exception e) {
							if (injectAnno.required())
								throw new InjectionException(
										"Could not Instantiate required field : "
												+ e.getMessage());
						}

						if (attrNames.contains(field.getName())) {
							throw new InjectionException("Ambigous Fieldname: "
									+ field.getName());
						}
					}
				}
			}
		}

		if (!hasValidComponentId)
			throw new InjectionException(
					"Not a valid ID field of type Long found");
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T getSingletonInstance(Class<T> clazz)
			throws InjectionException {
		if (!singletonMap.containsKey(clazz.getClass()))
			try {
				singletonMap.put(clazz, clazz.newInstance());
			} catch (InstantiationException e) {
				throw new InjectionException("Intantiation not possible : "
						+ e.getMessage());
			} catch (IllegalAccessException e) {
				throw new InjectionException("Access not possible : "
						+ e.getMessage());
			}
		return (T) singletonMap.get(clazz.getClass());
	}
}
