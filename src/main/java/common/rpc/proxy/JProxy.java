package common.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 */
public class JProxy {

	public static void main(String[] args) {
		JInvocationHandler ji = new JInvocationHandler();
		Subject sub = (Subject) ji.bind(new RealSubject());
		System.out.println(sub.say("dengjie", 25));
	}

}

interface Subject {
	public String say(String name, int age);
}

class RealSubject implements Subject {

	@Override
	public String say(String name, int age) {
		return name + "," + age;
	}

}

class JInvocationHandler implements InvocationHandler {

	private Object object = null;

	public Object bind(Object object) {
		this.object = object;
		return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object tmp = method.invoke(this.object, args);
		return tmp;
	}

}