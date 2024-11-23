package proxyjdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class JDKDynamicProxyPattern {

	public static void main(String[] args) {
		GreetingService realService = new EnglishGreetingService();
		GreetingService proxyService = ProxyFactoryJDK.createProxy(realService, GreetingService.class);

		System.out.println("Real Service: ");
		System.out.println(realService.greet("World"));
		System.out.println("------------------------------");
		System.out.println("Proxy: ");
		System.out.println(proxyService.greet("World"));
	}

}

interface GreetingService {
	String greet(String name);
}

class EnglishGreetingService implements GreetingService {

	@Override
	public String greet(String name) {
		return "Hello, " + name + "!";
	}

}

class LogginInvocationHandler implements InvocationHandler {

	private final Object target;

	public LogginInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("Method: " + method.getName() + "is called with args "
				+ (args != null ? Arrays.toString(args) : "null"));

		Object result = method.invoke(target, args);

		System.out.println("Method: " + method.getName() + " return " + result);

		return result;
	}

}

class ProxyFactoryJDK {
	public static <T> T createProxy(T target, Class<T> interfaceTytpe) {
		return (T) Proxy.newProxyInstance(
				interfaceTytpe.getClassLoader(),
				new Class<?>[] { interfaceTytpe },
				new LogginInvocationHandler(target));

	}
}